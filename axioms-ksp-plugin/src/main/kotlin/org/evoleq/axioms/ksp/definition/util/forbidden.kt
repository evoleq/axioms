package org.evoleq.axioms.ksp.definition.util

@Suppress("FunctionName")
fun FORBIDDEN(reason: String): Nothing = throw Forbidden.Operation(reason)

sealed class Forbidden(override val message: String?): Exception() {
    data class Operation(val reason: String): Forbidden("Operation is forbidden: $reason")
}