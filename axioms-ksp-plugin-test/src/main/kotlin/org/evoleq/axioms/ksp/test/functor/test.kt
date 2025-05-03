package org.evoleq.axioms.ksp.test.functor

import org.evoleq.axioms.ksp.definition.Functor



@Functor
class FuncWithoutCompanion<T>

@Functor
class FuncWithCompanion<T> {
    companion object { }
}

@Functor
data class FuncWithLift<T>(val value: T) {
    companion object {
        infix fun <S, T> lift(f: (S)->T): (FuncWithLift<S>)-> FuncWithLift<T> = { Fs: FuncWithLift<S> -> FuncWithLift(f(Fs.value)) }
    }
}