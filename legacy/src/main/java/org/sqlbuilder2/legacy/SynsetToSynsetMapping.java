package org.sqlbuilder2.legacy;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.ParseException;

import java.io.Serializable;

public class SynsetToSynsetMapping implements Insertable, Serializable
{
	public final long offset1;

	public final long offset2;

	public SynsetToSynsetMapping(long offset1, long offset2)
	{
		this.offset1 = offset1;
		this.offset2 = offset2;
	}

	public long getFrom()
	{
		return offset1;
	}

	public long getTo()
	{
		return offset2;
	}

	//1740 00001740 1 m
	public static SynsetToSynsetMapping parse(String line) throws ParseException
	{
		try
		{
			String[] fields = line.split("\\s");

			long synset1Id = Long.parseLong(fields[0]);
			long synset2Id = Long.parseLong(fields[1]);
			return new SynsetToSynsetMapping(synset1Id, synset2Id);
		}
		catch (Exception e)
		{
			throw new ParseException(e.getMessage());
		}
	}

	public String dataRow()
	{
		return String.format("%d,%d", offset1, offset2);
	}
}
