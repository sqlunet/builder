package org.sqlbuilder.common

object SqlId {

    fun getSqlId(id: Int?): Any {
        if (id != null) {
            return id
        }
        return "NULL"
    }
}
