package org.evoleq.axioms.ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import org.evoleq.axioms.ksp.processor.applicative.ApplicativeProcessor
import org.evoleq.axioms.ksp.processor.framework.MultiProcessor
import org.evoleq.axioms.ksp.processor.functor.FunctorProcessor
import org.evoleq.axioms.ksp.processor.lens.LensProcessor
import org.evoleq.axioms.ksp.processor.monad.MonadProcessor

class AxiomsProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MultiProcessor(listOf(
            FunctorProcessor(environment.codeGenerator, environment.logger),
            ApplicativeProcessor(environment.codeGenerator, environment.logger),
            MonadProcessor(environment.codeGenerator, environment.logger),
            LensProcessor(environment.codeGenerator, environment.logger),
        ))
    }
}