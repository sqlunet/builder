package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;
/*
corpuses.table=fncorpuses
corpuses.create=CREATE TABLE IF NOT EXISTS %Fn_corpuses.table% ( corpusid INTEGER NOT NULL,corpus VARCHAR(80),corpusdesc VARCHAR(96),luid INTEGER DEFAULT NULL,noccurs INTEGER DEFAULT 1,PRIMARY KEY (corpusid) );
corpuses.fk1=ALTER TABLE %Fn_corpuses.table% ADD CONSTRAINT fk_%Fn_corpuses.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
corpuses.no-fk1=ALTER TABLE %Fn_corpuses.table% DROP CONSTRAINT fk_%Fn_corpuses.table%_luid CASCADE;
corpuses.insert=INSERT INTO %Fn_corpuses.table% (corpusid,corpus,corpusdesc,luid) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE noccurs=noccurs+1;
*/

public class Corpus implements HasID, Insertable<Corpus>
{
	public static final Set<Corpus> SET = new HashSet<>();

	private final int corpusid;

	private final String name;

	private final String description;

	private final Integer luid;

	public static void make(final CorpDocType corpus, final Integer luid)
	{
		var c = new Corpus(corpus, luid);
		final boolean isNew = SET.add(c);
		if (!isNew)
		{
			// Logger.instance.logWarn(FnModule.MODULE_ID, "Corpus", "corpus-duplicate", null, -1, null, c.toString());
		}
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
