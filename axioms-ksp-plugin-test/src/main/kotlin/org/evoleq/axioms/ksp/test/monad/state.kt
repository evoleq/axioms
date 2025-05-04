package org.evoleq.axioms.ksp.test.monad

import org.evoleq.axioms.ksp.definition.Monad

@Monad(1)
data class State<S, T>(private val run: (S)->Pair<T, S>) : (S)-> Pair<T, S> by run {

    companion object {
        /**
         * Functorialiy of [State]
         */
        infix fun <S, T, TPrime> lift(f:(T)-> TPrime): (State<S, T>) -> State<S, TPrime> = TODO()

        /**
         * Applicative structure of [State]: Pure
         */
        fun <S, T> pure(value: T): State<S, T> = ret(value)

        /**
         * Applicative structure of [State]: Apply
         */
        fun <S, T, TPrime> apply(state: State<S, (T)->TPrime>): (State<S, T>)-> State<S,TPrime> = {
            source ->
                val f: ((T)->TPrime)->State<S, TPrime>  = {g  -> lift<S, T, TPrime>(g)(source) }
                val square = lift<S, (T)->TPrime, State<S,  TPrime>> (f)(state)
                multiply(square)
        }

        /**
         * Monadic structure of [State]: Return function
         */
        fun <S, T> ret(value: T): State<S, T> = TODO()

        /**
         * Monadic structure of [State]: Multiplication function
         */
        fun <S, T> multiply(state: State<S, State<S, T>>): State<S, T> = TODO()
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
