package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;

public class PbWord implements Insertable, Comparable<PbWord>
{
	public final String word;

	public PbWord(final String lemma)
	{
		this.word = lemma;
	}

	@Override
	public int compareTo(final PbWord that)
	{
		return word.compareTo(that.word);
	}

	@Override
	public String dataRow()
	{
		//		Long(1, getId());
		//		Long(2, this.wordid.toLong());
		//		Null(2, Types.INTEGER);
		//		String(3, this.word);
		return null;
	}
}
