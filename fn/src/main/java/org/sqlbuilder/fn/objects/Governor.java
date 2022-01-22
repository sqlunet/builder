package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.GovernorType;

/*
governors.table=fngovernors
governors.create=CREATE TABLE IF NOT EXISTS %Fn_governors.table% ( governorid INTEGER NOT NULL,fnwordid INTEGER DEFAULT NULL,governortype VARCHAR(5),PRIMARY KEY (governorid) );
governors.fk1=ALTER TABLE %Fn_governors.table% ADD CONSTRAINT fk_%Fn_governors.table%_fnwordid FOREIGN KEY (fnwordid) REFERENCES %Fn_words.table% (fnwordid);
governors.no-fk1=ALTER TABLE %Fn_governors.table% DROP CONSTRAINT fk_%Fn_governors.table%_fnwordid CASCADE;
governors.insert=INSERT INTO %Fn_governors.table% (governorid,fnwordid,governortype) VALUES(?,?,?);
 */
public class Governor implements Insertable<Governor>
{
	public static final Set<Governor> SET = new HashSet<>();

	public final GovernorType governor;

	public final Word word;

	public Governor(final GovernorType governor)
	{
		this.governor = governor;
		this.word = new Word(governor.getLemma());
		SET.add(this);
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s','%s',%s", //
				Utils.escape(governor.getType()), //
				Utils.escape(word.word), //
				word.getId());
	}

	@Override
	public String toString()
	{
		return String.format("[GOV type=%s word=%s]", governor.getType(), word);
	}
}
