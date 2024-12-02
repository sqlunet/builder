package org.sqlbuilder.sl

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.sl.foreign.VnClassAlias
import org.sqlbuilder.sl.foreign.VnRoleAlias
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
        insertVnAliases()
        insertVnRoleAliases()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnAliases() {
        tracePending("set", "vnalias")
        insert<VnClassAlias>(VnClassAlias.SET, VnClassAlias.COMPARATOR, File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header)
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnRoleAliases() {
        tracePending("set", "vnaliasrole")
        insert<VnRoleAlias>(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header)
        traceDone()
    }
}
