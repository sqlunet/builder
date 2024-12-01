package org.sqlbuilder.fn.collectors

import edu.berkeley.icsi.framenet.CoreType
import edu.berkeley.icsi.framenet.LabelType
import edu.berkeley.icsi.framenet.POSType
import org.apache.xmlbeans.StringEnumAbstractBase
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import org.sqlbuilder.fn.objects.Values

class FnEnumCollector : Processor("preset") {

    override fun run() {
        traceHeader("preset framenet tables", "poses coretypes labelitypes")
        makePoses()
        makeCoreTypes()
        makeLabelITypes()
        traceTailer(3)
    }

    private fun makePoses() {
        poses.withIndex()
            .forEach { (i, pos) ->
                Values.Pos.make(pos, i + 1)
            }
    }

    private fun makeCoreTypes() {
        coreTypes.withIndex()
            .forEach { (i, coretype) ->
                Values.CoreType.make(coretype, i + 1)
            }
    }

    private fun makeLabelITypes() {
        labelITypes.withIndex()
            .forEach { (i, labelitype) ->
                Values.LabelIType.make(labelitype, i + 1)
            }
    }

    companion object {

        private fun getValues(types: StringEnumAbstractBase.Table): Array<String> {
            return Array(types.lastInt() + 1) {
                val e = types.forInt(it)
                e.toString()
            }
        }

        val poses: Array<String>
            get() = getValues(POSType.Enum.table)

        @JvmStatic
        val coreTypes: Array<String>
            get() = getValues(CoreType.Enum.table)

        @JvmStatic
        val labelITypes: Array<String>
            get() = getValues(LabelType.Itype.Enum.table)
    }
}