package org.evoleq.axioms.ksp.processor.applicative

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import org.evoleq.axioms.ksp.definition.Applicative
import org.evoleq.axioms.ksp.definition.Functor
import org.evoleq.axioms.ksp.processor.framework.processDeclarations
import org.evoleq.axioms.ksp.processor.functor.generateMapFunction

class ApplicativeProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> =
        processDeclarations(
            Applicative::class.qualifiedName!!,
            listOf(
                {c, g, l -> generateMapFunction(c, g, l) },
                // TODO generate other functions
            ),
            resolver, codeGenerator, logger
        )
}