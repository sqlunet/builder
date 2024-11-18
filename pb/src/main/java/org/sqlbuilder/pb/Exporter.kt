package org.sqlbuilder.pb

import org.sqlbuilder.common.Names
import org.sqlbuilder.pb.foreign.FnLinks
import org.sqlbuilder.pb.foreign.VnLinks
import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word
import org.sqlbuilder2.ser.Serialize
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

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
        System.out.printf("%s %d%n", "vnlinks", VnLinks.COLLECTOR.size)
        System.out.printf("%s %d%n", "fnlinks", FnLinks.COLLECTOR.size)
        System.out.printf("%s %d%n", "roles", Role.COLLECTOR.size)
        System.out.printf("%s %d%n", "classes", RoleSet.COLLECTOR.size)
        System.out.printf("%s %d%n", "words", Word.COLLECTOR.size)
        duplicateRoles()

        VnLinks.COLLECTOR.open().use {
            FnLinks.COLLECTOR.open().use {
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
        serializeVnLinks()
        serializeFnLinks()
        serializeRoleSets()
        serializeRolesBare()
        serializeRoles()
        serializeWords()
    }

    @Throws(IOException::class)
    fun export() {
        exportVnLinks()
        exportFnLinks()
        exportRoleSets()
        exportRolesBare()
        exportRoles()
        exportWords()
    }

    @Throws(IOException::class)
    fun serializeVnLinks() {
        val m = makeVnLinksMap()
        Serialize.serialize(m, File(outDir, names.serFile("vnthetas", ".resolve_[theta]-[thetaid]")))
    }

    @Throws(IOException::class)
    fun serializeFnLinks() {
        val m = makeVnLinksMap()
        Serialize.serialize(m, File(outDir, names.serFile("fntheta", ".resolve_[theta]-[thetaid]")))
    }

    @Throws(IOException::class)
    fun serializeRoleSets() {
        val m = makeRoleSetsMap()
        Serialize.serialize(m, File(outDir, names.serFile("rolesets", ".resolve_[roleset]-[rolesetid]")))
    }

    @Throws(IOException::class)
    fun serializeRolesBare() {
        val m = makeRolesMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles", ".resolve_[roleset,argtype]-[roleid]")))
    }

    @Throws(IOException::class)
    private fun serializeRoles() {
        val m = makeRolesFromArgTypeToFullMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles", ".resolve_[roleset,argtype]-[roleid,rolesetid]")))
    }

    @Throws(IOException::class)
    fun serializeWords() {
        val m = makeWordMap()
        Serialize.serialize(m, File(outDir, names.serFile("words", ".resolve_[word]-[pbwordid]")))
    }

    @Throws(IOException::class)
    fun exportVnLinks() {
        val m = makeVnLinksMap()
        export(m, File(outDir, names.mapFile("vnthetas.resolve", "_[theta]-[thetaid]")))
    }

    @Throws(IOException::class)
    fun exportFnLinks() {
        val m = makeVnLinksMap()
        export(m, File(outDir, names.mapFile("fnthetas.resolve", "_[theta]-[thetaid]")))
    }

    @Throws(IOException::class)
    fun exportRoleSets() {
        val m = makeRoleSetsMap()
        export(m, File(outDir, names.mapFile("rolesets.resolve", "_[roleset]-[rolesetid]")))
    }

    @Throws(IOException::class)
    fun exportRolesBare() {
        val m = makeRolesTreeMap()
        export(m, File(outDir, names.mapFile("roles.resolve", "_[roleset,argtype]-[roleid]")))
    }

    @Throws(IOException::class)
    fun exportRoles() {
        val m = makeRolesFromArgTypeToFullTreeMap()
        export(m, File(outDir, names.mapFile("roles.resolve", "_[roleset,argtype]-[roleid,rolesetid]")))
    }

    @Throws(IOException::class)
    fun exportWords() {
        val m = makeWordMap()
        export(m, File(outDir, names.mapFile("words.resolve", "_[word]-[pbwordid]")))
    }

    fun duplicateRoles() {
        Role.COLLECTOR.entries
            .groupBy { it.key }
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
        return Word.COLLECTOR.entries
            .asSequence()
            .map { it.key.word to it.value }
            .toMap()
            .toSortedMap()
    }

    fun makeRoleSetsMap(): Map<String, Int> {
        return RoleSet.COLLECTOR.entries
            .asSequence()
            .map { it.key.name to it.value }
            .toMap()
            .toSortedMap()
    }

    fun makeVnLinksMap(): Map<String, Int> {
        return VnLinks.COLLECTOR.entries
            .asSequence()
            .map { it.key.names.toString() to it.value }
            .toMap()
            .toSortedMap()
    }

    fun makeFnLinksMap(): Map<String, Int> {
        return FnLinks.COLLECTOR.entries
            .asSequence()
            .map { it.key.names.toString() to it.value }
            .toMap()
            .toSortedMap()
    }

    fun makeRolesMap(): Map<Pair<String, String>, Int> {
        duplicateRoles()
        return Role.COLLECTOR.entries
            .asSequence()
            .map {
                val r = it.key
                val rs = r.roleSet
                (rs.name to r.argType) to it.value
            }
            .toMap()
    }

    fun makeRolesTreeMap(): Map<Pair<String, String>, Int> {
        return Role.COLLECTOR.entries
            .asSequence()
            .map {
                val r = it.key
                val rs = r.roleSet
                (rs.name to r.argType) to it.value
            }
            .toMap()
            .toSortedMap(COMPARATOR)
    }

    /**
     * Detailed role maps
     *
     * @return (rolesetname, argtype) -> (roleid, rolesetid)
     */
    fun makeRolesFromArgTypeToFullMap(): Map<Pair<String, String>, Pair<Int, Int>> {
        return Role.COLLECTOR.entries
            .asSequence()
            .map {
                val r = it.key
                val rs = r!!.roleSet
                (rs.name to r.argType) to (it.value to rs.intId)
            }
            .toMap()
    }

    /**
     * Detailed role maps
     *
     * @return (rolesetname, argtype) -> (roleid, rolesetid)
     */
    fun makeRolesFromArgTypeToFullTreeMap(): Map<Pair<String, String>, Pair<Int, Int>> {
        return Role.COLLECTOR.entries
            .asSequence()
            .map {
                val r = it.key
                val rs = r!!.roleSet
                (rs.name to r.argType) to (it.value to rs.intId)
            }
            .toMap()
            .toSortedMap(COMPARATOR)
    }

    companion object {

        private val COMPARATOR: Comparator<Pair<String, String>> =
            Comparator.comparing { p: Pair<String, String> -> p.first }
                .thenComparing<String> { it.second }

        @Throws(IOException::class)
        fun <K, V> export(m: Map<K, V>, file: File) {
            PrintStream(FileOutputStream(file), true, StandardCharsets.UTF_8).use { ps ->
                export<K, V>(ps, m)
            }
        }

        fun <K, V> export(ps: PrintStream, m: Map<K, V>) {
            m.forEach { (strs: K, nids: V) -> ps.printf("%s -> %s%n", strs, nids) }
        }
    }
}