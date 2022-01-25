package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.*;

import edu.berkeley.icsi.framenet.AnnoSetType;
import edu.berkeley.icsi.framenet.FEGroupRealizationType;

import static java.util.stream.Collectors.joining;

/*
patterns.table=fnpatterns
patterns.create=CREATE TABLE IF NOT EXISTS %Fn_patterns.table% ( patternid INTEGER NOT NULL,fegrid INTEGER,total INTEGER,PRIMARY KEY (patternid) );
patterns.fk1=ALTER TABLE %Fn_patterns.table% ADD CONSTRAINT fk_%Fn_patterns.table%_fegrid FOREIGN KEY (fegrid) REFERENCES %Fn_fegrouprealizations.table% (fegrid);
patterns.no-fk1=ALTER TABLE %Fn_patterns.table% DROP CONSTRAINT fk_%Fn_patterns.table%_fegrid CASCADE;
patterns.insert=INSERT INTO %Fn_patterns.table% (patternid,fegrid,total) VALUES(?,?,?);
 */
public class Pattern implements HasId, Insertable<Pattern>
{
	public static final Set<Pattern> SET = new HashSet<>();

	public static Map<Pattern, Integer> MAP;

	public final int[] annosetIDs;

	public final int total;

	public final FEGroupRealization fegr;

	public static Pattern make(final FEGroupRealizationType.Pattern pattern, final FEGroupRealization fegr)
	{
		var p = new Pattern(pattern, fegr);
		SET.add(p);
		return p;
	}

	private Pattern(final FEGroupRealizationType.Pattern pattern, final FEGroupRealization fegr)
	{
		this.annosetIDs = Arrays.stream(pattern.getAnnoSetArray()).mapToInt(AnnoSetType::getID).toArray();
		this.fegr = fegr;
		this.total = pattern.getTotal();
	}

	// A C C E S S

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
		Pattern pattern = (Pattern) o;
		return Arrays.equals(annosetIDs, pattern.annosetIDs) && fegr.equals(pattern.fegr);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(fegr);
		result = 31 * result + Arrays.hashCode(annosetIDs);
		return result;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,%d,%s", getId(), //
				total, //
				fegr.getId());
	}

	@Override
	public String comment()
	{
		return String.format("{%s},{%s}}", Arrays.stream(annosetIDs).mapToObj(Integer::toString).collect(joining()), this.fegr.getFENames());
	}

	@Override
	public String toString()
	{
		return String.format("[GPAT pattern=%s fegr=%s]", Arrays.stream(annosetIDs).mapToObj(Integer::toString).collect(joining()), this.fegr.getFENames());
	}
}
