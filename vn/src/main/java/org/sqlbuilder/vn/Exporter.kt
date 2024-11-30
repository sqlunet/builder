package org.sqlbuilder.vn

import org.sqlbuilder.common.Names
import org.sqlbuilder.vn.objects.Role
import org.sqlbuilder.vn.objects.RoleType
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Serialize
import org.sqlbuilder2.ser.Triplet
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class Exporter
    (conf: Properties) {

    private val names: Names = Names("vn")

    private val outDir: File = File(conf.getProperty("vn_outdir_ser", "sers"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun run() {
        println("classes ${VnClass.COLLECTOR.size}")
        println("roles ${Role.COLLECTOR.size}")
        println("roletypes ${RoleType.COLLECTOR.size}")
        println("words ${Word.COLLECTOR.size}")

        RoleType.COLLECTOR.open().use {
            Role.COLLECTOR.open().use {
                VnClass.COLLECTOR.open().use {
                    Word.COLLECTOR.open().use {
                        serialize()
                        export()
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    fun serialize() {
        serializeClasses()
        serializeClassTags()
        serializeRoleTypes()
        serializeRolesUsingClassTags()
        serializeRolesUsingClassNames()
        serializeWords()
    }

    @Throws(IOException::class)
    fun export() {
        exportClasses()
        exportClassTags()
        exportRoleTypes()
        exportRolesUsingClassTags()
        exportRolesUsingClassNames()
        exportWords()
    }

    @Throws(IOException::class)
    fun serializeClassTags() {
        val m = makeClassTagsMap()
        Serialize.serialize(m, File(outDir, names.serFile("classes.resolve", "_[classtag]-[classid]")))
    }

    @Throws(IOException::class)
    fun serializeClasses() {
        val m = makeClassesMap()
        Serialize.serialize(m, File(outDir, names.serFile("classes.resolve", "_[classname]-[classid]")))
    }

    @Throws(IOException::class)
    fun serializeRoleTypes() {
        val m = makeRoleTypesMap()
        Serialize.serialize(m, File(outDir, names.serFile("roletypes.resolve", "_[roletype]-[roletypeid]")))
    }

    @Throws(IOException::class)
    fun serializeRolesUsingClassTags() {
        val m = makeClassTagsRolesMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles.resolve", "_[classtag,roletype]-[roleid,classid,roletypeid]")))
    }

    @Throws(IOException::class)
    fun serializeRolesUsingClassNames() {
        val m = makeClassesRolesMap()
        Serialize.serialize(m, File(outDir, names.serFile("roles.resolve", "_[classname,roletype]-[roleid,classid,roletypeid]")))
    }

    @Throws(IOException::class)
    fun serializeWords() {
        val m = makeWordMap()
        Serialize.serialize(m, File(outDir, names.serFile("words.resolve", "_[word]-[vnwordid]")))
    }

    @Throws(IOException::class)
    fun exportClassTags() {
        val m = makeClassTagsMap()
        export(m, File(outDir, names.mapFile("classes.resolve", "_[classtag]-[classid]")))
    }

    @Throws(IOException::class)
    fun exportClasses() {
        val m = makeClassesMap()
        export(m, File(outDir, names.mapFile("classes.resolve", "_[classname]-[classid]")))
    }

    @Throws(IOException::class)
    fun exportRoleTypes() {
        val m = makeRoleTypesMap()
        export(m, File(outDir, names.mapFile("roletypes.resolve", "_[roletype]-[roletypeid]")))
    }

    @Throws(IOException::class)
    fun exportRolesUsingClassTags() {
        val m = makeClassTagsRolesTreeMap()
        export(m, File(outDir, names.mapFile("roles.resolve", "_[classtag,roletype]-[roleid,classid,roletypeid]")))
    }

    @Throws(IOException::class)
    fun exportRolesUsingClassNames() {
        val m = makeClassesRolesTreeMap()
        export(m, File(outDir, names.mapFile("roles.resolve", "_[classname,roletype]-[roleid,classid,roletypeid]")))
    }

    @Throws(IOException::class)
    fun exportWords() {
        val m = makeWordMap()
        export(m, File(outDir, names.mapFile("words.resolve", "_[word]-[vnwordid]")))
    }

    // M A P S

    /**
     * Make word to wordid map
     *
     * @return word to wordid
     */
    fun makeWordMap(): Map<String, Int> {
        return Word.COLLECTOR
            .associate { it.word to Word.COLLECTOR.apply(it) }
            .toSortedMap()
    }

    /**
     * Make classname to classid map
     *
     * @return classname to classid
     */
    fun makeClassesMap(): Map<String, Int> {
        return VnClass.COLLECTOR
            .associate { it.name to VnClass.COLLECTOR.apply(it) }
            .toSortedMap()
    }

    /**
     * Make classtags to classid map
     *
     * @return classtags to classid
     */
    fun makeClassTagsMap(): Map<String, Int> {
        return VnClass.COLLECTOR
            .associate { it.tag to VnClass.COLLECTOR.apply(it) }
            .toSortedMap()
    }

    /**
     * Make roletype to roletypeid map
     *
     * @return roletype to roletypeid map
     */
    fun makeRoleTypesMap(): MutableMap<String?, Int?> {
        return RoleType.COLLECTOR
            .associate { it.type to RoleType.COLLECTOR.apply(it) }
            .toSortedMap()
    }

    /**
     * Make classtag,roletype to ids map
     *
     * @return (classtag, roletype) to (roleid,classid,roletypeid)
     */
    fun makeClassTagsRolesMap(): Map<Pair<String?, String?>?, Triplet<Int?, Int?, Int?>?> {
        return Role.COLLECTOR
            .associate { Pair(it.clazz.tag, it.restrRole.roleType.type) to Triplet(it.intId, it.clazz.intId, it.restrRole.roleType.intId) }
    }

    /**
     * Make classtag,roletype to ids treemap
     *
     * @return (classtag, roletype) to (roleid,classid,roletypeid)
     */
    fun makeClassTagsRolesTreeMap(): Map<Pair<String?, String?>, Triplet<Int?, Int?, Int?>> {
        return Role.COLLECTOR
            .asSequence()
            .map { Pair(it.clazz.tag, it.restrRole.roleType.type) to Triplet(it.intId, it.clazz.intId, it.restrRole.roleType.intId) }
            .toMap()
            .toSortedMap(COMPARATOR)
    }

    /**
     * Make classname,roletype to ids map
     *
     * @return (classnameroletype) to (roleid,classid,roletypeid)
     */
    fun makeClassesRolesMap(): Map<Pair<String?, String?>?, Triplet<Int?, Int?, Int?>?> {
        return Role.COLLECTOR
            .associate { Pair(it.clazz.name, it.restrRole.roleType.type) to Triplet(it.intId, it.clazz.intId, it.restrRole.roleType.intId) }
    }

    /**
     * Make classname,roletype to ids treemap
     *
     * @return (classname, roletype) to (roleid,classid,roletypeid)
     */
    fun makeClassesRolesTreeMap(): MutableMap<Pair<String?, String?>?, Triplet<Int?, Int?, Int?>?> {
        return Role.COLLECTOR
            .associate { Pair(it.clazz.name, it.restrRole.roleType.type) to Triplet(it.intId, it.clazz.intId, it.restrRole.roleType.intId) }
            .toSortedMap(COMPARATOR)
    }

    companion object {

        private val COMPARATOR: Comparator<Pair<String, String>> =
            Comparator
                .comparing<Pair<String, String>, String> { it.first }
                .thenComparing<String> { it.second }

        @Throws(IOException::class)
        fun <K, V> export(m: Map<K, V>, file: File) {
            PrintStream(FileOutputStream(file), true, StandardCharsets.UTF_8).use {
                export(it, m)
            }
        }

        fun <K, V> export(ps: PrintStream, m: Map<K, V>) {
            m.forEach { (strs: K, nids: V) -> ps.printf("$strs -> $nids") }
        }
    }
}
