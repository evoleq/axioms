package org.evoleq.axioms.ksp.test.monad

import org.evoleq.axioms.ksp.definition.Monad

@Monad(1)
interface State<S, T> : (S)-> Pair<T, S> {
    companion object {
        infix fun <S, T, TPrime> lift(f:(T)-> TPrime): (State<S, T>) -> State<S, TPrime> = TODO()
    }
}

@Monad(1)
interface StateWithoutLift<S, T> : (S)-> Pair<T, S> {
    companion object {
        // infix fun <S, T, TPrime> lift(f:(T)-> TPrime): (State<S, T>) -> State<S, TPrime> = TODO()
    }
}

@Monad(1)
interface StateWithoutCompanion<S, T> : (S)-> Pair<T, S> {

}
