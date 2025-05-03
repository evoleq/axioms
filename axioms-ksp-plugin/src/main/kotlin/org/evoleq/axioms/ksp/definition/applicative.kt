package org.evoleq.axioms.ksp.definition

import org.evoleq.axioms.ksp.definition.util.ImplMode

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Applicative(
    val typeParameterIndex: Int = 0,
    val implMode: ImplMode = ImplMode.STANDARD
)