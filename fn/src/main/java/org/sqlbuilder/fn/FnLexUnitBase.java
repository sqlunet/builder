package org.sqlbuilder.fn;

public class FnLexUnitBase
{
	public static class Definition
	{
		public final Character dict;

		public final String def;

		public Definition(final Character dict, final String definition)
		{
			super();
			this.dict = dict;
			this.def = definition;
		}

		@Override
		public String toString()
		{
			return this.dict + "|<" + this.def + ">";
		}
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

	public static void main(final String[] args)
	{
		for (final String arg : args)
		{
			final Definition definition = FnLexUnitBase.getDefinition(arg);
			System.out.println("<" + definition.def + ">");
		}
	}
}
