package org.evoleq.axioms.cp.plugin.compiler


import org.evoleq.axioms.cp.def.Functor
import org.evoleq.axioms.cp.def.Monad
import org.evoleq.axioms.cp.plugin.compiler.functor.generateLiftFunction
// import org.evoleq.axioms.plugin.compiler.functor.generateMapFunction
import org.evoleq.axioms.cp.plugin.compiler.functor.hasLiftFunction
import org.evoleq.axioms.cp.plugin.compiler.monad.hasExtensionFunctionInFile
import org.evoleq.axioms.cp.plugin.compiler.monad.isMultiplySignature
import org.evoleq.axioms.cp.plugin.compiler.monad.isReturnSignature
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class AxiomsIrVisitor(
    private val context: IrPluginContext,
    private val messageCollector: MessageCollector
) : IrElementVisitor<Unit, Nothing?> {

    private val monadInterfaceSymbol: IrClassSymbol
    private val functorInterfaceSymbol: IrClassSymbol

    init {
        // Use ClassDescriptor to resolve the symbols
        monadInterfaceSymbol = irClassSymbol(FqName(Monad::class.qualifiedName!!))
        functorInterfaceSymbol = irClassSymbol(FqName(Functor::class.qualifiedName!!))
    }


    override fun visitElement(element: IrElement, data: Nothing?) {

        element.acceptChildren(this, data)
    }

    // Visit the class and check for Monad and Functor requirements
    override fun visitClass(declaration: IrClass, data: Nothing?) {

        println("Visiting class: ${declaration.name}")

        // Check if the class implements either Monad or Functor
        if (implementsInterface(declaration, monadInterfaceSymbol)) {
            println("Visiting Monad class: ${declaration.name}")




            // Check for necessary functions like 'Ret' and 'multiply' in the companion object
            val companionObject = declaration.companionObject()
            val missingFunctions = mutableListOf<String>()

            println(companionObject?.functions?.map { it }?.joinToString { it.name.asString() })

            // Check if 'Ret' function is present in the companion
            val retFunction = companionObject?.functions?.find { it.name.asString() == "ret" }
            if (retFunction == null) {

                missingFunctions.add("ret")
            }
            val hasReturnFunction = isReturnSignature(retFunction!!, declaration)
            if(!hasReturnFunction) {
                missingFunctions.add("ret")
            }


            val file = declaration.file
            val multiplyExtension = hasExtensionFunctionInFile(file, declaration, "multiply")
            if (multiplyExtension == null) {
                missingFunctions.add("multiply")
            }
            val hasMultiplyFunction = isMultiplySignature(multiplyExtension!!, declaration)
            if(!hasMultiplyFunction) {
                missingFunctions.add("multiply")
            }

            if (missingFunctions.isNotEmpty()) {

                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Class ${declaration.name} must implement required Monad functions: ${missingFunctions.joinToString(", ") { it }}",
                )

            }
        }

        // Check if the class implements Functor interface
        if (implementsInterface(declaration, functorInterfaceSymbol)) {
            println("Visiting Functor class: ${declaration.name}")


            if (!hasLiftFunction(declaration)) {

                generateLiftFunction(
                    context,
                    declaration
                )



                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Class ${declaration.name} must implement lift function of Functor",
                )



            }


            /*
            generateMapFunction(
                context,
                declaration,
                declaration.companionObject()!!,
                declaration.companionObject()?.functions?.find { it.name.asString() == "lift" }!!,
                declaration.file
            )
*/



        }

        // Visit children (if any)
        declaration.acceptChildren(this, data)
    }

    // Function to check if a class implements a specific interface
    private fun implementsInterface(irClass: IrClass, interfaceSymbol: IrClassSymbol): Boolean {
        // Check if any of the super types of the class matches the interface symbol
        return irClass.superTypes.any { superType ->
            superType.classOrNull == interfaceSymbol
        }
    }

    /*
    // Function to generate Ret() in the companion object
    private fun generateRetFunction(irClass: IrClass) {
        val retFunction = IrFunctionImpl(
            irClass.startOffset, irClass.endOffset, IrDeclarationOrigin.DEFINED,
            IrSimpleFunctionSymbolImpl("Ret")
        )
        // Set function properties here, like return type, parameters, etc.
        irClass.addFunction(retFunction) // You need to implement this method to add the function to the class or companion.
    }

    // Function to generate multiply() in the class
    private fun generateMultiplyFunction(irClass: IrClass) {
        val multiplyFunction = IrFunctionImpl(
            irClass.startOffset, irClass.endOffset, IrDeclarationOrigin.DEFINED,
            IrSimpleFunctionSymbolImpl("multiply")
        )
        // Set function properties here, like return type, parameters, etc.
        irClass.addFunction(multiplyFunction) // You need to implement this method to add the function to the class.
    }
*/
    // Helper function to find a ClassDescriptor from a fully qualified name
    private fun irClassSymbol(fqName: FqName): IrClassSymbol {


        val classDescriptor = context.referenceClass(ClassId.topLevel(fqName))
            ?: throw IllegalStateException("Class not found: $fqName")
        return classDescriptor
    }
}