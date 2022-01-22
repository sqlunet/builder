package org.sqlbuilder.fn.joins;

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
public class FE_FEExcluded extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FE_FEExcluded> SET = new HashSet<>();

	public FE_FEExcluded(final long feid, final InternalFrameRelationFEType fe2)
	{
		super(feid, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-FEexcl feid=%s fe2=%s]", this.first, this.second);
	}
}
