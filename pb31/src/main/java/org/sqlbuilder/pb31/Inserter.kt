package org.sqlbuilder.pb31

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Insert.insertStrings
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.pb31.foreign.RoleSetToFn
import org.sqlbuilder.pb31.foreign.RoleSetToVn
import org.sqlbuilder.pb31.foreign.RoleToVn
import org.sqlbuilder.pb31.foreign.Theta
import org.sqlbuilder.pb31.objects.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(conf: Properties) {

    protected val names: Names = Names("pb31")

    protected var header: String = conf.getProperty("pb31_header")

    protected var outDir: File = File(conf.getProperty("pb_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    open fun insert() {
        Example.ASPECT_COLLECTOR.open().use {
            Example.FORM_COLLECTOR.open().use {
                Example.PERSON_COLLECTOR.open().use {
                    Example.TENSE_COLLECTOR.open().use {
                        Example.VOICE_COLLECTOR.open().use {
                            Func.COLLECTOR.open().use {
                                Theta.COLLECTOR.open().use {
                                    RoleSet.COLLECTOR.open().use {
                                        Role.COLLECTOR.open().use {
                                            Example.COLLECTOR.open().use {
                                                Rel.COLLECTOR.open().use {
                                                    Arg.COLLECTOR.open().use {
                                                        Word.COLLECTOR.open().use {
                                                            traceSaving("aspect")
                                                            insertStrings(
                                                                Example.ASPECT_COLLECTOR,
                                                                Example.ASPECT_COLLECTOR,
                                                                File(outDir, names.file("aspects")),
                                                                names.table("aspects"),
                                                                names.columns("aspects"),
                                                                header
                                                            )
                                                            traceDone()

                                                            traceSaving("form")
                                                            insertStrings(
                                                                Example.FORM_COLLECTOR,
                                                                Example.FORM_COLLECTOR,
                                                                File(outDir, names.file("forms")),
                                                                names.table("forms"),
                                                                names.columns("forms"),
                                                                header
                                                            )
                                                            traceDone()

                                                            traceSaving("person")
                                                            insertStrings(
                                                                Example.PERSON_COLLECTOR,
                                                                Example.PERSON_COLLECTOR,
                                                                File(outDir, names.file("persons")),
                                                                names.table("persons"),
                                                                names.columns("persons"),
                                                                header
                                                            )
                                                            traceDone()

                                                            traceSaving("tense")
                                                            insertStrings(
                                                                Example.TENSE_COLLECTOR,
                                                                Example.TENSE_COLLECTOR,
                                                                File(outDir, names.file("tenses")),
                                                                names.table("tenses"),
                                                                names.columns("tenses"),
                                                                header
                                                            )
                                                            traceDone()

                                                            traceSaving("voice")
                                                            insertStrings(
                                                                Example.VOICE_COLLECTOR,
                                                                Example.VOICE_COLLECTOR,
                                                                File(outDir, names.file("voices")),
                                                                names.table("voices"),
                                                                names.columns("voices"),
                                                                header
                                                            )
                                                            traceDone()

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

                                                            traceSaving("theta")
                                                            insert(
                                                                Theta.COLLECTOR,
                                                                Theta.COLLECTOR,
                                                                File(outDir, names.file("thetas")),
                                                                names.table("thetas"),
                                                                names.columns("thetas"),
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
                                                                File(outDir, names.file("rolesets")),
                                                                names.table("rolesets"),
                                                                names.columns("rolesets"),
                                                                header
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
                                                                names.table("members"),
                                                                names.columns("members"),
                                                                header
                                                            )
                                                            traceDone()

                                                            insertWords()
                                                            insertFnAliases()
                                                            insertVnAliases()
                                                            insertVnRoleAliases()
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
    protected open fun insertFnAliases() {
        traceSaving("fnframe aliases")
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
    protected open fun insertVnAliases() {
        traceSaving("vnclass aliases")
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
        traceSaving("vnrole aliases")
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
}
