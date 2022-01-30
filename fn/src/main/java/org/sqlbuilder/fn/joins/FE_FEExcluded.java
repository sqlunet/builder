package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

/*
fes_excluded.table=fnfes_excluded
fes_excluded.create=CREATE TABLE IF NOT EXISTS %Fn_fes_excluded.table% ( feid INTEGER NOT NULL,fe2id INTEGER NOT NULL,PRIMARY KEY (feid,fe2id) );
fes_excluded.fk1=ALTER TABLE %Fn_fes_excluded.table% ADD CONSTRAINT fk_%Fn_fes_excluded.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
fes_excluded.fk2=ALTER TABLE %Fn_fes_excluded.table% ADD CONSTRAINT fk_%Fn_fes_excluded.table%_fe2id FOREIGN KEY (fe2id) REFERENCES %Fn_fes.table% (feid);
fes_excluded.no-fk1=ALTER TABLE %Fn_fes_excluded.table% DROP CONSTRAINT fk_%Fn_fes_excluded.table%_fe2id CASCADE;
fes_excluded.no-fk2=ALTER TABLE %Fn_fes_excluded.table% DROP CONSTRAINT fk_%Fn_fes_excluded.table%_feid CASCADE;
fes_excluded.insert=INSERT INTO %Fn_fes_excluded.table% (feid,fe2id) VALUES(?,?);
 */
public class FE_FEExcluded extends Pair<Integer, Integer> implements Insertable<FE_FEExcluded>
{
	public static final Set<FE_FEExcluded> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FE_FEExcluded make(final int feid, final InternalFrameRelationFEType fe2)
	{
		var ff = new FE_FEExcluded(feid, fe2.getID());
		SET.add(ff);
		return ff;
	}

	private FE_FEExcluded(final int feid, final int feid2)
	{
		super(feid, feid2);
	}

	// O R D E R

	public static final Comparator<FE_FEExcluded> COMPARATOR = Comparator.comparing(FE_FEExcluded::getFirst).thenComparing(FE_FEExcluded::getSecond);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d", first, second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FE-FEexcl feid=%s feid2=%s]", first, second);
	}
}
