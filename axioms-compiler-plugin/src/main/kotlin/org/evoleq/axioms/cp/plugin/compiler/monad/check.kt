package org.evoleq.axioms.cp.plugin.compiler.monad

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.*

//fun isMonad()

fun isMultiplySignature(function: IrFunction, clazz: IrClass): Boolean {
    val receiver = function.extensionReceiverParameter?.type as? IrSimpleType ?: return false
    val returnType = function.returnType as? IrSimpleType ?: return false

    val clazzSymbol = clazz.symbol

    // Expecting receiver type: F<F<T>>
    if (receiver.classifier != clazzSymbol) return false
    val outerArgs = receiver.arguments
    if (outerArgs.size != 1) return false

    val inner = outerArgs[0].typeOrNull as? IrSimpleType ?: return false
    if (inner.classifier != clazzSymbol) return false

    // Expecting return type: F<T>
    if (returnType.classifier != clazzSymbol) return false

    return true
}

fun isReturnSignature(function: IrFunction, clazz: IrClass): Boolean {
    if ( function.valueParameters.size != 1 ) return false
    if (function.typeParameters.size != 1) return false

    val paramType = function.valueParameters[0].type
    val genericType = function.typeParameters[0].symbol

    if (paramType != genericType.defaultType) return false


    val returnType = function.returnType as? IrSimpleType ?: return false

    val clazzSymbol = clazz.symbol
    // Expecting return type: F<T>
    if (returnType.classifier != clazzSymbol) return false

    return true
}
























fun hasExtensionFunctionInFile(file: IrFile, targetClass: IrClass, functionName: String): IrFunction? {
    return file.declarations
        .filterIsInstance<IrFunction>()
        .find { function ->
            function.name.asString() == functionName &&
                function.extensionReceiverParameter?.type?.classOrNull == targetClass.symbol
        }
}

fun IrFunction.matchesSignature(
    name: String,
    receiverType: IrType? = null,
    parameterTypes: List<IrType> = emptyList(),
    returnType: IrType? = null
): Boolean {
    if (this.name.asString() != name) return false

    // Extension receiver type
    if (receiverType != null) {
        if (this.extensionReceiverParameter?.type != receiverType) return false
    } else if (this.extensionReceiverParameter != null) {
        return false
    }

    // Value parameters
    if (this.valueParameters.size != parameterTypes.size) return false
    for ((param, expectedType) in this.valueParameters.zip(parameterTypes)) {
        if (param.type != expectedType) return false
    }

    // Return type
    if (returnType != null && this.returnType != returnType) return false

    return true
}
