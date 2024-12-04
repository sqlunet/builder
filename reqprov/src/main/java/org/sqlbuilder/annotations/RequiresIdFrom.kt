package org.sqlbuilder.annotations

import kotlin.reflect.KClass

@JvmRepeatable(RequiresIdsFrom::class)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR
)
annotation class RequiresIdFrom
    (val type: KClass<*>)
