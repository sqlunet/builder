package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.ListCollector;
import org.sqlbuilder.fn.SetCollector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FEGroupRealization;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.berkeley.icsi.framenet.AnnoSetType;
import edu.berkeley.icsi.framenet.FEGroupRealizationType;

/*
grouppatterns.table=fnpatterns
grouppatterns.create=CREATE TABLE IF NOT EXISTS %Fn_patterns.table% ( patternid INTEGER NOT NULL,fegrid INTEGER,total INTEGER,PRIMARY KEY (patternid) );
grouppatterns.fk1=ALTER TABLE %Fn_patterns.table% ADD CONSTRAINT fk_%Fn_patterns.table%_fegrid FOREIGN KEY (fegrid) REFERENCES %Fn_fegrouprealizations.table% (fegrid);
grouppatterns.no-fk1=ALTER TABLE %Fn_patterns.table% DROP CONSTRAINT fk_%Fn_patterns.table%_fegrid CASCADE;
grouppatterns.insert=INSERT INTO %Fn_patterns.table% (patternid,fegrid,total) VALUES(?,?,?);
 */
public class FEGroupPattern implements HasId, Insertable<FEGroupPattern>
{
	public static final Comparator<FEGroupPattern> COMPARATOR = Comparator.comparing(FEGroupPattern::getFegr, FEGroupRealization.COMPARATOR);

	public static final ListCollector<FEGroupPattern> COLLECTOR = new ListCollector<>();

	public final FEGroupRealization fegr;

	public final int total;

	public static FEGroupPattern make(final FEGroupRealization fegr, final FEGroupRealizationType.Pattern pattern)
	{
		var p = new FEGroupPattern(pattern, fegr);
		COLLECTOR.add(p);
		return p;
	}

	private FEGroupPattern(final FEGroupRealizationType.Pattern pattern, final FEGroupRealization fegr)
	{
		this.fegr = fegr;
		this.total = pattern.getTotal();
	}

	// A C C E S S

	public FEGroupRealization getFegr()
	{
		return fegr;
	}

	@RequiresIdFrom(type = FEGroupPattern.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		FEGroupPattern that = (FEGroupPattern) o;
		return fegr.equals(that.fegr);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(fegr);
	}

	// I N S E R T

	@RequiresIdFrom(type = FEGroupPattern.class)
	@RequiresIdFrom(type = FEGroupRealization.class)
	@Override
	public String dataRow()
	{
		// (patternid),fegrid,total
		return String.format("%s,%d,%s",  //
				getSqlId(), // patternid
				total,
				fegr.getSqlId()); // fegrid
	}

	@Override
	public String comment()
	{
		return String.format("fegr={%s}", fegr.getFENames());
	}

	@Override
	public String toString()
	{
		return String.format("[GPAT fegr={%s}]", fegr.getFENames());
	}
}
