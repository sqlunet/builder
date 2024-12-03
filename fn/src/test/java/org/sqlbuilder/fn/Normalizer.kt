package org.sqlbuilder.fn

import java.util.*

class Normalizer(
    newTable: String,
    newCol: String,
    newIdCol: String
) {

    private val props: Properties = Properties()

    private val sqls: MutableCollection<String> = ArrayList<String>()

    private val ignoreExceptionSqls: MutableCollection<String> = ArrayList<String>()

    init {
        this.props.put("newtable", getTable(newTable))
        this.props.put("newcol", newCol)
        this.props.put("newidcol", newIdCol)
        this.props.put("collength", "80")
    }

    // T A R G E T

    fun targets(oldTable: String, oldCol: String, oldIdCol: String): Normalizer {
        this.props.put("oldtable", getTable(oldTable))
        this.props.put("oldcol", oldCol)
        this.props.put("oldidcol", oldIdCol)
        return this
    }

    // O P E R A T I O N S

    fun create(): Normalizer {
        this.sqls.addAll(expand(SQLS_CREATE))
        return this
    }

    fun create2(): Normalizer {
        this.sqls.addAll(expand(SQLS_CREATE2))
        return this
    }

    fun insert(): Normalizer {
        this.sqls.addAll(expand(SQLS_INSERT))
        return this
    }

    fun insert2(): Normalizer {
        this.sqls.addAll(expand(SQLS_INSERT2))
        return this
    }

    fun reference(): Normalizer {
        this.ignoreExceptionSqls.addAll(expand(SQLS_DROP_FK_COLUMN))
        this.sqls.addAll(expand(SQLS_CREATE_FK_COLUMN))
        this.sqls.addAll(expand(SQLS_UPDATE_FK_COLUMN))
        return this
    }

    fun referenceThrough(vararg args: String): Normalizer {
        val meanExpr: String = joinAs("m", *args)
        this.props.put("through", meanExpr)
        this.sqls.addAll(expand(SQLS_UPDATE_FK_COLUMN2))
        return this
    }

    fun cleanup(): Normalizer {
        this.sqls.addAll(expand(SQL_CLEANUP))
        return this
    }

    fun swapPk(oldPk: String, newPk: String): Normalizer {
        this.props.put("oldpk", oldPk)
        this.props.put("newpk", newPk)
        this.sqls.addAll(expand(SQLS_SWAP_PK))
        return this
    }

    fun newPk(newPk: String): Normalizer {
        this.props.put("newpk", newPk)
        this.sqls.addAll(expand(SQLS_NEW_PK))
        return this
    }

    // H E L P E R S

    private fun expand(strs: Array<String>): Collection<String> {
        return expand(strs, this.props)
    }

    // E X E C

    fun dump() {
        this.props.list(System.out)
        println("-- SQL")
        dump(sqls)
    }

    companion object {

        const val PASSTHROUGH: Boolean = false

        private val SQLS_CREATE = arrayOf(Resources.resources.getString("Normalizer.drop-table"), Resources.resources.getString("Normalizer.create-table"), Resources.resources.getString("Normalizer.create-unq-index"))

        private val SQLS_CREATE2 = arrayOf(Resources.resources.getString("Normalizer.drop-table"), Resources.resources.getString("Normalizer.create2-table"), Resources.resources.getString("Normalizer.create-unq-index"))

        private val SQLS_INSERT = arrayOf(Resources.resources.getString("Normalizer.insert"))

        private val SQLS_INSERT2 = arrayOf(Resources.resources.getString("Normalizer.insert2"))

        private val SQLS_CREATE_FK_COLUMN = arrayOf(Resources.resources.getString("Normalizer.add-column-fk"))

        private val SQLS_DROP_FK_COLUMN = arrayOf(Resources.resources.getString("Normalizer.drop-column-fk"))

        private val SQLS_UPDATE_FK_COLUMN = arrayOf(Resources.resources.getString("Normalizer.update"))

        private val SQLS_UPDATE_FK_COLUMN2 = arrayOf(Resources.resources.getString("Normalizer.update2"))

        private val SQL_CLEANUP = arrayOf(Resources.resources.getString("Normalizer.drop-column-data"))

        private val SQLS_SWAP_PK = arrayOf(Resources.resources.getString("Normalizer.drop-auto-pk"), Resources.resources.getString("Normalizer.change-pk"), Resources.resources.getString("Normalizer.drop-pk"))

        private val SQLS_NEW_PK = arrayOf(Resources.resources.getString("Normalizer.new-pk"))

        private val SQLS_LENGTH: String? = Resources.resources.getString("Normalizer.length")

        private fun joinAs(alias: String, vararg args: String): String {
            val sb = StringBuilder()
            var i = 0
            while (i < args.size) {
                val table: String? = args[i]
                val joinColumn: String? = args[i + 1]
                if (i > 0) {
                    sb.append(' ')
                }
                sb.append("LEFT JOIN ")
                sb.append(getTable(table))
                if (i + 2 >= args.size) {
                    sb.append(" AS ").append(alias)
                }
                sb.append(" USING(")
                sb.append(joinColumn)
                sb.append(")")
                i += 2
            }
            return sb.toString()
        }

        private fun expand(strs: Array<String>, props: Properties): MutableCollection<String> {
            val strs2: MutableCollection<String> = ArrayList<String>()
            for (str in strs) {
                strs2.add(Resources.expand(str, props))
            }
            return strs2
        }

        private fun dump(strs: Collection<String>) {
            for (str in strs) {
                println(str)
            }
        }

        private fun getTable(tableKey: String?): String? {
            if (PASSTHROUGH) {
                return tableKey
            }
            return Resources.resources.getString("$tableKey.table")
        }
    }
}
