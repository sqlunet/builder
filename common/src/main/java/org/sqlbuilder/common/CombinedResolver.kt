package org.sqlbuilder.common

import org.sqlbuilder2.ser.Pair
import java.util.function.Function

/*
t->r
u->s
(t,u)->(r,s)
 */
class CombinedResolver<T, R, U, S>(private val r1: Function<T, R>, private val r2: Function<U, S>) : Function<Pair<T, U>, Pair<R, S>> {

    override fun apply(inputs: Pair<T, U>): Pair<R, S> {
        return Pair<R, S>(r1.apply(inputs.first), r2.apply(inputs.second))
    }
}
