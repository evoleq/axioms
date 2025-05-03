package org.evoleq.axioms.processor.monad

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import org.evoleq.axioms.definition.Monad

class MonadProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Monad::class.qualifiedName!!)
        val ret = symbols.filterNot { it.validate() }.toList()

        for (symbol in symbols.filter { it is KSClassDeclaration && it.validate() }) {
            val classDecl = symbol as KSClassDeclaration
            generateMultiplyFunction(classDecl)
        }

        return ret
    }

    fun hasReturnFunction(): Boolean = TODO()

    fun generateMultiplyFunction(declaration: KSClassDeclaration) {

    }
}