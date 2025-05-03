package org.evoleq.axioms.ksp.processor.monad

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.evoleq.axioms.ksp.definition.Monad
import org.evoleq.axioms.ksp.processor.framework.processDeclarations
import org.evoleq.axioms.ksp.processor.functor.generateMapFunction

class MonadProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> =
        processDeclarations(
            Monad::class.qualifiedName!!,
            listOf(
                {c, g, l, i -> generateMapFunction(c, g, l, i)},
                {c, g, l, i -> generateMultiplyFunction(c, g, l, i)},
                {c, g, l, i -> generateApplicativeFromMonad(c, g, l, i) }
            ),
            resolver, codeGenerator, logger
        )


}

fun hasReturnFunction(): Boolean = TODO()

fun generateMultiplyFunction(declaration: KSClassDeclaration, codeGenerator: CodeGenerator, logger: KSPLogger, typeParameterIndex: Int) {

}

fun generateApplicativeFromMonad(declaration: KSClassDeclaration, codeGenerator: CodeGenerator, logger: KSPLogger, typeParameterIndex: Int) {

}