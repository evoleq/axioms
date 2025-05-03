package org.evoleq.axioms.cp.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class AxiomsIrPlugin(
    private val messageCollector: org.jetbrains.kotlin.cli.common.messages.MessageCollector
) : IrGenerationExtension {
    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
    ) {
        println("🚀 Axioms plugin generate() called")

        // pluginContext gives you access to symbols, types, etc.
        moduleFragment.accept(AxiomsIrVisitor(pluginContext, messageCollector), null)
    }



}
