package org.semantikos.pb

import org.semantikos.common.Names
import org.semantikos.common.Serialize
import org.semantikos.pb.foreign.AliasFnFeLinks
import org.semantikos.pb.foreign.AliasVnRoleLinks
import org.semantikos.pb.objects.Role
import org.semantikos.pb.objects.RoleSet
import org.semantikos.pb.objects.Word
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

open class Exporter(conf: Properties) {

    protected val names: Names = Names("pb")

    protected val outDir: File = File(conf.getProperty("pb_outdir_ser", "sers"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun run() {
        println("vn links ${AliasFnFeLinks.COLLECTOR.size}")
        println("fn links ${AliasFnFeLinks.COLLECTOR.size}")
        println("roles ${Role.COLLECTOR.size}")
        println("classes ${RoleSet.COLLECTOR.size}")
        println("words ${Word.COLLECTOR.size}")
        duplicateRoles()

        AliasFnFeLinks.COLLECTOR.open().use {
            AliasVnRoleLinks.COLLECTOR.open().use {
                Role.COLLECTOR.open().use {
                    RoleSet.COLLECTOR.open().use {
                        Word.COLLECTOR.open().use {
                            serialize()
                            export()
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun serialize() {
        serializeVnRoles()
        serializeFnFes()
        serializeRoleSets()
        serializeRolesBare()
        serializeRoles()
        serializeWords()
    }

    @Throws(IOException::class)
    fun export() {
        exportVnRoles()
        exportFnFes()
        exportRoleSets()
        exportRolesBare()
        exportRoles()
        exportWords()
    }

    @Throws(IOException::class)
    fun serializeWords() {
        val m = makeWordMap()
        Serialize.serialize(m, File(outDir, names.serFile("words", ".resolve_[word]-[pbwordid]")))
    }

    @Throws(IOException::class)
    fun serializeRoleSets() {
        val m = makeRoleSetsMap()
        Serialize.serialize(m, File(outDir, names.serFile("rolesets", ".resolve_[roleset]-[rolesetid]")))
    }

    @Throws(IOException::class)
    private fun serializeRoles() {
        val m = makeRolesFromArgTypeToFullMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles", ".resolve_[roleset,argtype]-[roleid,rolesetid]")))
    }

    @Throws(IOException::class)
    fun serializeRolesBare() {
        val m = makeRolesMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles", ".resolve_[roleset,argtype]-[roleid]")))
    }

    @Throws(IOException::class)
    fun serializeVnRoles() {
        val m = makeVnRolesMap()
        Serialize.serialize(m, File(outDir, names.serFile("vnroles", ".resolve_[fe]-[feid]")))
    }

    @Throws(IOException::class)
    fun serializeFnFes() {
        val m = makeFnFesMap()
        Serialize.serialize(m, File(outDir, names.serFile("fnfes", ".resolve_[fe]-[feid]")))
    }

    @Throws(IOException::class)
    fun exportWords() {
        val m = makeWordMap().toSortedMap()
        export(m, File(outDir, names.mapFile("words.resolve", "_[word]-[pbwordid]")))
    }

    @Throws(IOException::class)
    fun exportRoleSets() {
        val m = makeRoleSetsMap().toSortedMap()
        export(m, File(outDir, names.mapFile("rolesets.resolve", "_[roleset]-[rolesetid]")))
    }

    @Throws(IOException::class)
    fun exportRoles() {
        val m = makeRolesFromArgTypeToFullMap().toSortedMap(STRING_PAIR_COMPARATOR)
        export(m, File(outDir, names.mapFile("roles.resolve", "_[roleset,argtype]-[roleid,rolesetid]")))
    }

    @Throws(IOException::class)
    fun exportRolesBare() {
        val m = makeRolesMap().toSortedMap(STRING_PAIR_COMPARATOR)
        export(m, File(outDir, names.mapFile("roles.resolve", "_[roleset,argtype]-[roleid]")))
    }

    @Throws(IOException::class)
    fun exportVnRoles() {
        val m = makeVnRolesMap().toSortedMap()
        export(m, File(outDir, names.mapFile("vnroles.resolve", "_[vnrole]-[vnroleid]")))
    }

    @Throws(IOException::class)
    fun exportFnFes() {
        val m = makeFnFesMap().toSortedMap()
        export(m, File(outDir, names.mapFile("fnfes.resolve", "_[fe]-[feid]")))
    }

    fun duplicateRoles() {
        Role.COLLECTOR
            .groupBy { it }
            .forEach { (role, instances) ->
                if (instances.size > 1) {
                    println("Warning: Duplicate role: ${instances.size} instances for  role '$role' (rs=${role.roleSet} argt=${role.argType}) found.")
                }
            }
    }

    // M A P S

    /**
     * Make word to wordid map
     *
     * @return word to wordid
     */
    fun makeWordMap(): Map<String, Int> {
        return Word.COLLECTOR
            .asSequence()
            .map { it.word to Word.COLLECTOR.invoke(it) }
            .toMap()
    }

    fun makeRoleSetsMap(): Map<String, Int> {
        return RoleSet.COLLECTOR
            .asSequence()
            .map { it.name to RoleSet.COLLECTOR.invoke(it) }
            .toMap()
    }

    fun makeVnRolesMap(): Map<String, Int> {
        return AliasVnRoleLinks.COLLECTOR
            .asSequence()
            .map { it.names.toString() to AliasVnRoleLinks.COLLECTOR.invoke(it) }
            .toMap()
    }

    fun makeFnFesMap(): Map<String, Int> {
        return AliasFnFeLinks.COLLECTOR
            .asSequence()
            .map { it.names.toString() to AliasFnFeLinks.COLLECTOR.invoke(it) }
            .toMap()
    }

    fun makeRolesMap(): Map<Pair<String, String>, Int> {
        duplicateRoles()
        return Role.COLLECTOR
            .asSequence()
            .map {
                val rs = it.roleSet
                (rs.name to it.argType) to Role.COLLECTOR.invoke(it)
            }
            .toMap()
    }

    /**
     * Detailed role maps
     *
     * @return (rolesetname, argtype) -> (roleid, rolesetid)
     */
    fun makeRolesFromArgTypeToFullMap(): Map<Pair<String, String>, Pair<Int, Int>> {
        return Role.COLLECTOR
            .asSequence()
            .map {
                val rs = it.roleSet
                (rs.name to it.argType) to (Role.COLLECTOR.invoke(it) to rs.intId)
            }
            .toMap()
    }

    companion object {

        private val STRING_PAIR_COMPARATOR: Comparator<Pair<String, String>> =
            Comparator.comparing { p: Pair<String, String> -> p.first }
                .thenComparing<String> { it.second }

        @Throws(IOException::class)
        fun <K, V> export(m: Map<K, V>, file: File) {
            PrintStream(FileOutputStream(file), true, StandardCharsets.UTF_8).use { ps ->
                export<K, V>(ps, m)
            }
        }

        fun <K, V> export(ps: PrintStream, m: Map<K, V>) {
            m.forEach { ps.println("${it.key} -> ${it.value}") }
        }
    }
}