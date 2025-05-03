package org.evoleq.axioms.ksp.processor.lens

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import org.evoleq.axioms.ksp.definition.Lensify
import org.evoleq.axioms.ksp.processor.framework.processDeclarations

class LensProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> =
        processDeclarations(
            Lensify::class.qualifiedName!!,
            listOf(
                {c, g, l, i -> generateLensFile(c, g, l) },
            ),
            resolver, codeGenerator, logger
        )
}