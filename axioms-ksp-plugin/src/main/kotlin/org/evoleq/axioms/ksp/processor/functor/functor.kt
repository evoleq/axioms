package org.evoleq.axioms.ksp.processor.functor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import org.evoleq.axioms.ksp.definition.Functor
import org.evoleq.axioms.ksp.processor.framework.processDeclarations


class FunctorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> =
        processDeclarations(
            Functor::class.qualifiedName!!,
            listOf(
                {c, g, l, i -> generateMapFunction(c, g, l, i)},
            ),
            resolver, codeGenerator, logger
        )
}
