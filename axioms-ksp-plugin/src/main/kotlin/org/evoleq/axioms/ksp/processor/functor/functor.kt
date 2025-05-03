package org.evoleq.axioms.ksp.processor.functor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.evoleq.axioms.ksp.definition.Functor
import org.evoleq.axioms.ksp.definition.Monad
import org.evoleq.axioms.ksp.processor.framework.hasFunction
import org.evoleq.axioms.ksp.processor.framework.isLiftFunction
import org.evoleq.axioms.ksp.processor.framework.processDeclarations
import org.evoleq.axioms.ksp.processor.monad.generateMultiplyFunction


class FunctorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> =
        processDeclarations(
            Functor::class.qualifiedName!!,
            listOf(
                {c, g, l -> generateMapFunction(c, g, l)},
            ),
            resolver, codeGenerator, logger
        )
}
