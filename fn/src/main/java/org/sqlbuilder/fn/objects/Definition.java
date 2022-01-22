package org.sqlbuilder.fn.objects;

public class Definition
{
	public final Character dict;

	public final String def;

	public Definition(final Character dict, final String definition)
	{
		super();
		this.dict = dict;
		this.def = definition;
	}

	public static Definition getDefinition(final String definition0)
	{
		Character dict = null;
		String definition = definition0;
		if (definition0.startsWith("COD"))
		{
			dict = 'O';
			definition = definition0.substring(3);
		}
		if (definition0.startsWith("FN"))
		{
			dict = 'F';
			definition = definition0.substring(2);
		}
		// noinspection ConstantConditions
		if (definition != null)
		{
			definition = definition.replaceAll("[ \t\n.:]*$|^[ \t\n.:]*", "");
		}
		return new Definition(dict, definition);
	}

	@Override
	public String toString()
	{
		return this.dict + "|<" + this.def + ">";
	}
}
