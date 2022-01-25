package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.*;

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

	private final int luid;

	private final int frameid;

	private final int total;

	private final String feNames;

	public static FEGroupRealization make(final FEGroupRealizationType fegr, final int luid, final int frameid)
	{
		var r = new FEGroupRealization(fegr, luid, frameid);
		SET.add(r);
		return r;
	}

	private FEGroupRealization(final FEGroupRealizationType fegr, final int luid, final int frameid)
	{
		this.luid = luid;
		this.frameid = frameid;
		this.total = fegr.getTotal();
		this.feNames = Arrays.stream(fegr.getFEArray()).map(FEValenceType::getName).collect(joining(","));
	}

	// A C C E S S

	public int getLuID()
	{
		return luid;
	}

	public int getFrameID()
	{
		return frameid;
	}

	public String getFENames()
	{
		return feNames;
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
		return feNames.equals(that.feNames) && luid == that.luid && frameid == that.frameid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(feNames, luid, frameid);
	}

	// O R D E R

	public static final Comparator<FEGroupRealization> COMPARATOR = Comparator //
			.comparing(FEGroupRealization::getFENames) //
			.thenComparing(FEGroupRealization::getLuID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		// fegrid INTEGER NOT NULL,
		// total INTEGER NOT NULL,
		// luid INTEGER NOT NULL,
		return String.format("%s,%d", //
				total, //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", feNames);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FEGR fes={%s} luid=%s]", feNames, luid);
	}
}
