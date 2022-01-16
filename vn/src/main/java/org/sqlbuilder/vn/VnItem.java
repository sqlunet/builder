package org.sqlbuilder.vn;

import java.util.ArrayList;
import java.util.List;

public class VnItem
{
	public final String lemma;

	public final List<VnSensekey> senseKeys;

	public final List<VnGrouping> groupings;

	private final boolean isDefinite;

	// C O N S T R U C T

	private VnItem(final String lemma, final List<VnSensekey> senseKeys, final List<VnGrouping> groupings, final boolean definiteFlag)
	{
		this.lemma = lemma;
		this.senseKeys = senseKeys;
		this.groupings = groupings;
		this.isDefinite = definiteFlag;
	}

	// F A C T O R Y

	public static VnItem make(final String lemmaString0, final String wnSenses, final String groupingAttribute)
	{
		String lemmaString = lemmaString0;
		boolean isDefiniteFlag = true;
		if (lemmaString.startsWith("?"))
		{
			isDefiniteFlag = false;
			lemmaString = lemmaString.substring(1);
		}
		final String lemma = lemmaString.replace('_', ' ');

		List<VnSensekey> senseKeys = null;
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
				final VnSensekey sensekey = VnSensekey.parse(senseKeyName);
				if (senseKeys == null)
				{
					senseKeys = new ArrayList<>();
				}
				senseKeys.add(sensekey);
			}
		}

		List<VnGrouping> groupings = null;
		if (groupingAttribute != null && !groupingAttribute.trim().isEmpty())
		{
			final String groupingAttribute2 = groupingAttribute.trim();
			final String[] groupingNames = groupingAttribute2.split("\\s+");
			for (final String groupingName : groupingNames)
			{
				// get sensekey
				final VnGrouping grouping = VnGrouping.parse(groupingName);
				if (groupings == null)
				{
					groupings = new ArrayList<>();
				}
				groupings.add(grouping);
			}
		}

		return new VnItem(lemma, senseKeys, groupings, isDefiniteFlag);
	}

	// A C C E S S

	public float getQuality()
	{
		return this.isDefinite ? 1.f : .5f;
	}
}
