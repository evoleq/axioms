package org.evoleq.axioms.structure.lens.diagonal

import org.evoleq.axioms.structure.lens.spectral.times
import org.evoleq.axioms.structure.lens.trafo.toDiagonalLens
import org.evoleq.axioms.structure.lens.trafo.toSpectralLens

typealias DiagonalLens<W, P> = Lens<W, P>

data class Lens<W, P> (
    val get: (W)-> P,
    val set: (P)-> (W) -> W
)

operator fun <W, P, Q> Lens<W, P>.times(other: Lens<P, Q>): Lens<W, Q> =
    (toSpectralLens() * other.toSpectralLens()).toDiagonalLens()
