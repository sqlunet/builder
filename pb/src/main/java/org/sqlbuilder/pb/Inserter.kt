package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress
import org.sqlbuilder.pb.foreign.*
import org.sqlbuilder.pb.objects.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(conf: Properties) {

    protected val names: Names = Names("pb")

    protected var header: String = conf.getProperty("pb_header")

    protected var outDir: File = File(conf.getProperty("pb_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    open fun insert() {
        Func.COLLECTOR.open().use {
            AliasVnRoleLinks.COLLECTOR.open().use {
                AliasFnFeLinks.COLLECTOR.open().use {
                    RoleSet.COLLECTOR.open().use {
                        Role.COLLECTOR.open().use {
                            Example.COLLECTOR.open().use {
                                Rel.COLLECTOR.open().use {
                                    Arg.COLLECTOR.open().use {
                                        Word.COLLECTOR.open().use {

                                            Progress.tracePending("collector", "func")
                                            insert(
                                                Func.COLLECTOR,
                                                Func.COLLECTOR,
                                                File(outDir, names.file("funcs")),
                                                names.table("funcs"),
                                                names.columns("funcs"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "vnroles")
                                            insert(
                                                AliasVnRoleLinks.COLLECTOR,
                                                AliasVnRoleLinks.COLLECTOR,
                                                File(outDir, names.file("vnroles")),
                                                names.table("vnroles"),
                                                names.columns("vnroles"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "fnfes")
                                            insert(
                                                AliasFnFeLinks.COLLECTOR,
                                                AliasFnFeLinks.COLLECTOR,
                                                File(outDir, names.file("fnfes")),
                                                names.table("fnfes"),
                                                names.columns("fnfes"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("set", "argtype")
                                            insert(
                                                ArgType.SET,
                                                ArgType.COMPARATOR,
                                                File(outDir, names.file("argtypes")),
                                                names.table("argtypes"),
                                                names.columns("argtypes"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "roleset")
                                            insert(
                                                RoleSet.COLLECTOR,
                                                RoleSet.COLLECTOR,
                                                File(outDir, names.file("rolesets")), names.table("rolesets"), names.columns("rolesets"), header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "role")
                                            insert(
                                                Role.COLLECTOR,
                                                Role.COLLECTOR,
                                                File(outDir, names.file("roles")),
                                                names.table("roles"),
                                                names.columns("roles"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "example")
                                            insert(
                                                Example.COLLECTOR,
                                                Example.COLLECTOR,
                                                File(outDir, names.file("examples")),
                                                names.table("examples"),
                                                names.columns("examples"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "arg")
                                            insert(
                                                Arg.COLLECTOR,
                                                Arg.COLLECTOR,
                                                File(outDir, names.file("args")),
                                                names.table("args"),
                                                names.columns("args"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("collector", "rel")
                                            insert(
                                                Rel.COLLECTOR,
                                                Rel.COLLECTOR,
                                                File(outDir, names.file("rels")),
                                                names.table("rels"),
                                                names.columns("rels"),
                                                header
                                            )
                                            Progress.traceDone()

                                            Progress.tracePending("set", "member")
                                            insert(
                                                Member.SET,
                                                Member.COMPARATOR,
                                                File(outDir, names.file("members")),
                                                names.table("members"), names.columns("members"),
                                                header
                                            )
                                            Progress.traceDone()

                                            insertWords()
                                            insertFnFrameAliases()
                                            insertVnClassAliases()
                                            insertVnRoleAliases()
                                            insertFnFeAliases()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertWords() {
        Progress.tracePending("collector", "word")
        insert(
            Word.COLLECTOR,
            Word.COLLECTOR,
            File(outDir, names.file("words")),
            names.table("words"),
            names.columns("words"),
            header
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnFrameAliases() {
        Progress.tracePending("set", "fnframealias")
        insert(
            RoleSetToFn.SET,
            RoleSetToFn.COMPARATOR,
            File(outDir, names.file("pbrolesets_fnframes")),
            names.table("pbrolesets_fnframes"),
            names.columns("pbrolesets_fnframes"),
            header
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnClassAliases() {
        Progress.tracePending("set", "vnclassalias")
        insert(
            RoleSetToVn.SET,
            RoleSetToVn.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnrolealias")
        insert(
            RoleToVn.SET,
            RoleToVn.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnFeAliases() {
        Progress.tracePending("set", "fnfealias")
        insert(
            RoleToFn.SET,
            RoleToFn.COMPARATOR,
            File(outDir, names.file("pbroles_fnfes")),
            names.table("pbroles_fnfes"),
            names.columns("pbroles_fnfes"),
            header
        )
        Progress.traceDone()
    }
}
