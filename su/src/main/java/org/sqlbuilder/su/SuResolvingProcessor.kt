package org.sqlbuilder.su

import org.sqlbuilder.common.NotFoundException
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.su.joins.Term_Synset
import org.sqlbuilder.su.objects.Term
import org.sqlbuilder.su.objects.TermAttr.Companion.make
import java.io.File
import java.io.PrintStream
import java.util.*

open class SuResolvingProcessor(conf: Properties) : SuProcessor(conf) {

    @JvmField
    protected val wordResolver: SuWordResolver

    @JvmField
    protected val synsetResolver: SuSynsetResolver

    @JvmField
    protected val synset31Resolver: SuSynset31Resolver

    init {
        // header
        header += "\n-- ${conf.getProperty("wn_resolve_against")}"

        // columns
        termsColumns = names.columns("terms", true)
        synsetsColumns = names.columns("terms_synsets", true)

        // outdir
        outDir = File(conf.getProperty("su_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // resolver
        resolve = true
        val wordSerFile = conf.getProperty("word_nids")
        wordResolver = SuWordResolver(wordSerFile)
        val synsetSerFile = conf.getProperty("synset_nids")
        synsetResolver = SuSynsetResolver(synsetSerFile)
        val synset31SerFile = conf.getProperty("synsets30_to_synsets31")
        synset31Resolver = SuSynset31Resolver(synset31SerFile)
    }

    override fun processTerms(ps: PrintStream, terms: Iterable<Term>, table: String, columns: String) {
        val iterator: Iterator<Term> = terms.iterator()
        if (iterator.hasNext()) {
            ps.println("INSERT INTO $table ($columns) VALUES")
            while (iterator.hasNext()) {
                val term = iterator.next()
                val isLast = !iterator.hasNext()
                val row = term.dataRow()
                val wordId = wordResolver.apply(term.term.lowercase())
                ps.println("($row,${nullableInt(wordId)})${if (isLast) ";" else ","}")
            }
        }
    }

    override fun processTermsAndAttrs(ps: PrintStream, ps2: PrintStream, terms: Iterable<Term>, kb: Kb, table: String, columns: String, table2: String, columns2: String) {
        val iterator: Iterator<Term> = terms.iterator()
        if (iterator.hasNext()) {
            var i = 0
            ps.println("INSERT INTO $table ($columns) VALUES")
            ps2.println("INSERT INTO $table2 ($columns2) VALUES")
            while (iterator.hasNext()) {
                val term = iterator.next()
                val isLast = !iterator.hasNext()
                val row = term.dataRow()
                val wordId = wordResolver.apply(term.term.lowercase())
                ps.println("($row,${nullableInt(wordId)})${if (isLast) ";" else ","}")

                val termid = term.resolve()
                try {
                    val attributes = make(term, kb)
                    for (attribute in attributes) {
                        val row2 = "$termid,${attribute.dataRow()}"
                        val comment2 = term.comment()
                        ps2.print("${if (i == 0) "" else ",\n"}($row2) /* $comment2 */")
                        i++
                    }
                } catch (_: NotFoundException) {
                }
            }
            ps2.println(";")
        }
    }

    override fun processSynsets(ps: PrintStream, terms_synsets: Iterable<Term_Synset>, table: String, columns: String) {
        val iterator: Iterator<Term_Synset> = terms_synsets.iterator()
        if (iterator.hasNext()) {
            ps.println("INSERT INTO $table ($columns) VALUES")
            while (iterator.hasNext()) {
                val map = iterator.next()
                val isLast = !iterator.hasNext()
                val row = map.dataRow()

                // 30 to 31
                val posId = map.posId // {n,v,a,r,s}
                val synset30Id = map.synsetId
                val synset31Id = synset31Resolver.apply(posId, synset30Id)

                // 31 to XX
                var resolvedSynsetId: Int? = null
                if (synset31Id != null) {
                    val synsetId = "${"%08d".format(synset31Id)}-$posId"
                    resolvedSynsetId = synsetResolver.apply(synsetId)
                }
                val comment = map.comment()
                ps.println("($row,${nullableInt(resolvedSynsetId)})${if (isLast) ";" else ","} -- $comment")
            }
        }
    }
}
