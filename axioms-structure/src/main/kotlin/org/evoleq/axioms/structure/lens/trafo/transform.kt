package org.evoleq.axioms.structure.lens.trafo

import org.evoleq.axioms.structure.lens.diagonal.DiagonalLens
import org.evoleq.axioms.structure.lens.spectral.SpectralLens

fun <W, P> DiagonalLens<W, P>.toSpectralLens(): SpectralLens<W, W, P, P> = SpectralLens(
    get, set
)

fun <W, P> SpectralLens<W, W, P, P>.toDiagonalLens(): DiagonalLens<W, P> = DiagonalLens(
    get, set
)

