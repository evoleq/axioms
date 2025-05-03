package org.evoleq.axioms.test

import org.evoleq.axioms.definition.Functor

@Functor
class F<T>(val value: T) {

    companion object {
        fun <T> ret(value: T): F<T> = F(value)

        infix fun <S, T> lift(f: (S)->T): (F<S>)-> F<T> = {Fs: F<S> ->F(f(Fs.value))}
    }
}

fun <T> F<F<T>>.multiply() : F<T> = value

// infix fun <S, T> F<S>.map(f: (S)->T):F<T> = (F lift f)(this)

// operator fun <S, T> F<S>.times(kleisli: (S)->F<T>): F<T> = map(kleisli).multiply()



fun main() {

}