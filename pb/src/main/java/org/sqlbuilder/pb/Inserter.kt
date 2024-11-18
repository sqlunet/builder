package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress
import org.sqlbuilder.pb.foreign.*
import org.sqlbuilder.pb.objects.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(conf: Properties) {

    @JvmField
    protected val names: Names = Names("pb")

    @JvmField
    protected var header: String = conf.getProperty("pb_header")

    @JvmField
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
                                VnLinks.COLLECTOR.open().use {
                                    FnLinks.COLLECTOR.open().use {
                                        RoleSet.COLLECTOR.open().use {
                                            Role.COLLECTOR.open().use {
                                                Example.COLLECTOR.open().use {
                                                    Rel.COLLECTOR.open().use {
                                                        Arg.COLLECTOR.open().use {
                                                            Word.COLLECTOR.open().use {
                                                                Progress.tracePending("collector", "aspect")
                                                                Insert.insertStringMap(Example.ASPECT_COLLECTOR, File(outDir, names.file("aspects")), names.table("aspects"), names.columns("aspects"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "form")
                                                                Insert.insertStringMap(Example.FORM_COLLECTOR, File(outDir, names.file("forms")), names.table("forms"), names.columns("forms"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "person")
                                                                Insert.insertStringMap(Example.PERSON_COLLECTOR, File(outDir, names.file("persons")), names.table("persons"), names.columns("persons"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "tense")
                                                                Insert.insertStringMap(Example.TENSE_COLLECTOR, File(outDir, names.file("tenses")), names.table("tenses"), names.columns("tenses"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "voice")
                                                                Insert.insertStringMap(Example.VOICE_COLLECTOR, File(outDir, names.file("voices")), names.table("voices"), names.columns("voices"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "func")
                                                                Insert.insert<Func>(Func.COLLECTOR, File(outDir, names.file("funcs")), names.table("funcs"), names.columns("funcs"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "vntheta")
                                                                Insert.insert<VnLinks>(VnLinks.COLLECTOR, File(outDir, names.file("vnthetas")), names.table("vnthetas"), names.columns("vnthetas"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "fntheta")
                                                                Insert.insert<FnLinks>(FnLinks.COLLECTOR, File(outDir, names.file("fnthetas")), names.table("fnthetas"), names.columns("fnthetas"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("set", "argtype")
                                                                Insert.insert<ArgType>(ArgType.SET, ArgType.COMPARATOR, File(outDir, names.file("argtypes")), names.table("argtypes"), names.columns("argtypes"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "roleset")
                                                                Insert.insert<RoleSet>(RoleSet.COLLECTOR, File(outDir, names.file("rolesets")), names.table("rolesets"), names.columns("rolesets"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "role")
                                                                Insert.insert<Role>(Role.COLLECTOR, File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "example")
                                                                Insert.insert<Example>(Example.COLLECTOR, File(outDir, names.file("examples")), names.table("examples"), names.columns("examples"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "arg")
                                                                Insert.insert<Arg>(Arg.COLLECTOR, File(outDir, names.file("args")), names.table("args"), names.columns("args"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("collector", "rel")
                                                                Insert.insert<Rel>(Rel.COLLECTOR, File(outDir, names.file("rels")), names.table("rels"), names.columns("rels"), header)
                                                                Progress.traceDone()

                                                                Progress.tracePending("set", "member")
                                                                Insert.insert<Member>(Member.SET, Member.COMPARATOR, File(outDir, names.file("members")), names.table("members"), names.columns("members"), header)
                                                                Progress.traceDone()

                                                                // R E S O L V A B L E
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
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertWords() {
        Progress.tracePending("collector", "word")
        Insert.insert<Word>(Word.COLLECTOR, File(outDir, names.file("words")), names.table("words"), names.columns("words"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnAliases() {
        Progress.tracePending("collector", "fnalias")
        Insert.insert<FnAlias>(FnAlias.SET, FnAlias.COMPARATOR, File(outDir, names.file("pbrolesets_fnframes")), names.table("pbrolesets_fnframes"), names.columns("pbrolesets_fnframes"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnAliases() {
        Progress.tracePending("set", "vnalias")
        Insert.insert<VnAlias>(VnAlias.SET, VnAlias.COMPARATOR, File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        Insert.insert<VnRoleAlias>(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header)
        Progress.traceDone()
    }
}
