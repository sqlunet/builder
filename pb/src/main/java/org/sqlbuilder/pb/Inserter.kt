package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress
import org.sqlbuilder.pb.foreign.FnAlias
import org.sqlbuilder.pb.foreign.VnAlias
import org.sqlbuilder.pb.foreign.VnRoleAlias
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
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    open fun insert() {
        Example.ASPECT_COLLECTOR.open().use { ignored1 ->
            Example.FORM_COLLECTOR.open().use { ignored2 ->
                Example.PERSON_COLLECTOR.open().use { ignored3 ->
                    Example.TENSE_COLLECTOR.open().use { ignored4 ->
                        Example.VOICE_COLLECTOR.open().use { ignored5 ->
                            Func.COLLECTOR.open().use { ignored6 ->
                                Theta.COLLECTOR.open().use { ignored8 ->
                                    RoleSet.COLLECTOR.open().use { ignored10 ->
                                        Role.COLLECTOR.open().use { ignored11 ->
                                            Example.COLLECTOR.open().use { ignored14 ->
                                                Rel.COLLECTOR.open().use { ignored15 ->
                                                    Arg.COLLECTOR.open().use { ignored16 ->
                                                        Word.COLLECTOR.open().use { ignored20 ->
                                                            Progress.tracePending("collector", "aspect")
                                                            Insert.insertStringMap2(Example.ASPECT_COLLECTOR, Example.ASPECT_COLLECTOR, File(outDir, names.file("aspects")), names.table("aspects"), names.columns("aspects"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "form")
                                                            Insert.insertStringMap2(Example.FORM_COLLECTOR, Example.FORM_COLLECTOR, File(outDir, names.file("forms")), names.table("forms"), names.columns("forms"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "person")
                                                            Insert.insertStringMap2(Example.PERSON_COLLECTOR, Example.PERSON_COLLECTOR, File(outDir, names.file("persons")), names.table("persons"), names.columns("persons"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "tense")
                                                            Insert.insertStringMap2(Example.TENSE_COLLECTOR, Example.TENSE_COLLECTOR, File(outDir, names.file("tenses")), names.table("tenses"), names.columns("tenses"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "voice")
                                                            Insert.insertStringMap2(Example.VOICE_COLLECTOR, Example.VOICE_COLLECTOR, File(outDir, names.file("voices")), names.table("voices"), names.columns("voices"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "func")
                                                            Insert.insert2<Func>(Func.COLLECTOR, File(outDir, names.file("funcs")), names.table("funcs"), names.columns("funcs"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "theta")
                                                            Insert.insert2<Theta>(Theta.COLLECTOR, File(outDir, names.file("thetas")), names.table("thetas"), names.columns("thetas"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("set", "argtype")
                                                            Insert.insert2<ArgType>(ArgType.SET, ArgType.COMPARATOR, File(outDir, names.file("argtypes")), names.table("argtypes"), names.columns("argtypes"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "roleset")
                                                            Insert.insert2<RoleSet>(RoleSet.COLLECTOR, RoleSet.COLLECTOR, File(outDir, names.file("rolesets")), names.table("rolesets"), names.columns("rolesets"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "role")
                                                            Insert.insert2<Role>(Role.COLLECTOR, File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "example")
                                                            Insert.insert2<Example>(Example.COLLECTOR, File(outDir, names.file("examples")), names.table("examples"), names.columns("examples"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "arg")
                                                            Insert.insert2<Arg>(Arg.COLLECTOR, Arg.COLLECTOR, File(outDir, names.file("args")), names.table("args"), names.columns("args"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("collector", "rel")
                                                            Insert.insert2<Rel>(Rel.COLLECTOR, File(outDir, names.file("rels")), names.table("rels"), names.columns("rels"), header)
                                                            Progress.traceDone()

                                                            Progress.tracePending("set", "member")
                                                            Insert.insert2<Member>(Member.SET, Member.COMPARATOR, File(outDir, names.file("members")), names.table("members"), names.columns("members"), header)
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

    @Throws(FileNotFoundException::class)
    protected open fun insertWords() {
        Progress.tracePending("collector", "word")
        Insert.insert2<Word>(Word.COLLECTOR, File(outDir, names.file("words")), names.table("words"), names.columns("words"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertFnAliases() {
        Progress.tracePending("collector", "fnalias")
        Insert.insert2<FnAlias>(FnAlias.SET, FnAlias.COMPARATOR, File(outDir, names.file("pbrolesets_fnframes")), names.table("pbrolesets_fnframes"), names.columns("pbrolesets_fnframes"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnAliases() {
        Progress.tracePending("set", "vnalias")
        Insert.insert2<VnAlias>(VnAlias.SET, VnAlias.COMPARATOR, File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header)
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        Insert.insert2<VnRoleAlias>(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header)
        Progress.traceDone()
    }
}
