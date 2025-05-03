package org.evoleq.axioms.ksp.processor.framework

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType


// Helper function to check if the type is a function (S) -> T
fun isFunctionType(type: KSType): Boolean {
    val classDeclaration = type.declaration as? KSClassDeclaration ?: return false
    return classDeclaration.simpleName.asString() == "Function" && type.arguments.size == 1
}

// Helper function to check if a type is a generic type like F<T>
fun isGenericType(type: KSType): Boolean {
    val classDeclaration = type.declaration as? KSClassDeclaration ?: return false
    return classDeclaration.typeParameters.isNotEmpty()
}

fun KSClassDeclaration.hasFunction(predicate: (KSFunctionDeclaration)->Boolean): Boolean =
    getDeclaredFunctions().any { predicate(it) }

fun KSFunctionDeclaration.isLiftFunction():Boolean =
    // it.hasSignatureOfLiftFunction()
    simpleName.asString() == "lift"