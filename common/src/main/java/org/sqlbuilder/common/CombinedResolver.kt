package org.sqlbuilder.common

import java.util.function.Function

data class Bunch<T, U> ( val first: T, val second: U )

/*
t->r
u->s
(t,u)->(r,s)
 */
class CombinedResolver<T, R, U, S>(
    private val r1: Function<T, R?>,
    private val r2: Function<U, S?>,
) : Function<Pair<T, U>, Pair<R, S>?> {

    override fun apply(inputs: Pair<T, U>): Pair<R, S>? {
        val v1: R? = r1.apply(inputs.first)
        val v2: S? = r2.apply(inputs.second)
        if (v1 == null || v2 == null)
            return null
        val n1: R = v1
        val n2: S = v2
        return n1 to n2
    }
}

class CombinedResolver2<T, R, U, S>(
    private val r1: Function<T, R?>,
    private val r2: Function<U, S?>,
) : Function<Bunch<T, U>, Bunch<R, S>?> {

    override fun apply(inputs: Bunch<T, U>): Bunch<R, S>? {
        val v1: R? = r1.apply(inputs.first)
        val v2: S? = r2.apply(inputs.second)
        if (v1 == null || v2 == null)
            return null
        val n1: R = v1
        val n2: S = v2
        return Bunch(n1, n2)
    }
}
