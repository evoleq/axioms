package org.evoleq.axioms.structure.lens.spectral

import org.evoleq.axioms.structure.function.o

typealias SpectralLens<V, W, P, Q> = Lens<V, W, P, Q>

data class Lens<in V, out W, out P, in Q> (
    val get: (V)-> P,
    val set: (Q)-> (V) -> W
)

/**
 * [Lens] multiplication
 */
operator fun <V, W, P, Q, K,L > Lens<V, W, P, Q>.times(other: Lens<P, Q, K, L>): Lens<V, W, K ,L> = Lens(
    get = other.get o get,
    set = {l:L -> {v: V -> set( other.set(l)(get(v) ))( v )}  }
)

