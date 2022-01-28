package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.types.Cxns;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;
import edu.berkeley.icsi.framenet.AnnotationSetType;

/*
FROM FULL TEXT:
<fullTextAnnotation>
    <header>
        <corpus description="American National Corpus Texts" name="ANC" ID="195">
            <document description="Goodwill fund-raising letter" name="110CYL067" ID="23791"/>
        </corpus>
    </header>
    <sentence corpID="195" docID="23791" sentNo="1" paragNo="1" aPos="0" ID="4106338">
        <text>December 1998</text>
        <annotationSet cDate="11/17/2008 01:54:44 PST Mon" status="UNANN" ID="6556391">
            <layer rank="1" name="PENN">
                <label end="7" start="0" name="NP"/>
                <label end="12" start="9" name="cd"/>
            </layer>
            <layer rank="1" name="NER">
                <label end="12" start="0" name="date"/>
            </layer>
            <layer rank="1" name="WSL"/>
        </annotationSet>
    </sentence>
</fullTextAnnotation>
*/
/*
FROM LEXUNIT
<lexUnit POS="V" name="cause.v" ID="2" frame="Causation" frameID="5"">
    <subCorpus name="V-429-s20-rcoll-change">
        <sentence sentNo="0" aPos="2990199" ID="651962">
            <text>What caused you to change your mind ? </text>
            <annotationSet cDate="01/07/2003 09:27:03 PST Tue" status="UNANN" ID="784391">
                <layer rank="1" name="BNC">
                    <label end="3" start="0" name="DTQ"/>
                    <label end="10" start="5" name="VVD"/>
                    <label end="14" start="12" name="PNP"/>
                    <label end="17" start="16" name="TO0"/>
                    <label end="24" start="19" name="VVI"/>
                    <label end="29" start="26" name="DPS"/>
                    <label end="34" start="31" name="NN1"/>
                    <label end="36" start="36" name="PUN"/>
                </layer>
                <layer rank="1" name="NER"/>
                <layer rank="1" name="WSL"/>
            </annotationSet>
        </sentence>
   </subcorpus>
</lexUnit>
*/
/*
annosets.table=fnannosets
annosets.create=CREATE TABLE IF NOT EXISTS %Fn_annosets.table% ( annosetid INTEGER NOT NULL,sentenceid INTEGER NOT NULL,frameid INTEGER DEFAULT NULL,luid INTEGER DEFAULT NULL,cxnid INTEGER DEFAULT NULL,statusid INTEGER DEFAULT NULL,cdate VARCHAR(27),noccurs INTEGER DEFAULT 1,PRIMARY KEY (annosetid) );
annosets.altcreate1=ALTER TABLE %Fn_annosets.table% ADD COLUMN cxn VARCHAR(32) AFTER cxnid;
annosets.altcreate2=ALTER TABLE %Fn_annosets.table% ADD COLUMN status VARCHAR(32) AFTER statusid;
annosets.index1=CREATE INDEX IF NOT EXISTS k_%Fn_annosets.table%_frameid ON %Fn_annosets.table% (frameid);
annosets.no-index1=DROP INDEX IF EXISTS k_%Fn_annosets.table%_frameid;
annosets.index2=CREATE INDEX IF NOT EXISTS k_%Fn_annosets.table%_luid ON %Fn_annosets.table% (luid);
annosets.no-index2=DROP INDEX IF EXISTS k_%Fn_annosets.table%_luid;
annosets.fk1=ALTER TABLE %Fn_annosets.table% ADD CONSTRAINT fk_%Fn_annosets.table%_sentenceid FOREIGN KEY (sentenceid) REFERENCES %Fn_sentences.table% (sentenceid);
annosets.fk2=ALTER TABLE %Fn_annosets.table% ADD CONSTRAINT fk_%Fn_annosets.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
annosets.fk3=ALTER TABLE %Fn_annosets.table% ADD CONSTRAINT fk_%Fn_annosets.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
annosets.fk4=ALTER TABLE %Fn_annosets.table% ADD CONSTRAINT fk_%Fn_annosets.table%_cxnid FOREIGN KEY (cxnid) REFERENCES %Fn_cxns.table% (cxnid);
annosets.no-fk1=ALTER TABLE %Fn_annosets.table% DROP CONSTRAINT fk_%Fn_annosets.table%_sentenceid CASCADE;
annosets.no-fk2=ALTER TABLE %Fn_annosets.table% DROP CONSTRAINT fk_%Fn_annosets.table%_frameid CASCADE;
annosets.no-fk3=ALTER TABLE %Fn_annosets.table% DROP CONSTRAINT fk_%Fn_annosets.table%_luid CASCADE;
annosets.no-fk4=ALTER TABLE %Fn_annosets.table% DROP CONSTRAINT fk_%Fn_annosets.table%_cxnid CASCADE;
annosets.insert=INSERT INTO %Fn_annosets.table% (annosetid,sentenceid,frameid,luid,cxnid,cxn,status,cdate) VALUES(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE noccurs=noccurs+1;
*/

public class AnnotationSet implements HasID, Insertable<AnnotationSet>
{
	public static final Set<AnnotationSet> SET = new HashSet<>();

	public final int annosetid;

	public final int sentenceid;

	public final Integer luid;

	public final Integer frameid;

	public final Integer cxnid;

	public final String cxnName;

	public static AnnotationSet make(final AnnotationSetType annoset, final int sentenceid)
	{
		var a = new AnnotationSet(annoset, sentenceid);

		final boolean isNew = SET.add(a);
		if (!isNew)
		{
			throw new RuntimeException(a.toString());
		}
		return a;
	}

	public static AnnotationSet make(final AnnotationSetType annoset, final int sentenceid, final Integer luid, final Integer frameid)
	{
		var a = new AnnotationSet(annoset, sentenceid, luid, frameid);

		if (a.cxnid != null && a.cxnName != null)
		{
			Cxns.make(a.cxnid, a.cxnName);
		}
		final boolean isNew = SET.add(a);
		if (!isNew)
		{
			throw new RuntimeException(a.toString());
		}
		return a;
	}

	private AnnotationSet(final AnnotationSetType annoset, final int sentenceid)
	{
		this(annoset, sentenceid, null, null);
	}

	private AnnotationSet(final AnnotationSetType annoset, final int sentenceid, final Integer luid, final Integer frameid)
	{
		this.annosetid = annoset.getID();
		this.sentenceid = sentenceid;
		this.luid = luid;
		this.frameid = frameid;
		this.cxnid = annoset.getCxnID();
		this.cxnName = annoset.getCxnName();
	}

	// A C C E S S

	public int getID()
	{
		return annosetid;
	}

	public long getLuId()
	{
		return luid;
	}

	public long getFrameId()
	{
		return frameid;
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
		AnnotationSet that = (AnnotationSet) o;
		return annosetid == that.annosetid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(annosetid);
	}

	// O R D E R

	public static final Comparator<AnnotationSet> COMPARATOR = Comparator.comparing(AnnotationSet::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d,%s,%s,%s", //
				annosetid, //
				sentenceid, //
				Utils.nullableInt(luid), //
				Utils.nullableInt(frameid), //
				Utils.zeroableInt(cxnid) //
		);
		// String(7, this.annoset.getStatus());
		// String(8, this.annoset.getCDate());
	}

	@Override
	public String comment()
	{
		return cxnName == null ? null : String.format("%s", cxnName);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[AS id=%s luid=%s frameid=%s]", annosetid, luid, frameid);
	}
}
