package org.sqlbuilder.fn;

@jakarta.xml.bind.annotation.XmlTransient
public abstract class Base
{
	@jakarta.xml.bind.annotation.XmlTransient
	public Object parent;

	void setParent(final Object parent)
	{
		this.parent = parent;
	}

	public Object getParent()
	{
		return parent;
	}
}