package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType;

/*
semtypes.table=fnsemtypes
semtypes.create=CREATE TABLE IF NOT EXISTS %Fn_semtypes.table% ( semtypeid INTEGER NOT NULL,semtype VARCHAR(32),semtypeabbrev VARCHAR(24),semtypedefinition TEXT,PRIMARY KEY (semtypeid) );
semtypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_semtypes.table%_semtype ON %Fn_semtypes.table% (semtype);
semtypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_semtypes.table%_semtype;
semtypes.insert=INSERT INTO %Fn_semtypes.table% (semtypeid,semtype,semtypeabbrev,semtypedefinition) VALUES(?,?,?,?);
 */
public class SemType implements HasID, Insertable<SemType>
{
	public static final Set<SemType> SET = new HashSet<>();

	private final int semtypeid;

	private final String name;

	private final String abbrev;

	private final String definition;

	public static SemType make(final SemTypeType type)
	{
		var t = new SemType(type);
		SET.add(t);
		return t;
	}

	private SemType(final SemTypeType type)
	{
		this.semtypeid = type.getID();
		this.name = type.getName();
		this.abbrev = type.getAbbrev();
		this.definition = type.getDefinition();
	}

	// A C C E S S

	public int getID()
	{
		return semtypeid;
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
		SemType that = (SemType) o;
		return semtypeid == that.semtypeid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(semtypeid);
	}

	// O R D E R

	public static final Comparator<SemType> COMPARATOR = Comparator.comparing(SemType::getName).thenComparing(SemType::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s','%s'", //
				semtypeid, //
				name, //
				abbrev, //
				Utils.escape(definition));
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SEMTYPE semtypeid=%s name=%s]", this.semtypeid, this.name);
	}
}
