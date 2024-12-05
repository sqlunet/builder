package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.AnnotationSetType
import org.sqlbuilder.common.AlreadyFoundException
import org.sqlbuilder.common.HasID
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.fn.types.Cxns.Companion.make
import java.util.*

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
class AnnotationSet private constructor(
    annoset: AnnotationSetType,
    val sentenceid: Int,
    val luid: Int?,
    val frameid: Int?,
) : HasID, Insertable {

    val iD: Int = annoset.getID()

    val cxnid: Int? = annoset.getCxnID()

    val cxnName: String? = annoset.getCxnName()

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as AnnotationSet
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,$sentenceid,${nullableInt(luid)},${nullableInt(frameid)},${Utils.zeroableInt(cxnid ?: 0)}"
    }

    override fun comment(): String? {
        return if (cxnName == null) null else "cxns=$cxnName"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[AS id=$iD luid=$luid frameid=$frameid]"
    }

    companion object {

        val COMPARATOR: Comparator<AnnotationSet> = Comparator
            .comparing<AnnotationSet, Int> { it.iD }

        val SET = HashSet<AnnotationSet>()

        fun make(annoset: AnnotationSetType, sentenceid: Int): AnnotationSet {
            val a = AnnotationSet(annoset, sentenceid, null, null)

            if (a.cxnid != null && a.cxnName != null) {
                make(a.cxnid, a.cxnName)
            }
            val isNew: Boolean = SET.add(a)
            if (!isNew) {
                throw AlreadyFoundException(a.toString())
            }
            return a
        }

        fun make(annoset: AnnotationSetType, sentenceid: Int, luid: Int, frameid: Int): AnnotationSet {
            val a = AnnotationSet(annoset, sentenceid, luid, frameid)

            if (a.cxnid != null && a.cxnName != null) {
                make(a.cxnid, a.cxnName)
            }
            val isNew: Boolean = SET.add(a)
            if (!isNew) {
                throw AlreadyFoundException(a.toString())
            }
            return a
        }
    }
}
