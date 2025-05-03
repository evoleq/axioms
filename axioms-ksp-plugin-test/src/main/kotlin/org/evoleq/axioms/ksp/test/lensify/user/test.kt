package org.evoleq.axioms.ksp.test.lensify.user

import org.evoleq.axioms.ksp.definition.Lensify
import org.evoleq.axioms.ksp.definition.ReadOnly
import org.evoleq.axioms.ksp.definition.ReadWrite

@Lensify
data class User(
    @ReadOnly val id: String,
    @ReadWrite val username: String,
    @ReadWrite val password: String
)

/**
val id: Lens<User, String>  by lazy {
    Lens(
        get = { w -> w.id },
        set = FORBIDDEN("Property 'id' is read-only")
    )
}

val username: Lens<User, String> by lazy {
    Lens(
        get = { w -> w.username },
        set = { p -> { w -> w.copy(username = p) } }
    )
}
val password: Lens<User, String> by lazy {
    Lens(
        get = { w -> w.password },
        set = { p -> { w -> w.copy(password = p) } }
    )
}

 */

