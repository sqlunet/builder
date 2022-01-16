package org.sqlbuilder.vn;

public class VnSensekey implements Comparable<VnSensekey>
{
	private final String sensekey;

	private final boolean isDefinite;

	protected VnSensekey(final String sensekey, final boolean IsDefiniteFlag)
	{
		this.sensekey = sensekey;

		this.isDefinite = IsDefiniteFlag;
	}

	public static VnSensekey parse(final String str0)
	{
		// handle question mark
		String str = str0;
		boolean isDefiniteFlag = true;
		if (str.startsWith("?"))
		{
			isDefiniteFlag = false;
			str = str.substring(1);
		}

		final String senseKey = str + "::";
		return new VnSensekey(senseKey, isDefiniteFlag);
	}

	public float getQuality()
	{
		return this.isDefinite ? 1.f : .5f;
	}

	public String getSensekey()
	{
		return sensekey;
	}

	@Override
	public int compareTo(final VnSensekey that)
	{
		return sensekey.compareTo(that.sensekey);
	}
}
