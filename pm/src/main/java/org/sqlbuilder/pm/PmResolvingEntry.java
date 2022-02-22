package org.sqlbuilder.pm;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pm.objects.PmEntry;

public class PmResolvingEntry extends PmEntry implements Insertable
{
	private Integer wordid;

	private Integer synsetid;

	private Integer vnwordid;

	private Integer vnclassid;

	private Integer vnroletypeid;

	private Integer pbwordid;

	private Integer pbrolesetid;

	private Integer pbroleid;

	private Integer fnwordid;

	private Integer fnluid;

	private Integer fnframeid;

	private Integer fnfetypeid;

	private Integer sumoid;

	@Override
	public String toString()
	{
		return String.format("wordid=%d synsetid=%d vnwordid=%d pbwordid=%d fnwordid=%d vnclassid=%d vnroletypeid=%d pbrolesetid=%d pbnarg=%d fnframeid=%d fnluid=%d fnfetypeid=%d sumoid=%d", //
				wordid, synsetid, vnwordid, pbwordid, fnwordid, vnclassid, vnroletypeid, pbrolesetid, pbroleid, fnframeid, fnluid, fnfetypeid, sumoid);
	}

	// pmid INTEGER UNSIGNED NOT NULL,
	// predicateroleid INTEGER UNSIGNED NOT NULL,
	// lemma VARCHAR(33),
	// wordid INTEGER UNSIGNED NOT NULL,
	// synsetid INTEGER UNSIGNED NOT NULL,
	// vnwordid INTEGER UNSIGNED NULL,
	// vnclassid INTEGER UNSIGNED NULL,
	// vnroletypeid INTEGER UNSIGNED NULL,
	// pbwordid INTEGER UNSIGNED NULL,
	// pbrolesetid INTEGER UNSIGNED NULL,
	// pbnarg SMALLINT UNSIGNED NULL,
	// fnwordid INTEGER UNSIGNED NULL,
	// fnluid INTEGER UNSIGNED NULL,
	// fnframeid INTEGER UNSIGNED NULL,
	// fnfetypeid SMALLINT UNSIGNED NULL,
	// sumoid INTEGER UNSIGNED NULL,
	// wsource INTEGER UNSIGNED,
	// sources INTEGER UNSIGNED

	@Override
	public String dataRow()
	{
		// primary key
		// Long(1, pmid);

		// role id
		// Long(2, role.pmroleid);

		// data
		// String(3, lemma);

		// Long(4, wordid);
		// Long(5, synsetid);

		// Long(6, vnwordid);
		// Integer(7, vnclassid);
		// Integer(8, vnroletypeid);

		// Long(9, pbwordid);
		// Integer(10, pbrolesetid);
		// Integer(11, pbroleid);

		// Long(12, fnwordid);
		// Integer(13, fnluid);
		// Integer(14, fnframeid);
		// Integer(15, fnfetypeid);

		// Long(16, sumoid);

		// Int(17, wsource);

		return null;
	}
}
