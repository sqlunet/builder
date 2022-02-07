package org.sqlbuilder.sn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder2.legacy.Triplet;

import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;

public class ResolvingCollocation extends Collocation implements Insertable
{
	public int word1id;

	public int word2id;

	public int synset1id;

	public int synset2id;

	// C O N S T R U C T O R
	public ResolvingCollocation(final Collocation c)
	{
		super(c.offset1, c.pos1, c.word1, c.offset2, c.pos2, c.word2);
	}

	public static ResolvingCollocation parse(String line) throws ParseException
	{
		try
		{
			Collocation c = Collocation.parse(line);
			return new ResolvingCollocation(c);
		}
		catch (Exception e)
		{
			throw new ParseException(e.getMessage());
		}
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		//return String.format("('%s',%d,'%c',%d,%d, '%s',%d,'%c',%d,%d)", Utils.escape(word1), word1id, pos1, offset1, synset1id, Utils.escape(word2), word2id, pos2, offset2, synset2id);
		return String.format("'%s',%d,%d, '%s',%d,%d", Utils.escape(sensekey1), word1id, synset1id, Utils.escape(sensekey2), word2id, synset2id);
	}

	// R E S O L V E

	public boolean resolve(final Function<Triplet<String, Character, Long>, String> skResolver, final Function<String, SimpleEntry<Integer, Integer>> senseResolver)
	{
		boolean resolved = resolve(skResolver);
		if (resolved)
		{
			assert sensekey1 != null;
			boolean resolved1;
			SimpleEntry<Integer, Integer> id1 = senseResolver.apply(sensekey1);
			resolved1 = id1 != null;
			if (!resolved1)
			{
				System.err.printf("[RS] %s%n", sensekey1);
			}
			word1id = resolved1 ? id1.getKey() : -1;
			synset1id = resolved1 ? id1.getValue() : -1;

			assert sensekey2 != null;
			boolean resolved2;
			SimpleEntry<Integer, Integer> id2 = senseResolver.apply(sensekey2);
			resolved2 = id2 != null;
			if (!resolved2)
			{
				System.err.printf("[RS] %s%n", sensekey2);
			}
			word2id = resolved2 ? id2.getKey() : -1;
			synset2id = resolved2 ? id2.getValue() : -1;

			return resolved1 && resolved2;
		}

		return false;
	}
}
