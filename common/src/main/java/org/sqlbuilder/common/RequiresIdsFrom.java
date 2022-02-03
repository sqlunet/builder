package org.sqlbuilder.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface RequiresIdsFrom
{
	RequiresIdFrom[] value();
}
