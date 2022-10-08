package org.sqlbuilder.su;

import com.articulate.sigma.IterableFormula;
import com.articulate.sigma.kif.StreamTokenizer_s;

import org.sqlbuilder.su.objects.Arg;
import org.sqlbuilder.su.objects.Formula;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class designed to read a file in SUO-KIF format into memory. See <a href="https://suo.ieee.org/suo-kif.html">suo-kif</a> for a language specification.
 *
 * @author Adam Pease
 * @author Bernard Bou (rewrite)
 */
public class FormulaParser
{
	private FormulaParser()
	{
	}

	/**
	 * Parse
	 *
	 * @param formula formula
	 * @return parses
	 * @throws IllegalArgumentException illegal
	 * @throws ParseException parse
	 * @throws IOException io exception
	 */
	public static Map<String, Arg> parse(final com.articulate.sigma.Formula formula) throws IllegalArgumentException, ParseException, IOException
	{
		final Reader reader = new StringReader(formula.form);
		try
		{
			return FormulaParser.parse(reader);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("Formula: " + formula.form);
			throw e;
		}
	}

	/**
	 * Parse
	 *
	 * @param reader reader
	 * @return parses
	 * @throws IllegalArgumentException illegal
	 * @throws ParseException parse
	 * @throws IOException io
	 */
	public static Map<String, Arg> parse(final Reader reader) throws IllegalArgumentException, ParseException, IOException
	{
		// reader
		if (reader == null)
			throw new IllegalArgumentException("Null reader");

		final Map<String, Arg> map = new HashMap<>();
		final StringBuilder sb = new StringBuilder(40);

		// tokenizer
		final StreamTokenizer_s tokenizer = new StreamTokenizer_s(reader);
		FormulaParser.setupStreamTokenizer(tokenizer);

		// parser state
		int parenLevel = 0;
		boolean inRule = false;
		int argumentNum = -1;
		boolean inAntecedent = false;
		boolean inConsequent = false;

		// parser line state
		int startLine = 0;
		boolean isEOL = false;

		do
		{
			// get next token
			final int lastTokenType = tokenizer.ttype;
			tokenizer.nextToken();

			// E N D O F L I N E

			// Check the situation when multiple KIF statements read as one
			// This relies on extra blank line to separate KIF statements
			if (tokenizer.ttype == StreamTokenizer.TT_EOL)
			{
				if (isEOL)
				{
					// two line separators in a row, shows a new KIF statement is to start.
					// check if a new statement has already been generated, otherwise report error
					if (sb.length() > 0)
						throw new ParseException("Parsing error : possible missed closing parenthesis", tokenizer.lineno());
					continue;
				}
				// Found a first end of line character.
				isEOL = true; // Turn on flag, to watch for a second consecutive one.
				continue;
			}
			else if (isEOL)
			{
				isEOL = false; // Turn off isEOL if a non-space token encountered
			}

			// O P E N P A R E N T H E S I S

			if (tokenizer.ttype == 40)
			{
				if (parenLevel == 0)
				{
					startLine = tokenizer.lineno();
				}
				parenLevel++;
				if (inRule && !inAntecedent && !inConsequent)
				{
					inAntecedent = true;
				}
				else
				{
					if (inRule && inAntecedent && parenLevel == 2)
					{
						inAntecedent = false;
						inConsequent = true;
					}
				}
				if (parenLevel != 0 && lastTokenType != 40 && sb.length() > 0)
				{
					sb.append(" ");
				}
				sb.append("(");
			}

			// C L O S E P A R E N T H E S I S

			else if (tokenizer.ttype == 41)
			{
				parenLevel--;
				sb.append(")");

				if (parenLevel == 0)
				{
					// end of the statement

					// create formula
					final com.articulate.sigma.Formula f = com.articulate.sigma.Formula.of(sb.toString());
					f.startLine = startLine;
					f.endLine = tokenizer.lineno();

					// check argument validity
					String validArgs = f.validArgs(null, null);
					if (validArgs.equals(""))
					{
						validArgs = f.badQuantification();
					}
					if (!validArgs.equals(""))
						throw new ParseException("Parsing error in : Invalid number of arguments", startLine);

					// reset state
					inConsequent = false;
					inRule = false;
					argumentNum = -1;
					startLine = tokenizer.lineno() + 1; // start next statement from next line
					sb.delete(0, sb.length());
				}
				else if (parenLevel < 0)
					throw new ParseException("Parsing error : Extra closing parenthesis found.", startLine);
			}

			// Q U O T E

			else if (tokenizer.ttype == 34)
			{
				tokenizer.sval = tokenizer.sval.replace("\"", "\\\"");
				if (lastTokenType != 40)
				{
					sb.append(" ");
				}
				sb.append("\"");
				sb.append(tokenizer.sval);
				sb.append("\"");
				if (parenLevel < 2)
				{
					argumentNum = argumentNum + 1;
				}
			}

			// N U M B E R

			else if (tokenizer.ttype == StreamTokenizer.TT_NUMBER || tokenizer.sval != null && Character.isDigit(tokenizer.sval.charAt(0)))
			{
				if (lastTokenType != 40)
				{
					sb.append(" ");
				}
				if (tokenizer.nval == 0)
				{
					sb.append(tokenizer.sval);
				}
				else
				{
					sb.append(tokenizer.nval);
				}
				if (parenLevel < 2)
				{
					argumentNum = argumentNum + 1; // RAP - added on 11/27/04
				}
			}

			// W O R D

			else if (tokenizer.ttype == StreamTokenizer.TT_WORD)
			{
				assert tokenizer.sval != null;
				if ((tokenizer.sval.compareTo("=>") == 0 || tokenizer.sval.compareTo("<=>") == 0) && parenLevel == 1)
				{
					// RAP - added parenLevel clause on 11/27/04 to
					// prevent implications embedded in statements from being rules
					inRule = true;
				}
				if (parenLevel < 2)
				{
					argumentNum = argumentNum + 1;
				}
				if (lastTokenType != 40)
				{
					sb.append(" ");
				}
				sb.append(tokenizer.sval);
				if (sb.length() > 64000)
					throw new ParseException("Parsing error : Sentence over 64000 characters", startLine);

				// Build the terms list and create special keys
				// Variables are not terms
				if (tokenizer.sval.charAt(0) != '?' && tokenizer.sval.charAt(0) != '@')
				{
					// term
					final String term = tokenizer.sval;

					// term's relation to formula
					final Arg tokenRelation = new Arg(inAntecedent, inConsequent, argumentNum, parenLevel);
					tokenRelation.check();

					map.put(term, tokenRelation);
				}
			}

			// E O F
			else if (tokenizer.ttype != StreamTokenizer.TT_EOF)
				throw new ParseException("Parsing error : Illegal character", startLine);
		}
		while (tokenizer.ttype != StreamTokenizer.TT_EOF);

		if (sb.length() > 0)
			throw new ParseException("Parsing error : Missed closing parenthesis", startLine);

		return map;
	}

	/**
	 * Sets up the StreamTokenizer_s so that it parses SUO-KIF. = &lt; &gt; are treated as word characters, as are normal alphanumerics. ; is the line comment
	 * character and " is the quote character.
	 *
	 * @param st stream tokenizer
	 */
	public static void setupStreamTokenizer(final StreamTokenizer_s st)
	{
		st.whitespaceChars(0, 32);
		st.ordinaryChars(33, 44); // !"#$%&'()*+,
		st.wordChars(45, 46); // -.
		st.ordinaryChar(47); // /
		st.wordChars(48, 57); // 0-9
		st.ordinaryChars(58, 59); // :;
		st.wordChars(60, 64); // <=>?@
		st.wordChars(65, 90); // A-Z
		st.ordinaryChars(91, 94); // [\]^
		st.wordChars(95, 95); // _
		st.ordinaryChar(96); // `
		st.wordChars(97, 122); // a-z
		st.ordinaryChars(123, 255); // {|}~
		st.quoteChar('"');
		st.commentChar(';');
		st.eolIsSignificant(true);
	}

	public static Map<String, Arg> parseArg(final Formula formula0)
	{
		final Map<String, Arg> map = new HashMap<>();
		final IterableFormula f = new IterableFormula(formula0.formula.form);
		for (int i = 0; !f.empty(); i++)
		{
			final String arg = f.car();
			if (arg != null && !arg.isEmpty())
			{
				map.put(arg, new Arg(false, false, i, 1));
			}
			f.pop();
		}
		return map;
	}
}
