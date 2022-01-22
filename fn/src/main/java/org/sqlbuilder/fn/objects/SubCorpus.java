package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SubCorpusType;

/*
subcorpuses.table=fnsubcorpuses
subcorpuses.create=CREATE TABLE IF NOT EXISTS %Fn_subcorpuses.table% ( subcorpusid INTEGER NOT NULL,luid INTEGER,subcorpus VARCHAR(80),PRIMARY KEY (subcorpusid) );
subcorpuses.fk1=ALTER TABLE %Fn_subcorpuses.table% ADD CONSTRAINT fk_%Fn_subcorpuses.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
subcorpuses.no-fk1=ALTER TABLE %Fn_subcorpuses.table% DROP CONSTRAINT fk_%Fn_subcorpuses.table%_luid CASCADE;
subcorpuses.insert=INSERT INTO %Fn_subcorpuses.table% (subcorpusid,luid,subcorpus) VALUES(?,?,?);
 */
public class SubCorpus implements Insertable<SubCorpus>
{
	public static final Set<SubCorpus> SET = new HashSet<>();

	private final SubCorpusType subcorpus;

	private final int luid;

	public SubCorpus(final SubCorpusType subcorpus, final int luid)
	{
		this.subcorpus = subcorpus;
		this.luid = luid;
		SET.add(this);
	}

	@Override
	public String dataRow()
	{
		return String.format("NULL,'%s',%d",
				// getId(),
				subcorpus.getName(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS name=%s]", subcorpus.getName());
	}
}
