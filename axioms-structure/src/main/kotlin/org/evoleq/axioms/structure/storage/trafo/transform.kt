package org.evoleq.axioms.structure.storage.trafo

import org.evoleq.axioms.structure.storage.diagonal.DiagonalStorage
import org.evoleq.axioms.structure.storage.spectral.SpectralStorage

fun <D> DiagonalStorage<D>.toSpectralStorage(): SpectralStorage<D, D> = SpectralStorage(read, write)
fun <D> SpectralStorage<D, D>.toDiagonalStorage(): DiagonalStorage<D> = DiagonalStorage(read, write)