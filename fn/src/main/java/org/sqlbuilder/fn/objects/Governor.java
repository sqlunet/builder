package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.Collector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.Comparator;

import edu.berkeley.icsi.framenet.GovernorType;

/*
governors.table=fngovernors
governors.create=CREATE TABLE IF NOT EXISTS %Fn_governors.table% ( governorid INTEGER NOT NULL,fnwordid INTEGER DEFAULT NULL,governortype VARCHAR(5),PRIMARY KEY (governorid) );
governors.fk1=ALTER TABLE %Fn_governors.table% ADD CONSTRAINT fk_%Fn_governors.table%_fnwordid FOREIGN KEY (fnwordid) REFERENCES %Fn_words.table% (fnwordid);
governors.no-fk1=ALTER TABLE %Fn_governors.table% DROP CONSTRAINT fk_%Fn_governors.table%_fnwordid CASCADE;
governors.insert=INSERT INTO %Fn_governors.table% (governorid,fnwordid,governortype) VALUES(?,?,?);
 */
public class Governor implements HasId, Insertable<Governor>
{
	public static final Comparator<Governor> COMPARATOR = Comparator.comparing(Governor::getWord).thenComparing(Governor::getType);

	public static final Collector<Governor> COLLECTOR = new Collector<>(COMPARATOR);

	private final String type;

	private final Word word;

	public static Governor make(final GovernorType governor)
	{
		var g = new Governor(governor);
		COLLECTOR.add(g);
		return g;
	}

	private Governor(final GovernorType governor)
	{
		this.type = governor.getType();
		this.word = Word.make(governor.getLemma());
	}

	//A C C E S S

	public String getType()
	{
		return type;
	}

	public String getWord()
	{
		return word.getWord();
	}

	@RequiresIdFrom(type = Governor.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// I N S E R T

	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		// governorid,governortype,fnwordid
		return String.format("'%s',%s", //
				Utils.escape(type), //
				word.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("%s", word.getWord());
	}

	@Override
	public String toString()
	{
		return String.format("[GOV type=%s word=%s]", type, word);
	}
}