package org.evoleq.axioms.ksp.test.monad

import org.evoleq.axioms.ksp.definition.Monad


@Monad
class MonadWithRetAndMultFunctions<T>(val value: T) {

    companion object {
        fun <T> ret(value: T): MonadWithRetAndMultFunctions<T> = MonadWithRetAndMultFunctions(value)

        // infix fun <S, T> lift(f: (S)->T): (F<S>)-> F<T> = { Fs: F<S> -> F(f(Fs.value)) }
    }
}

fun <T> MonadWithRetAndMultFunctions<MonadWithRetAndMultFunctions<T>>.multiply() : MonadWithRetAndMultFunctions<T> = value

// infix fun <S, T> F<S>.map(f: (S)->T):F<T> = (F lift f)(this)

// operator fun <S, T> F<S>.times(kleisli: (S)->F<T>): F<T> = map(kleisli).multiply()