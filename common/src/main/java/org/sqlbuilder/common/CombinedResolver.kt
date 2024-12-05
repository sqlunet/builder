package org.sqlbuilder.common

/*
t->r
u->s
(t,u)->(r,s)
 */
class CombinedResolver<T, R, U, S>(
    private val r1: (T) -> R?,
    private val r2: (U) -> S?,
) : (Pair<T, U>) -> Pair<R, S>? {

    override fun invoke(inputs: Pair<T, U>): Pair<R, S>? {
        val v1: R? = r1.invoke(inputs.first)
        val v2: S? = r2.invoke(inputs.second)
        if (v1 == null || v2 == null)
            return null
        val n1: R = v1
        val n2: S = v2
        return n1 to n2
    }
}
