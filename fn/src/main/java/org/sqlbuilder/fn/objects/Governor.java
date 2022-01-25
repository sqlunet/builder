package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	public static final Set<Governor> SET = new HashSet<>();

	public static Map<Governor, Integer> MAP;

	private final String type;

	private final Word word;

	public Governor(final GovernorType governor)
	{
		this.type = governor.getType();
		this.word = Word.make(governor.getLemma());
		SET.add(this);
	}

	@Override
	public Object getId()
	{
		return MAP.get(this);
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s','%s',%s", //
				Utils.escape(type), //
				Utils.escape(word.getWord()), //
				word.getId());
	}

	@Override
	public String comment()
	{
		return Insertable.super.comment();
	}

	@Override
	public String toString()
	{
		return String.format("[GOV type=%s word=%s]", type, word);
	}
}