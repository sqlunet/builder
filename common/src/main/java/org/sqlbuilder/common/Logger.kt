package org.sqlbuilder.common

class Logger {

    fun logParseException(moduleId: String, tag: String, filename: String, lineNum: Long, line: String?, pe: ParseException) {
        System.err.println("$moduleId $tag $filename:$lineNum ${if (line == null) "" else "='$line'"} ${pe.message}")
    }

    fun logNotFoundException(moduleId: String, tag: String, filename: String, lineNum: Long, line: String?, nfe: NotFoundException) {
        System.err.println("$moduleId $tag $filename:$lineNum ${if (line == null) "" else "='$line'"} ${nfe.message}")
    }

    fun logXmlException(moduleId: String, tag: String, where: String?, e: Exception) {
        System.err.println("$moduleId $tag ${if (where != null) "'$where' " else ""}${e.message}")
    }

    fun logWarn(moduleId: String, tag: String, tag2: String, message: String) {
        System.err.println("$moduleId $tag $tag2 $message")
    }

    companion object {

        @JvmField
        val instance: Logger = Logger()
    }
}
