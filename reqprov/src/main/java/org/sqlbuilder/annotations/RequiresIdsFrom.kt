package org.sqlbuilder.annotations

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR
)
annotation class RequiresIdsFrom
    (vararg val value: RequiresIdFrom)
