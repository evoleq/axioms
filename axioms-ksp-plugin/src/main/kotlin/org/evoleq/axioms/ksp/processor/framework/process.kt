package org.evoleq.axioms.ksp.processor.framework

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

fun processDeclarations(qualifiedName: String, actions: List<(KSClassDeclaration, CodeGenerator, KSPLogger)->Unit> , resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger): List<KSAnnotated> {
    val symbols = resolver.getSymbolsWithAnnotation(qualifiedName)
    val ret = symbols.filterNot { it.validate() }.toList()

    for (symbol in symbols.filter { it is KSClassDeclaration && it.validate() }) {
        val classDecl = symbol as KSClassDeclaration
        actions.forEach { action ->
            action(classDecl, codeGenerator, logger)
        }
    }

    return ret
}