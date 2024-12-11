package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
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

                                            traceSaving("func")
                                            insert(
                                                Func.COLLECTOR,
                                                Func.COLLECTOR,
                                                File(outDir, names.file("funcs")),
                                                names.table("funcs"),
                                                names.columns("funcs"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("vnroles")
                                            insert(
                                                AliasVnRoleLinks.COLLECTOR,
                                                AliasVnRoleLinks.COLLECTOR,
                                                File(outDir, names.file("vnroles")),
                                                names.table("vnroles"),
                                                names.columns("vnroles"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("fnfes")
                                            insert(
                                                AliasFnFeLinks.COLLECTOR,
                                                AliasFnFeLinks.COLLECTOR,
                                                File(outDir, names.file("fnfes")),
                                                names.table("fnfes"),
                                                names.columns("fnfes"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("argtype")
                                            insert(
                                                ArgType.SET,
                                                ArgType.COMPARATOR,
                                                File(outDir, names.file("argtypes")),
                                                names.table("argtypes"),
                                                names.columns("argtypes"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("roleset")
                                            insert(
                                                RoleSet.COLLECTOR,
                                                RoleSet.COLLECTOR,
                                                File(outDir, names.file("rolesets")), names.table("rolesets"), names.columns("rolesets"), header
                                            )
                                            traceDone()

                                            traceSaving("role")
                                            insert(
                                                Role.COLLECTOR,
                                                Role.COLLECTOR,
                                                File(outDir, names.file("roles")),
                                                names.table("roles"),
                                                names.columns("roles"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("example")
                                            insert(
                                                Example.COLLECTOR,
                                                Example.COLLECTOR,
                                                File(outDir, names.file("examples")),
                                                names.table("examples"),
                                                names.columns("examples"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("arg")
                                            insert(
                                                Arg.COLLECTOR,
                                                Arg.COLLECTOR,
                                                File(outDir, names.file("args")),
                                                names.table("args"),
                                                names.columns("args"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("rel")
                                            insert(
                                                Rel.COLLECTOR,
                                                Rel.COLLECTOR,
                                                File(outDir, names.file("rels")),
                                                names.table("rels"),
                                                names.columns("rels"),
                                                header
                                            )
                                            traceDone()

                                            traceSaving("member")
                                            insert(
                                                Member.SET,
                                                Member.COMPARATOR,
                                                File(outDir, names.file("members")),
                                                names.table("members"), names.columns("members"),
                                                header
                                            )
                                            traceDone()

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
        traceSaving("word")
        insert(
            Word.COLLECTOR,
            Word.COLLECTOR,
            File(outDir, names.file("words")),
            names.table("words"),
            names.columns("words"),
            header
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnFrameAliases() {
        traceSaving("fnframealias")
        insert(
            RoleSetToFn.SET,
            RoleSetToFn.COMPARATOR,
            File(outDir, names.file("pbrolesets_fnframes")),
            names.table("pbrolesets_fnframes"),
            names.columns("pbrolesets_fnframes"),
            header
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnClassAliases() {
        traceSaving("vnclassalias")
        insert(
            RoleSetToVn.SET,
            RoleSetToVn.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnRoleAliases() {
        traceSaving("vnrolealias")
        insert(
            RoleToVn.SET,
            RoleToVn.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnFeAliases() {
        traceSaving("fnfealias")
        insert(
            RoleToFn.SET,
            RoleToFn.COMPARATOR,
            File(outDir, names.file("pbroles_fnfes")),
            names.table("pbroles_fnfes"),
            names.columns("pbroles_fnfes"),
            header
        )
        traceDone()
    }
}
