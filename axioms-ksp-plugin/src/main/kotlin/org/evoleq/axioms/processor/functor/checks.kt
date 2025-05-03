package org.evoleq.axioms.processor.functor

import com.google.devtools.ksp.symbol.*


// Function to check if a function matches the signature 'infix fun <S, T> lift(f: (S) -> T): (F<S>) -> F<T>'
fun KSFunctionDeclaration.hasSignatureOfLiftFunction(): Boolean {
    val function = this
    // Check if the function is infix (by checking its modifiers)
    if (!function.modifiers.contains(Modifier.INFIX)) {
        //logger.info("Function ${function.simpleName.asString()} is not infix")
        return false
    }

    // Check if it is a generic function with two type parameters
    val typeParameters = function.typeParameters
    if (typeParameters.size != 2) {
        //logger.info("Function ${function.simpleName.asString()} does not have exactly 2 type parameters")
        return false
    }

    // Check that the parameter is a function of the form (S) -> T
    val firstParameter = function.parameters.firstOrNull() ?: return false
    val parameterType = firstParameter.type.resolve()
    if (parameterType.arguments.size != 1 || !isFunctionType(parameterType)) {
        //logger.info("First parameter of ${function.simpleName.asString()} is not a function (S) -> T")
        return false
    }

    // Check if the return type is a parameterized type (F<S>) -> F<T>
    val returnType = function.returnType?.resolve()
    if (returnType?.arguments?.size != 1 || !isGenericType(returnType)) {
        //logger.info("Return type of ${function.simpleName.asString()} is not of the form F<S> -> F<T>")
        return false
    }

    // Further check that the return type is correctly parameterized as F<S> -> F<T>
    val returnTypeClass = returnType.declaration as? KSClassDeclaration
    if (returnTypeClass == null || returnTypeClass.typeParameters.size != 1) {
        //logger.info("Return type ${function.simpleName.asString()} is not a valid F<S> -> F<T>")
        return false
    }

    return true
}

// Helper function to check if the type is a function (S) -> T
private fun isFunctionType(type: KSType): Boolean {
    val classDeclaration = type.declaration as? KSClassDeclaration ?: return false
    return classDeclaration.simpleName.asString() == "Function" && type.arguments.size == 1
}

// Helper function to check if a type is a generic type like F<T>
private fun isGenericType(type: KSType): Boolean {
    val classDeclaration = type.declaration as? KSClassDeclaration ?: return false
    return classDeclaration.typeParameters.isNotEmpty()
}



//fun KSFunctionDeclaration.hasSignatureOfLiftFunction(): Boolean =
    /*
    simpleName.toString() == "lift" &&
    modifiers.contains(Modifier.INFIX)    &&
    typeParameters.size == 2 && // S, T
    parameters.size == 1 &&
    with(parameters.first()){
        type is KSFunction &&
        (this as KSFunction).parameterTypes.size == 1 &&
        (this as KSFunction).returnType!= null
    } &&
    returnType is KSFunction &&


     */