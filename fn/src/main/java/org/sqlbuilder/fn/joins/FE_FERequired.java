package org.sqlbuilder.fn.joins;

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
public class FE_FERequired extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FE_FERequired> SET = new HashSet<>();

	public FE_FERequired(final long fe, final InternalFrameRelationFEType fe2)
	{
		super(fe, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-FEreq feid=%s fe2=%s]", this.first, this.second);
	}
}
