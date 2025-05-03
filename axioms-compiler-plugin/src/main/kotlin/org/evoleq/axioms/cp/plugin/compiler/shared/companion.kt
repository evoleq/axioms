package org.evoleq.axioms.cp.plugin.compiler.shared

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.createImplicitParameterDeclarationWithWrappedDescriptor
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
fun IrClass.ensureCompanionObject(pluginContext: IrPluginContext): IrClass {
    // If already present, return it
    companionObject()?.let { return it }

    // Create a new companion object
    val companion = pluginContext.irFactory.buildClass {
        name = Name.identifier("Companion")
        kind = ClassKind.OBJECT
        isCompanion = true
        visibility = org.jetbrains.kotlin.descriptors.DescriptorVisibilities.PUBLIC
        modality = Modality.FINAL
        origin = IrDeclarationOrigin.DEFINED
    }.apply {
        parent = this@ensureCompanionObject
        createImplicitParameterDeclarationWithWrappedDescriptor()
    }

    // Add it to declarations
    this.declarations += companion
    this.metadata = this.metadata // force metadata update

    return companion
}