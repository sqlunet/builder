package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.SetCollector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.Comparator;

import edu.berkeley.icsi.framenet.SubCorpusType;

/*
subcorpuses.table=fnsubcorpuses
subcorpuses.create=CREATE TABLE IF NOT EXISTS %Fn_subcorpuses.table% ( subcorpusid INTEGER NOT NULL,luid INTEGER,subcorpus VARCHAR(80),PRIMARY KEY (subcorpusid) );
subcorpuses.fk1=ALTER TABLE %Fn_subcorpuses.table% ADD CONSTRAINT fk_%Fn_subcorpuses.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
subcorpuses.no-fk1=ALTER TABLE %Fn_subcorpuses.table% DROP CONSTRAINT fk_%Fn_subcorpuses.table%_luid CASCADE;
subcorpuses.insert=INSERT INTO %Fn_subcorpuses.table% (subcorpusid,luid,subcorpus) VALUES(?,?,?);
 */
public class SubCorpus implements HasId, Insertable<SubCorpus>
{
	public static final Comparator<SubCorpus> COMPARATOR = Comparator.comparing(SubCorpus::getName).thenComparing(SubCorpus::getLuid);

	public static final SetCollector<SubCorpus> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	private final int luid;

	public static SubCorpus make(final SubCorpusType subcorpus, final int luid)
	{
		var c = new SubCorpus(subcorpus.getName(), luid);
		COLLECTOR.add(c);
		return c;
	}

	private SubCorpus(final String name, final int luid)
	{
		this.name = name;
		this.luid = luid;
	}

	// I D

	public String getName()
	{
		return name;
	}

	public int getLuid()
	{
		return luid;
	}

	@RequiresIdFrom(type = SubCorpus.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// I N S E R T

	@RequiresIdFrom(type = SubCorpus.class)
	@Override
	public String dataRow()
	{
		return String.format("'%s',%d", //
				Utils.escape(name), //
				luid);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS name=%s]", name);
	}
}
