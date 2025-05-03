package org.evoleq.axioms.ksp.test.functor

class Id {
    companion object {
        operator fun <T> invoke(value: T): T = value
        infix fun <S, T> lift(f: (S)->T): (S)->T = f
    }
}

fun <S, T> S.map(f: (S)->T): T = f(this)
