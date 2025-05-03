package org.evoleq.axioms.cp.def

interface Functor

interface Transformation : (Functor)->Functor

interface Monad : Functor