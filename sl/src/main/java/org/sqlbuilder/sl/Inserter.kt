package org.sqlbuilder.sl

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.sl.foreign.PbRoleSet_VnClass
import org.sqlbuilder.sl.foreign.PbRole_VnRole
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(
    conf: Properties,
) {

    @JvmField
    protected val names: Names = Names("sl")

    @JvmField
    protected var header: String = conf.getProperty("sl_header")

    @JvmField
    protected var outDir: File = File(conf.getProperty("sl_outdir", "sql/data"))

    init {
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    // R E S O L V A B L E

    @Throws(FileNotFoundException::class)
    open fun insert() {
        insertClassAliases()
        insertRoleAliases()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertClassAliases() {
        tracePending("set", "vnalias")
        insert<PbRoleSet_VnClass>(PbRoleSet_VnClass.SET, PbRoleSet_VnClass.COMPARATOR, File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header)
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertRoleAliases() {
        tracePending("set", "vnaliasrole")
        insert<PbRole_VnRole>(PbRole_VnRole.SET, PbRole_VnRole.COMPARATOR, File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header)
        traceDone()
    }
}
