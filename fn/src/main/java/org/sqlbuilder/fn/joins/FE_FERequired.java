package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

/*
fes_required.table=fnfes_required
fes_required.create=CREATE TABLE IF NOT EXISTS %Fn_fes_required.table% ( feid INTEGER NOT NULL,fe2id INTEGER NOT NULL,PRIMARY KEY (feid,fe2id) );
fes_required.fk1=ALTER TABLE %Fn_fes_required.table% ADD CONSTRAINT fk_%Fn_fes_required.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
fes_required.fk2=ALTER TABLE %Fn_fes_required.table% ADD CONSTRAINT fk_%Fn_fes_required.table%_fe2id FOREIGN KEY (fe2id) REFERENCES %Fn_fes.table% (feid);
fes_required.no-fk1=ALTER TABLE %Fn_fes_required.table% DROP CONSTRAINT fk_%Fn_fes_required.table%_fe2id CASCADE;
fes_required.no-fk2=ALTER TABLE %Fn_fes_required.table% DROP CONSTRAINT fk_%Fn_fes_required.table%_feid CASCADE;
fes_required.insert=INSERT INTO %Fn_fes_required.table% (feid,fe2id) VALUES(?,?);
 */
public class FE_FERequired extends Pair<Integer, Integer> implements Insertable<FE_FERequired>
{
	public static final Set<FE_FERequired> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FE_FERequired make(final int fe, final InternalFrameRelationFEType fe2)
	{
		var ff = new FE_FERequired(fe, fe2.getID());
		SET.add(ff);
		return ff;
	}

	private FE_FERequired(final int feid, final int feid2)
	{
		super(feid, feid2);
	}

	// O R D E R

	public static final Comparator<FE_FERequired> COMPARATOR = Comparator.comparing(FE_FERequired::getFirst).thenComparing(FE_FERequired::getSecond);

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
		return String.format("[FE-FEreq feid=%s feid2=%s]", this.first, this.second);
	}
}
