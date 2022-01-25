package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.Sentence;
import org.sqlbuilder.fn.objects.SubCorpus;

import java.util.HashSet;
import java.util.Set;

/*
subcorpuses_sentences.table=fnsubcorpuses_sentences
subcorpuses_sentences.create=CREATE TABLE IF NOT EXISTS %Fn_subcorpuses_sentences.table% ( subcorpusid INTEGER NOT NULL,sentenceid INTEGER NOT NULL,PRIMARY KEY (subcorpusid,sentenceid) );
subcorpuses_sentences.fk1=ALTER TABLE %Fn_subcorpuses_sentences.table% ADD CONSTRAINT fk_%Fn_subcorpuses_sentences.table%_subcorpusid FOREIGN KEY (subcorpusid) REFERENCES %Fn_subcorpuses.table% (subcorpusid);
subcorpuses_sentences.fk2=ALTER TABLE %Fn_subcorpuses_sentences.table% ADD CONSTRAINT fk_%Fn_subcorpuses_sentences.table%_sentenceid FOREIGN KEY (sentenceid) REFERENCES %Fn_sentences.table% (sentenceid);
subcorpuses_sentences.no-fk1=ALTER TABLE %Fn_subcorpuses_sentences.table% DROP CONSTRAINT fk_%Fn_subcorpuses_sentences.table%_subcorpusid CASCADE;
subcorpuses_sentences.no-fk2=ALTER TABLE %Fn_subcorpuses_sentences.table% DROP CONSTRAINT fk_%Fn_subcorpuses_sentences.table%_sentenceid CASCADE;
subcorpuses_sentences.insert=INSERT INTO %Fn_subcorpuses_sentences.table% (subcorpusid,sentenceid) VALUES(?,?);
 */
public class SubCorpus_Sentence extends Pair<SubCorpus, Integer> implements Insertable<SubCorpus_Sentence>
{
	public static final Set<SubCorpus_Sentence> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static SubCorpus_Sentence make(final SubCorpus subcorpus, final Sentence sentence)
	{
		var ss = new SubCorpus_Sentence(subcorpus, sentence.getID());
		SET.add(ss);
		return ss;
	}

	private SubCorpus_Sentence(final SubCorpus subcorpus, final Integer sentenceid)
	{
		super(subcorpus, sentenceid);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS-SENT subcorpusid=%s sentenceid=%s]", this.first, this.second);
	}
}
