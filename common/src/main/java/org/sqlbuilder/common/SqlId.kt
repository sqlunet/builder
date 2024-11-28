package org.sqlbuilder.common

object SqlId {

    @JvmStatic
    fun getSqlId(id: Int?): Any {
        if (id != null) {
            return id
        }
        return "NULL"
    }
}
