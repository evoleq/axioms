package org.evoleq.axioms.ksp.processor.functor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.evoleq.axioms.ksp.processor.framework.hasFunction
import org.evoleq.axioms.ksp.processor.framework.isLiftFunction


fun generateMapFunction(classDecl: KSClassDeclaration, codeGenerator: CodeGenerator, logger: KSPLogger) {
    val className = classDecl.simpleName.asString()
    val packageName = classDecl.packageName.asString()
    val type = ClassName(packageName, className)

    // Determine if lift() exists in companion
    val companion = classDecl.declarations
        .filterIsInstance<KSClassDeclaration>()
        .find { it.isCompanionObject }

    val liftExists = companion?.hasFunction { it.isLiftFunction() }?: false

    val mapFun = FunSpec.builder("map")
        .receiver(type.parameterizedBy(TypeVariableName("S")))
        .addModifiers(KModifier.INFIX)
        .addTypeVariable(TypeVariableName("S"))
        .addTypeVariable(TypeVariableName("T"))
        .addParameter("f", LambdaTypeName.get(TypeVariableName("S"), returnType = TypeVariableName("T")))
        .returns(type.parameterizedBy(TypeVariableName("T")))
        .addKdoc("Functoriality:\nThe map function of [%L]", className)

    if (liftExists) {
        mapFun.addStatement("return (%T lift f)(this)", type.nestedClass("Companion"))
    } else {
        mapFun.addStatement(
            "return TODO(%S)",
            "Add the function 'infix fun <S, T> lift(f: (S) -> T): ($className<S>)->$className<S> ' to the companion object of $className"
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
