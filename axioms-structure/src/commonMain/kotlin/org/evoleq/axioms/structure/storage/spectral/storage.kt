package org.evoleq.axioms.structure.storage.spectral



typealias SpectralStorage<I, O> = Storage<I, O>

data class Storage<out  O, in I>(
    val read: ()->O,
    val write: (I)->(Unit)->Unit
) {
    companion object {
        infix fun <O, P, I> lift(f: (O)->P): (Storage<O, I>)->Storage<P, I> =
            {soi: Storage<O,I> -> Storage(read = {f(soi.read())}, write = soi.write)}
        infix fun <O, I, J> contraLift(g: (J)->I): (Storage<O,I>)->Storage<O,J> =
            {soi: Storage<O, I> -> Storage(read = soi.read, write = {j:J -> soi.write(g(j) )})}
    }
}

infix fun <O,P,I> Storage<O,I>.map(f: (O)->P): Storage<P,I> = (Storage.lift<O,P,I> (f))(this)

infix fun <O,I,J> Storage<O,I>.contraMap(g: (J)->I): Storage<O,J> = (Storage.contraLift<O,I,J> (g))(this)