package org.sqlbuilder.su

import org.sqlbuilder.su.joins.Term_Synset
import org.sqlbuilder.su.objects.Term
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class SuUpdatingProcessor(conf: Properties) : SuResolvingProcessor(conf) {
    init {
        // output
        outDir = File(conf.getProperty("su_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        KBLoader().load()
        collectTerms(KBLoader.kb!!)
        collectSynsets(inDir.toString() + File.separator + SUMO_TEMPLATE, System.err)

        Term.COLLECTOR.open().use { ignored ->
            PrintStream(FileOutputStream(File(outDir, names.file("terms"))), true, StandardCharsets.UTF_8).use { ps ->
                ps.println("-- $header")
                processTerms(ps, Term.COLLECTOR, names.table("terms"), termsColumns)
            }
            PrintStream(FileOutputStream(File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8).use { ps ->
                ps.println("-- $header")
                processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), synsetsColumns)
            }
        }
    }

    override fun processTerms(ps: PrintStream, terms: Iterable<Term>, table: String, columns: String) {
        val iterator: Iterator<Term> = terms.iterator()
        if (iterator.hasNext()) {
            val colWordId = names.column("terms.wordid")
            val colTermId = names.column("terms.termid")

            var i = 0
            for (term in terms) {
                updateTermRow(ps, table, ++i, term, colWordId, colTermId)
            }
        }
    }

    override fun processTermsAndAttrs(ps: PrintStream, ps2: PrintStream, terms: Iterable<Term>, kb: Kb, table: String, columns: String, table2: String, columns2: String) {
        processTerms(ps, terms, table, columns)
    }

    private fun updateTermRow(ps: PrintStream, table: String, index: Int, term: Term, vararg columns: String) {
        val resolvedWordId = wordResolver.apply(term.term.lowercase())
        if (resolvedWordId != null) {
            val resolvedTermId = term.resolve()

            val setClause = "${columns[0]}=$resolvedWordId"
            val whereClause = "${columns[1]}=$resolvedTermId"
            ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1} ${term.comment()}")
        }
    }

    override fun processSynsets(ps: PrintStream, terms_synsets: Iterable<Term_Synset>, table: String, columns: String) {
        if (terms_synsets.iterator().hasNext()) {
            val colMapId = names.column("terms_synsets.mapid")
            val colPosId = names.column("terms_synsets.posid")
            val colSynsetId = names.column("terms_synsets.synsetid")

            var i = 0
            for (map in terms_synsets) {
                updateMapRow(ps, table, ++i, map, colSynsetId, colMapId, colPosId)
            }
        }
    }

    private fun updateMapRow(ps: PrintStream, table: String, index: Int, map: Term_Synset, vararg columns: String) {
        // 30 to 31
        val posId = map.posId // {n,v,a,r}
        val synset30Id = map.synsetId
        val synset31Id = synset31Resolver.apply(posId, synset30Id)
        if (synset31Id != null) {
            // 31 to XX
            val synsetId = "${"%08d".format(synset31Id)}-$posId"
            val resolvedSynsetId = synsetResolver.apply(synsetId)
            if (resolvedSynsetId != null) {
                val setClause = "${columns[0]}=$resolvedSynsetId"
                val whereClause = "${columns[1]}=$synsetId AND ${columns[2]}='$posId'"
                ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1} ${map.comment()}")
            }
        }
    }
}