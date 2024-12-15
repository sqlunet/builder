package org.semantikos.sl

import org.semantikos.common.Insert.insert
import org.semantikos.common.Names
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.sl.foreign.PbRoleSet_VnClass
import org.semantikos.sl.foreign.PbRole_VnRole
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(
    conf: Properties,
) {

    protected val names: Names = Names("sl")

    protected var header: String = conf.getProperty("sl_header")

    protected var outDir: File = File(conf.getProperty("sl_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
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
        traceSaving("vnalias")
        insert(PbRoleSet_VnClass.SET, PbRoleSet_VnClass.COMPARATOR, File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header)
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertRoleAliases() {
        traceSaving("vnaliasrole")
        insert(PbRole_VnRole.SET, PbRole_VnRole.COMPARATOR, File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header)
        traceDone()
    }
}
