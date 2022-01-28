package org.sqlbuilder.fn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Repeatable(RequiresIdsFrom.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface RequiresIdFrom
{
	Class<?> type();
}
