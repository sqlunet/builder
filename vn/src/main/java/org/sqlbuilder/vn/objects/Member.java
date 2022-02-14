package org.sqlbuilder.vn.objects;

import java.util.ArrayList;
import java.util.List;

public class Member
{
	public final String lemma;

	public final List<Sensekey> senseKeys;

	public final List<Grouping> groupings;

	private final boolean isDefinite;

	// C O N S T R U C T O R

	public static Member make(final String wnword, final String wnSenses, final String wnGrouping)
	{
		boolean isDefiniteFlag = wnword.startsWith("?");
		String word = makeWord(wnword);
		List<Sensekey> senseKeys = makeSensekeys(wnSenses);
		List<Grouping> groupings = makeGroupings(wnGrouping);

		return new Member(word, senseKeys, groupings, isDefiniteFlag);
	}

	public static String makeWord(final String wnword)
	{
		// word
		String word = wnword;
		if (word.startsWith("?"))
		{
			word = word.substring(1);
		}
		return word.replace('_', ' ');
	}

	public static List<Sensekey> makeSensekeys(final String wnSenses)
	{
		// senses
		List<Sensekey> senseKeys = null;
		if (wnSenses != null && !wnSenses.trim().isEmpty())
		{
			String wnSenses2 = wnSenses.trim();
			if (wnSenses2.indexOf('\n') != -1)
			{
				wnSenses2 = wnSenses2.replace("\n", "");
			}
			if (wnSenses2.indexOf('\r') != -1)
			{
				wnSenses2 = wnSenses2.replace("\r", "");
			}
			final String[] senseKeyNames = wnSenses2.split("\\s+");
			for (final String senseKeyName : senseKeyNames)
			{
				// get sensekey
				final Sensekey sensekey = Sensekey.parse(senseKeyName);
				if (senseKeys == null)
				{
					senseKeys = new ArrayList<>();
				}
				senseKeys.add(sensekey);
			}
		}
		return senseKeys;
	}

	public static List<Grouping> makeGroupings(final String groupingAttribute)
	{
		List<Grouping> groupings = null;
		if (groupingAttribute != null && !groupingAttribute.trim().isEmpty())
		{
			final String groupingAttribute2 = groupingAttribute.trim();
			final String[] groupingNames = groupingAttribute2.split("\\s+");
			for (final String groupingName : groupingNames)
			{
				// get sensekey
				final Grouping grouping = Grouping.make(groupingName);
				if (groupings == null)
				{
					groupings = new ArrayList<>();
				}
				groupings.add(grouping);
			}
		}
		return groupings;
	}

	private Member(final String lemma, final List<Sensekey> senseKeys, final List<Grouping> groupings, final boolean definiteFlag)
	{
		this.lemma = lemma;
		this.senseKeys = senseKeys;
		this.groupings = groupings;
		this.isDefinite = definiteFlag;
	}

	// A C C E S S

	public float getQuality()
	{
		return this.isDefinite ? 1.f : .5f;
	}
}
