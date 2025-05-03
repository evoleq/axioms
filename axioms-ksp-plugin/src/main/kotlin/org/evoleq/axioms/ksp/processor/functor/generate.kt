package org.evoleq.axioms.ksp.processor.functor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.evoleq.axioms.ksp.processor.framework.hasFunction
import org.evoleq.axioms.ksp.processor.framework.isLiftFunction


fun generateMapFunction(classDecl: KSClassDeclaration, codeGenerator: CodeGenerator, logger: KSPLogger, typeParameterIndex: Int = 0) {
    val className = classDecl.simpleName.asString()
    val packageName = classDecl.packageName.asString()
    val type = ClassName(packageName, className)

    // Determine if lift() exists in companion
    val companion = classDecl.declarations
        .filterIsInstance<KSClassDeclaration>()
        .find { it.isCompanionObject }

    val liftExists = companion?.hasFunction { it.isLiftFunction() }?: false

    val typeParamNames = classDecl.typeParameters.map { it.name.asString() }
    val original = typeParamNames[typeParameterIndex]
    val mapped = "${original}Prime"

// The full list of type parameters in the return type (only the functorial one is changed)
    val mappedParameters = typeParamNames.mapIndexed { index, name ->
        if (index == typeParameterIndex) mapped else name
    }

// Build the function
    val mapFun = FunSpec.builder("map")
        .addModifiers(KModifier.INFIX)
        // Add all original type variables
        .apply {
            typeParamNames.forEach { addTypeVariable(TypeVariableName(it)) }
            addTypeVariable(TypeVariableName(mapped)) // Add the new mapped one
        }
        // Receiver uses all original type parameters
        .receiver(type.parameterizedBy(*typeParamNames.map { TypeVariableName(it) }.toTypedArray()))
        // Add the mapping function f: (Original) -> Mapped
        .addParameter("f", LambdaTypeName.get(
            parameters = arrayOf(TypeVariableName(original)),
            returnType = TypeVariableName(mapped)
        ))
        // Return type uses all parameters, with the mapped one swapped in
        .returns(type.parameterizedBy(*mappedParameters.map { TypeVariableName(it) }.toTypedArray()))
        .addKdoc("Functoriality:\nThe map function of [%L]", className)


    if (liftExists) {
        mapFun.addStatement("return (%T.lift<${(typeParamNames + mapped).joinToString(", ") { it }}> (f))(this)", type.nestedClass("Companion"))
    } else {
        mapFun.addStatement(
            "return TODO(%S)",
            "Add the function 'infix fun <${(typeParamNames + mapped).joinToString(", ") { it }}> lift(f: ($original) -> $mapped): ($className<${typeParamNames.joinToString(", ") { it }}>)->$className<${mappedParameters.joinToString(", ") { it }}> ' to the companion object of $className"
        )
    }

    val fileSpec = FileSpec.builder(packageName, "${className}Map")
        .addFunction(mapFun.build())
        .build()

    val file = codeGenerator.createNewFile(
        Dependencies(false, classDecl.containingFile!!),
        packageName,
        "${className}Map"
    )

    file.bufferedWriter().use { writer -> fileSpec.writeTo(writer) }
}
