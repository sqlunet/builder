package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.common.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;

public class Corpus implements HasID, Insertable
{
	public static final Set<Corpus> SET = new HashSet<>();

	private final int corpusid;

	private final String name;

	private final String description;

	private final Integer luid;

	@SuppressWarnings("UnusedReturnValue")
	public static Corpus make(final CorpDocType corpus, final Integer luid)
	{
		var c = new Corpus(corpus, luid);
		SET.add(c);
		return c;
	}

	private Corpus(final CorpDocType corpus, final Integer luid)
	{
		this.corpusid = corpus.getID();
		this.name = corpus.getName();
		this.description = corpus.getDescription();
		this.luid = luid;
	}

	// A C C E S S

	public int getID()
	{
		return corpusid;
	}

	public String getName()
	{
		return name;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Corpus that = (Corpus) o;
		return corpusid == that.corpusid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(corpusid);
	}

	// O R D E R

	public static final Comparator<Corpus> COMPARATOR = Comparator.comparing(Corpus::getName).thenComparing(Corpus::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s',%s", //
				corpusid, //
				name, //
				Utils.escape(description), //
				Utils.nullableInt(luid));
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[CORPUS id=%s name=%s]", corpusid, name);
	}
}
