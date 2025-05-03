package org.evoleq.axioms.structure.trafo

import org.evoleq.axioms.structure.lens.diagonal.DiagonalLens
import org.evoleq.axioms.structure.lens.diagonal.times
import org.evoleq.axioms.structure.lens.spectral.SpectralLens
import org.evoleq.axioms.structure.lens.spectral.times
import org.evoleq.axioms.structure.storage.diagonal.DiagonalStorage
import org.evoleq.axioms.structure.storage.spectral.SpectralStorage

fun <W> DiagonalStorage<W>.asLens(): DiagonalLens<Unit, W> = DiagonalLens({read()}, write)
fun <W> DiagonalLens<Unit, W>.asStorage(): DiagonalStorage<W> = DiagonalStorage({get(Unit)}, set)

operator fun <W, P> DiagonalStorage<W>.times(lens: DiagonalLens<W, P>): DiagonalStorage<P> =
    (asLens() * lens).asStorage()


fun <V, W> SpectralStorage<V, W>.asLens(): SpectralLens<Unit, Unit, V, W> = SpectralLens({read()}, write)
fun <V, W> SpectralLens<Unit,Unit,V,  W>.asStorage(): SpectralStorage<V, W> = SpectralStorage({get(Unit)}, set)

operator fun <V, W, P, Q> SpectralStorage<V, W>.times(lens: SpectralLens<V, W, P, Q>): SpectralStorage<P, Q> =
    (asLens() * lens).asStorage()