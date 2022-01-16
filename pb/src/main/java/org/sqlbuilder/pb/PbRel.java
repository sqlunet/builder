package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;

public class PbRel implements Insertable<PbRel>, Serializable
{
	private final String text;

	private final String f;

	private final PbExample example;

	public PbRel(final PbExample example, final String rel, final String f)
	{
		this.text = PbNormalizer.normalize(rel);
		this.f = f;
		this.example = example;
	}

	public static PbRel make(final PbExample example, final String rel, final String f)
	{
		return new PbRel(example, rel, f);
	}

	public PbExample getExample()
	{
		return this.example;
	}

	public String getText()
	{
		return this.text;
	}

	public String getF()
	{
		return this.f;
	}

	@Override
	public String dataRow()
	{
			// relid,rel,func,exampleid
//			Long(1, this.id);
//			String(2, this.text);
//			Null(3, Types.INTEGER);
//			Long(3, fId);
//			Long(4, this.example.getId());
			return null;
	}

	@Override
	public String toString()
	{
		return String.format("rel %s[%s][%s]", this.text, this.example, this.f);
	}
}
