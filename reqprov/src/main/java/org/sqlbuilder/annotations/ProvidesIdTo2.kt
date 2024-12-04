package org.sqlbuilder.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE,
		ElementType.CONSTRUCTOR,
		ElementType.ANNOTATION_TYPE,
		ElementType.TYPE_PARAMETER,
		ElementType.TYPE_USE})
@Retention(RetentionPolicy.SOURCE)
public @interface ProvidesIdTo2
{
	String value();
}
