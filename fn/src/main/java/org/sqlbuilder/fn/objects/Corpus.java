package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
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

	public final CorpDocType corpus;

	public final Integer luid;

	public Corpus(final CorpDocType corpus, final Integer luid)
	{
		this.corpus = corpus;
		this.luid = luid;
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s',%s", //
				corpus.getID(), //
				corpus.getName(), //
				Utils.escape(corpus.getDescription()), //
				Utils.nullableInt(luid));
	}

	@Override
	public String toString()
	{
		return String.format("[CORPUS id=%s name=%s]", corpus.getID(), corpus.getName());
	}
}
