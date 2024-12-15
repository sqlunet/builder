package org.semantikos.common

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.regex.Pattern

object XmlChecker {

    var errors: Int = 0

    @Throws(RuntimeException::class)
    fun checkEmpty(node: Node, context: String?, logOnly: Boolean) {
        if (!node.nodeValue.matches(" *".toRegex())) {
            ++errors
            val message = "empty node <$node> ${context ?: ""}"
            if (logOnly) {
                System.err.println(message)
            } else {
                throw RuntimeException(message)
            }
        }
    }

    @Throws(RuntimeException::class)
    fun checkSubElements(e: Element, regex: String?, context: String?, logOnly: Boolean) {
        val nodes = e.childNodes
        for (j in 0..<nodes.length) {
            val node = nodes.item(j)
            if (node is Element) {
                if (regex == null) {
                    ++errors
                    val message = "${e.nodeName} has element node <${node.nodeName}> ${context ?: ""}"
                    if (logOnly) {
                        System.err.println(message)
                    } else {
                        throw RuntimeException(message)
                    }
                }
                checkElementName(node.nodeName, regex!!, context)
            }
        }
    }

    @Throws(RuntimeException::class)
    fun checkElementName(name: String, regex: String, context: String?) {
        if (!name.matches(regex.toRegex())) {
            ++errors
            throw RuntimeException("element <$name> ${context ?: ""}")
        }
    }

    @Throws(RuntimeException::class)
    fun checkAttributeName(e: Element, regex: String?, context: String?, logOnly: Boolean): Boolean {
        val attrs = e.attributes
        if (regex != null) {
            for (i in 0..<attrs.length) {
                val attr = attrs.item(i)
                val name = attr.nodeName
                if (!name.matches(regex.toRegex())) {
                    ++errors
                    val message = "Illegal attribute in ${e.nodeName} <$name> ${context ?: ""}"
                    if (!logOnly) {
                        throw RuntimeException(message)
                    } else {
                        System.err.println(message)
                        return false
                    }
                }
            }
        } else {
            if (attrs.length != 0) {
                val sb = StringBuilder()
                for (i in 0..<attrs.length) {
                    if (i > 0) {
                        sb.append(' ')
                    }
                    val attr = attrs.item(i)
                    sb.append(attr.nodeName)
                }
                ++errors
                val message = "Illegal attribute in ${e.nodeName} <$sb> ${context ?: ""}"
                if (!logOnly) {
                    throw RuntimeException(message)
                } else {
                    System.err.println(message)
                    return false
                }
            }
        }
        return true
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValue(value: String?, regex: String, context: String?, logOnly: Boolean): Boolean {
        return checkAttributeValue(value, "\\s+", regex, context, logOnly)
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValue(value: String?, pattern: Pattern, context: String?, logOnly: Boolean): Boolean {
        return checkAttributeValue(value, "\\s+", pattern, context, logOnly)
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValue(value: String?, delim: String, regex: String, context: String?, logOnly: Boolean): Boolean {
        if (value == null || value.isEmpty()) {
            return true
        }
        val items = value.trim { it <= ' ' }.split(delim.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return checkAttributeValues(items, regex, context, logOnly)
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValue(value: String?, delim: String, pattern: Pattern, context: String?, logOnly: Boolean): Boolean {
        if (value == null || value.isEmpty()) {
            return true
        }
        val items = value.trim { it <= ' ' }.split(delim.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return checkAttributeValues(items, pattern, context, logOnly)
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValues(values: Array<String>?, regex: String, context: String?, logOnly: Boolean): Boolean {
        if (values == null || values.isEmpty()) {
            return true
        }
        val p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        for (value in values) {
            val m = p.matcher(value)
            if (!m.matches()) {
                ++errors
                val message = "Illegal attribute value <$value> ${context ?: ""}"
                if (!logOnly) {
                    throw RuntimeException(message)
                } else {
                    System.err.println(message)
                    return false
                }
            }
        }
        return true
    }

    @Throws(RuntimeException::class)
    fun checkAttributeValues(values: Array<String>?, pattern: Pattern, context: String?, logOnly: Boolean): Boolean {
        if (values == null || values.isEmpty()) {
            return true
        }
        for (value in values) {
            val m = pattern.matcher(value)
            if (!m.matches()) {
                ++errors
                val message = "Illegal attribute value <$value> ${context ?: ""}"
                if (!logOnly) {
                    throw RuntimeException(message)
                } else {
                    System.err.println(message)
                    return false
                }
            }
        }
        return true
    }
}
