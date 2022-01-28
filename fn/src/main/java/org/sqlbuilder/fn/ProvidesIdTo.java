package org.sqlbuilder.fn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.CLASS)
public @interface ProvidesIdTo
{
	Class<?> type();
}
