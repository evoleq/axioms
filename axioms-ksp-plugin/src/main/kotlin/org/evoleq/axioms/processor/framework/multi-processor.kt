package org.evoleq.axioms.processor.framework

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.processing.Resolver

class MultiProcessor(
    private val processors: List<SymbolProcessor>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val unprocessedSymbols = mutableListOf<KSAnnotated>()

        // Run each processor on the symbols
        for (processor in processors) {
            unprocessedSymbols.addAll(processor.process(resolver))
        }

        return unprocessedSymbols
    }
}
