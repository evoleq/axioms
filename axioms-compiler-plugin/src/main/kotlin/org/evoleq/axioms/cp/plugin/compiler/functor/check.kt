package org.evoleq.axioms.cp.plugin.compiler.functor

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.fqNameForIrSerialization
import org.jetbrains.kotlin.ir.util.functions

fun hasLiftFunction(clazz: IrClass): Boolean {
    val companion = clazz.companionObject() ?: return false
    val classSymbol = clazz.symbol

    return companion.functions.any { fn ->
        fn.name.asString() == "lift" &&
            fn.typeParameters.size == 2 &&
            fn.valueParameters.size == 1 &&
            isFunctionTypeFromSToT(fn.valueParameters[0].type, fn.typeParameters) &&
            isReturnTypeFunctionFStoFT(fn.returnType, classSymbol, fn.typeParameters)
    }
}

fun isFunctionTypeFromSToT(type: IrType, typeParams: List<IrTypeParameter>): Boolean {
    if (type !is IrSimpleType || (type.classifierOrNull as? IrClassSymbol)?.fqNameSafe() != "kotlin.Function1") return false

    val (sParam, tParam) = typeParams.takeIf { it.size == 2 } ?: return false
    val (arg, ret) = type.arguments.takeIf { it.size == 2 }?.mapNotNull { (it as? IrTypeProjection)?.type } ?: return false

    return arg.classifierOrNull == sParam.symbol && ret.classifierOrNull == tParam.symbol
}

fun isReturnTypeFunctionFStoFT(type: IrType, classSymbol: IrClassSymbol, typeParams: List<IrTypeParameter>): Boolean {
    if (type !is IrSimpleType || (type.classifierOrNull as? IrClassSymbol)?.fqNameSafe() != "kotlin.Function1") return false

    val (sParam, tParam) = typeParams.takeIf { it.size == 2 } ?: return false
    val (arg, ret) = type.arguments.takeIf { it.size == 2 }?.mapNotNull { (it as? IrTypeProjection)?.type as? IrSimpleType } ?: return false

    return arg.classifierOrNull == classSymbol &&
        ret.classifierOrNull == classSymbol &&
        arg.arguments.singleOrNull()?.typeOrNull?.classifierOrNull == sParam.symbol &&
        ret.arguments.singleOrNull()?.typeOrNull?.classifierOrNull == tParam.symbol
}

fun IrClassSymbol.fqNameSafe(): String =
    this.owner.parent.fqNameForIrSerialization.asString() + "." + this.owner.name.asString()
