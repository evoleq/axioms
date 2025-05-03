package org.evoleq.axioms.processor.functor

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.evoleq.axioms.definition.Functor




class FunctorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Functor::class.qualifiedName!!)
        val ret = symbols.filterNot { it.validate() }.toList()

        for (symbol in symbols.filter { it is KSClassDeclaration && it.validate() }) {
            val classDecl = symbol as KSClassDeclaration
            generateMapFunction(classDecl)
        }

        return ret
    }

    private fun generateMapFunction(classDecl: KSClassDeclaration) {
        val className = classDecl.simpleName.asString()
        val packageName = classDecl.packageName.asString()
        val type = ClassName(packageName, className)

        // Determine if lift() exists in companion
        val companion = classDecl.declarations
            .filterIsInstance<KSClassDeclaration>()
            .find { it.isCompanionObject }

        val liftExists = companion?.getDeclaredFunctions()
            ?.any {
                // it.hasSignatureOfLiftFunction()
                it.simpleName.asString() == "lift"
            } ?: false

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

}
