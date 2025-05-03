package org.evoleq.axioms.cp.plugin.compiler.functor

import org.evoleq.axioms.cp.plugin.compiler.shared.ensureCompanionObject
import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.addTypeParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl

@OptIn(FirIncompatiblePluginAPI::class)
fun generateLiftFunction(
    pluginContext: IrPluginContext,
    functorClass: IrClass, // the Func<T> class

): IrSimpleFunction {

    val companionObject = functorClass.ensureCompanionObject(pluginContext)
    val builtIns = pluginContext.irBuiltIns
    val funcClassSymbol = functorClass.symbol // Get the symbol of the current class (Func<T>)

    val function = pluginContext.irFactory.buildFun {
        name = Name.identifier("lift")
        visibility = DescriptorVisibilities.PUBLIC
        modality = Modality.FINAL
        origin = IrDeclarationOrigin.DEFINED
        isInfix = true
    }.apply {
        parent = companionObject
    }

    // Type parameters S and T
    val typeParamS = function.addTypeParameter("S", builtIns.anyNType)
    val typeParamT = function.addTypeParameter("T", builtIns.anyNType)

    // Create IR types for the parameters S and T
    val irTypeS = IrSimpleTypeImpl(
        typeParamS.symbol,  // The symbol of the type parameter 'S'
        false,              // Nullable flag (false means non-nullable)
        emptyList(),        // Arguments for the type (empty in this case)
        emptyList()         // Annotations
    )

    val irTypeT = IrSimpleTypeImpl(
        typeParamT.symbol,  // The symbol of the type parameter 'T'
        false,              // Nullable flag
        emptyList(),        // Arguments for the type
        emptyList()         // Annotations
    )

    // Now you can create a function or other constructs using the types `irTypeS` and `irTypeT`
    val funcSType = funcClassSymbol.owner.typeWith(irTypeS)
    val funcTType = funcClassSymbol.owner.typeWith(irTypeT)

    // Create the f: (S) -> T function type
    // create the function type with type parameters S and T
    val fType = builtIns.functionN(1).typeWith(irTypeS, irTypeT)  // Use the manually created types for S and T

    // Manually create the parameter
    val fParam = pluginContext.irFactory.createValueParameter(
        //parent = function, // function is the parent here
        index = 0, // First parameter
        name = Name.identifier("f"), // Name of the parameter
        type = fType, // The type of the parameter, which is (S) -> T
        startOffset = -1, // Placeholder for start offset (use a real value in actual code)
        endOffset = -1, // Placeholder for end offset (use a real value in actual code)
        origin = IrDeclarationOrigin.DEFINED, // Origin of the declaration, typically DEFINED
        symbol =  IrValueParameterSymbolImpl(),// Symbol for this value parameter
        varargElementType = null, // Not a vararg, so set this to null
        isCrossinline = false, // Not crossinline
        isNoinline = false, // Not noinline
        isHidden = false, // Not hidden
        isAssignable = false // The parameter is not assignable
    )

    fParam.parent = function
    // Add parameter to function
    function.valueParameters += fParam

    // Return type: (Func<S>) -> Func<T>
    val returnType = builtIns.functionN(1).typeWith(funcSType, funcTType)
    function.returnType = returnType

    // Body: TODO()
    val todoFunction = pluginContext.referenceFunctions(FqName("kotlin.TODO")).first()
    function.body = DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
        +irReturn(irCall(todoFunction)
            /*.apply {
            putValueArgument(0, irString("Not implemented"))
        }*/)
    }

/*
    // Add the function to the companion object's declarations
    val companionObject = functorClass.companionObject()
    if (companionObject != null) {
        companionObject.declarations += function
        function.parent = companionObject
    } else {
        // If the class doesn't have a companion object, create one and add the function there
        val newCompanionObject = pluginContext.irFactory.buildClass {
            name = Name.identifier("Companion")
            origin = IrDeclarationOrigin.FILE_CLASS
            visibility = DescriptorVisibilities.PUBLIC
            isCompanion = true
        }.apply {
         //  parent = functorClass
        }

        newCompanionObject.parent = functorClass
        function.parent = newCompanionObject
        newCompanionObject.declarations += function
        functorClass.declarations += newCompanionObject
    }
*/
    return function
}


/*
fun generateMapFunction(
    context: IrPluginContext,
    irClass: IrClass,
    companion: IrClass,
    liftFunction: IrSimpleFunction,
    targetFile: IrFile
) {
    val irFactory = context.irFactory
    val builtIns = context.irBuiltIns

    val startOffset = SYNTHETIC_OFFSET
    val endOffset = SYNTHETIC_OFFSET

    // Create type parameters: S, T
    val typeParamSSymbol = IrTypeParameterSymbolImpl()
    val typeParamS = context.irFactory.createTypeParameter(
        startOffset,
        endOffset,
        IrDeclarationOrigin.DEFINED, // or LOCAL/PLUGIN if synthetic
        typeParamSSymbol,
        Name.identifier("S"),
        0, // index
        false, // isReified
        Variance.INVARIANT,

    ).apply {
     //   parent = parentDeclaration // e.g., the map function you're building
    }
    val typeParamTSymbol = IrTypeParameterSymbolImpl()
    val typeParamT = context.irFactory.createTypeParameter(
        startOffset,
        endOffset,
        IrDeclarationOrigin.DEFINED,
        typeParamTSymbol,
        Name.identifier("T"),
        1,
        false,
        Variance.INVARIANT,
    ).apply {
     //   parent = parentDeclaration
    }

    val typeParamList = listOf(typeParamS, typeParamT)

    // Build F<S> and F<T>
    val fOfS = irClass.defaultType.substitute(mapOf(irClass.typeParameters[0].symbol to typeParamS.defaultType))
    val fOfT = irClass.defaultType.substitute(mapOf(irClass.typeParameters[0].symbol to typeParamT.defaultType))

    // Build the function (F<S>.map(f: (S)->T): F<T>)
    val mapFun = irFactory.buildFun {
        name = Name.identifier("map")
        returnType = fOfT
        origin = IrDeclarationOrigin.DEFINED
        isInfix = true
    }.apply {
        parent = targetFile
        typeParameters += typeParamList

        // Extension receiver: F<S>
        extensionReceiverParameter = irFactory.createValueParameter(
            startOffset = startOffset,
            endOffset = endOffset,
            origin = IrDeclarationOrigin.DEFINED,
            name = Name.identifier("value"),
            index = -1,
            type = typeParamS.defaultType, // Use the type parameter you defined earlier
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false,
            symbol = IrValueParameterSymbolImpl()
            ).apply {
                // parent = function // Set this to the IrFunction that owns this parameter
        }
        // Parameter: (S) -> T
        valueParameters += irFactory.createValueParameter(
            startOffset = startOffset,
            endOffset = endOffset,
            origin = IrDeclarationOrigin.DEFINED,
            name = Name.identifier("value"),
            index = 0,
            type = typeParamS.defaultType, // Use the type parameter you defined earlier
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false,
            symbol = IrValueParameterSymbolImpl()
        ).apply {
            // parent = function // Set this to the IrFunction that owns this parameter
        }
        // Function body
        body = irFactory.createBlockBody(startOffset, endOffset) {
            val callLift = IrCallImpl(
                startOffset, endOffset,
                type = builtIns.functionN(1).typeWith(fOfS, fOfT),
                symbol = liftFunction.symbol,
                typeArgumentsCount = 2,
                valueArgumentsCount = 1
            ).apply {
                putTypeArgument(0, typeParamS.defaultType)
                putTypeArgument(1, typeParamT.defaultType)
                putValueArgument(0, IrGetValueImpl(startOffset, endOffset, valueParameters[0].symbol))
                dispatchReceiver = IrGetObjectValueImpl(
                    startOffset, endOffset,
                    companion.defaultType,
                    companion.symbol
                )
            }

            val callInvoke = IrCallImpl(
                startOffset, endOffset,
                type = fOfT,
                symbol = builtIns.functionN(1).functions
                    .single { it.symbol.owner.name.asString() == "invoke" && it.symbol.owner.valueParameters.size == 1 }.symbol,
                typeArgumentsCount = 0,
                valueArgumentsCount = 1
            ).apply {
                dispatchReceiver = callLift
                putValueArgument(0, IrGetValueImpl(startOffset, endOffset, extensionReceiverParameter!!.symbol))
            }

            statements += IrReturnImpl(
                startOffset, endOffset,
                builtIns.nothingType,
                symbol,
                callInvoke
            )
        }
    }

    targetFile.declarations += mapFun
}
 */

