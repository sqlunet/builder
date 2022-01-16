package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

public class PbWord implements Insertable<PbWord>
{
	public final String word;

	public PbWord(final String lemma)
	{
		this.word = lemma;
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
