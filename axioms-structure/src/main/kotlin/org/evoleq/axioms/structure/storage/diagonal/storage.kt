package org.evoleq.axioms.structure.storage.diagonal

typealias DiagonalStorage<Data> = Storage<Data>

data class Storage<Data>(
    val read: ()->Data,
    val write: (Data)->(Unit)->Unit
)



