package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.*;
import java.util.stream.Collectors;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;
import edu.berkeley.icsi.framenet.FEValenceType;

import static java.util.stream.Collectors.joining;

/*
fegrouprealizations.table=fnfegrouprealizations
fegrouprealizations.create=CREATE TABLE IF NOT EXISTS %Fn_fegrouprealizations.table% ( fegrid INTEGER NOT NULL,luid INTEGER,total INTEGER,PRIMARY KEY (fegrid) );
fegrouprealizations.fk1=ALTER TABLE %Fn_fegrouprealizations.table% ADD CONSTRAINT fk_%Fn_fegrouprealizations.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
fegrouprealizations.no-fk1=ALTER TABLE %Fn_fegrouprealizations.table% DROP CONSTRAINT fk_%Fn_fegrouprealizations.table%_luid CASCADE;
fegrouprealizations.insert=INSERT INTO %Fn_fegrouprealizations.table% (fegrid,luid,total) VALUES(?,?,?);
 */
public class FEGroupRealization implements HasId, Insertable<FEGroupRealization>
{
	public static final Set<FEGroupRealization> SET = new HashSet<>();

	public static Map<FEGroupRealization, Integer> MAP;

	final FEGroupRealizationType fegr;

	final int luid;

	public FEGroupRealization(final FEGroupRealizationType fegr, final int luid)
	{
		this.fegr = fegr;
		this.luid = luid;
		SET.add(this);
	}

	// A C C E S S

	public String getFENames()
	{
		return Arrays.stream(fegr.getFEArray()).map(FEValenceType::getName).collect(joining(","));
	}

	public int getLuid()
	{
		return luid;
	}

	@Override
	public Object getId()
	{
		Integer id = MAP.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
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
		FEGroupRealization that = (FEGroupRealization) o;
		return luid == that.luid && fegr.equals(that.fegr);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(fegr, luid);
	}

	// O R D E R

	public static final Comparator<FEGroupRealization> COMPARATOR = Comparator.comparing(FEGroupRealization::getLuid).thenComparing(FEGroupRealization::getFENames);

	// I N S E R T

	@Override
	public String dataRow()
	{
		// fegrid INTEGER NOT NULL,
		// total INTEGER,
		// luid INTEGER,
		return String.format("%s,%d", //
				fegr.getTotal(), //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", //
				Arrays.stream(fegr.getFEArray()).map(FEValenceType::getName).collect(joining(",")));
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FEGR fegr=%s luid=%s]", fegr, luid);
	}
}
