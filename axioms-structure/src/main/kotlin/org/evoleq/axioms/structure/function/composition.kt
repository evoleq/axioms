package org.evoleq.axioms.structure.function

infix fun <R, S, T> ((S)->T).o(other: (R)->S): (R)->T = {r -> this(other(r)) }