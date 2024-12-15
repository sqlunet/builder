package org.semantikos.vn

import org.semantikos.common.Insert.insert
import org.semantikos.common.Names
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.vn.joins.*
import org.semantikos.vn.objects.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(conf: Properties) {

    protected val names: Names = Names("vn")

    protected var header: String = conf.getProperty("vn_header")

    protected var outDir: File = File(conf.getProperty("vn_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    open fun insert() {
        VnClass.COLLECTOR.open().use {
            RoleType.COLLECTOR.open().use {
                Role.COLLECTOR.open().use {
                    RestrType.COLLECTOR.open().use {
                        Restrs.COLLECTOR.open().use {
                            Frame.COLLECTOR.open().use {
                                FrameName.COLLECTOR.open().use {
                                    FrameSubName.COLLECTOR.open().use {
                                        FrameExample.COLLECTOR.open().use {
                                            Syntax.COLLECTOR.open().use {
                                                Semantics.COLLECTOR.open().use {
                                                    Grouping.COLLECTOR.open().use {
                                                        Predicate.COLLECTOR.open().use {
                                                            Word.COLLECTOR.open().use {
                                                                traceSaving("class")
                                                                insert(
                                                                    VnClass.COLLECTOR,
                                                                    VnClass.COLLECTOR,
                                                                    File(outDir, names.file("classes")),
                                                                    names.table("classes"),
                                                                    names.columns("classes"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("roletype")
                                                                insert(
                                                                    RoleType.COLLECTOR,
                                                                    RoleType.COLLECTOR,
                                                                    File(outDir, names.file("roletypes")),
                                                                    names.table("roletypes"),
                                                                    names.columns("roletypes"),
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

                                                                traceSaving("restrtype")
                                                                insert(
                                                                    RestrType.COLLECTOR,
                                                                    RestrType.COLLECTOR,
                                                                    File(outDir, names.file("restrtypes")),
                                                                    names.table("restrtypes"),
                                                                    names.columns("restrtypes"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("restrs")
                                                                insert(
                                                                    Restrs.COLLECTOR,
                                                                    Restrs.COLLECTOR,
                                                                    File(outDir, names.file("restrs")),
                                                                    names.table("restrs"),
                                                                    names.columns("restrs"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("name")
                                                                insert(
                                                                    FrameName.COLLECTOR,
                                                                    FrameName.COLLECTOR,
                                                                    File(outDir, names.file("framenames")),
                                                                    names.table("framenames"),
                                                                    names.columns("framenames"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("subname")
                                                                insert(
                                                                    FrameSubName.COLLECTOR,
                                                                    FrameSubName.COLLECTOR,
                                                                    File(outDir, names.file("framesubnames")),
                                                                    names.table("framesubnames"),
                                                                    names.columns("framesubnames"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("frame example")
                                                                insert(
                                                                    FrameExample.COLLECTOR,
                                                                    FrameExample.COLLECTOR,
                                                                    File(outDir, names.file("examples")),
                                                                    names.table("examples"),
                                                                    names.columns("examples"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("syntax")
                                                                insert(
                                                                    Syntax.COLLECTOR,
                                                                    Syntax.COLLECTOR,
                                                                    File(outDir, names.file("syntaxes")),
                                                                    names.table("syntaxes"),
                                                                    names.columns("syntaxes"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("semantics")
                                                                insert(
                                                                    Semantics.COLLECTOR,
                                                                    Semantics.COLLECTOR,
                                                                    File(outDir, names.file("semantics")),
                                                                    names.table("semantics"),
                                                                    names.columns("semantics"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("predicate")
                                                                insert(
                                                                    Predicate.COLLECTOR,
                                                                    Predicate.COLLECTOR,
                                                                    File(outDir, names.file("predicates")),
                                                                    names.table("predicates"),
                                                                    names.columns("predicates"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("frame")
                                                                insert(
                                                                    Frame.COLLECTOR,
                                                                    Frame.COLLECTOR,
                                                                    File(outDir, names.file("frames")),
                                                                    names.table("frames"),
                                                                    names.columns("frames"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("frame example")
                                                                insert(
                                                                    Frame_Example.SET,
                                                                    null as Comparator<Frame_Example>?,
                                                                    File(outDir, names.file("frames_examples")),
                                                                    names.table("frames_examples"),
                                                                    names.columns("frames_examples"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("predicate semantics")
                                                                insert(
                                                                    Predicate_Semantics.SET,
                                                                    null as Comparator<Predicate_Semantics>?,
                                                                    File(outDir, names.file("predicates_semantics")),
                                                                    names.table("predicates_semantics"),
                                                                    names.columns("predicates_semantics"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("class word")
                                                                insert(
                                                                    Class_Word.SET,
                                                                    Class_Word.COMPARATOR,
                                                                    File(outDir, names.file("members")),
                                                                    names.table("members"),
                                                                    names.columns("members"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("class frame")
                                                                insert(
                                                                    Class_Frame.SET,
                                                                    Class_Frame.COMPARATOR,
                                                                    File(outDir, names.file("classes_frames")),
                                                                    names.table("classes_frames"),
                                                                    names.columns("classes_frames"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("grouping")
                                                                insert(
                                                                    Grouping.COLLECTOR,
                                                                    Grouping.COLLECTOR,
                                                                    File(outDir, names.file("groupings")),
                                                                    names.table("groupings"),
                                                                    names.columns("groupings"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                traceSaving("member grouping")
                                                                insert(
                                                                    Member_Grouping.SET,
                                                                    Member_Grouping.COMPARATOR,
                                                                    File(outDir, names.file("members_groupings")),
                                                                    names.table("members_groupings"),
                                                                    names.columns("members_groupings"),
                                                                    header
                                                                )
                                                                traceDone()

                                                                // R E S O L V A B L E

                                                                insertWords()
                                                                insertMemberSenses()
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
    protected open fun insertMemberSenses() {
        traceSaving("member sense")
        insert(
            Member_Sense.SET,
            Member_Sense.COMPARATOR,
            File(outDir, names.file("members_senses")),
            names.table("members_senses"),
            names.columns("members_senses"),
            header
        )
        traceDone()
    }
}
