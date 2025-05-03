package org.evoleq.axioms.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import org.evoleq.axioms.processor.framework.MultiProcessor
import org.evoleq.axioms.processor.functor.FunctorProcessor

class AxiomsProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MultiProcessor(listOf(
            FunctorProcessor(environment.codeGenerator, environment.logger)

        ))
    }
}