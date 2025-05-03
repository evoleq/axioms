package org.evoleq.axioms.ksp.test.applicative

import org.evoleq.axioms.ksp.definition.Applicative


@Applicative
class ApplicativeWithCompanion<T>(val value: T) {

    companion object {
    }
}

@Applicative
class ApplicativeWithLift<T>(val value: T) {

    companion object {
        /**
         * Functoriality
         * lift function of [ApplicativeWithLift]
         */
        infix fun <S, T> lift(f: (S) -> T): (ApplicativeWithLift<S>)->ApplicativeWithLift<T> = TODO()
    }
}


// infix fun <S, T> F<S>.map(f: (S)->T):F<T> = (F lift f)(this)

// operator fun <S, T> F<S>.times(kleisli: (S)->F<T>): F<T> = map(kleisli).multiply()