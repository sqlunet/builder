package org.sqlbuilder.fn

import java.util.*
import java.util.regex.Pattern

class Resources {

    // key-value map
    private val resourceMap: MutableMap<String, String> = TreeMap<String, String>()

    // initialize
    fun addResource(bundle: ResourceBundle) {
        for (key in bundle.keySet()) {
            this.resourceMap.put(key, bundle.getString(key))
        }
    }

    // remove
    fun removeKeys(vararg keys: String?): Int {
        var count = 0
        for (key in keys) {
            val value = this.resourceMap.remove(key)
            if (value != null) {
                count++
                // System.err.println("removed key " + key + " val=" + value);
            }
        }
        return count
    }

    // keys
    fun getKeysWithOp(str: String): MutableList<String> {
        val result: MutableList<String> = ArrayList<String>()
        for (key in this.resourceMap.keys) {
            val fields = key.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val opKey = fields[fields.size - 1]
            if (opKey.startsWith(str)) {
                result.add(key)
            }
        }
        return result
    }

    // values
    fun getValuesWithOp(str: String, strict: Boolean): MutableList<String?> {
        val result: MutableList<String?> = ArrayList<String?>()
        for (key in this.resourceMap.keys) {
            val fields = key.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val opKey = fields[fields.size - 1]
            if (if (strict) (opKey == str) else opKey.startsWith(str)) {
                result.add(getString(key))
            }
        }
        return result
    }

    fun dumpAll() {
        for (key in this.resourceMap.keys) {
            val value = getString(key)
            if (value.indexOf('!') != -1 || value.indexOf('%') != -1) {
                println(key + '+' + value)
            }
        }
    }

    fun dumpKeys(regExp: String) {
        for (key in this.resourceMap.keys) {
            if (!key.matches(regExp.toRegex())) {
                continue
            }

            val value = getString(key)
            if (value.indexOf('!') != -1 || value.indexOf('%') != -1) {
                println(key + "+" + value)
            }
        }
    }

    fun dumpRawKeys(regExp: String) {
        for (entry in this.resourceMap.entries) {
            val key = entry.key
            val value = entry.value

            if (!key.matches(regExp.toRegex())) {
                continue
            }

            if (value.indexOf('!') != -1 || value.indexOf('%') != -1) {
                println(key + "+" + value)
            }
        }
    }

    val tables: MutableList<String?>
        // tables
        get() = getValuesWithOp("table", true)

    fun getTables(vararg tableKeys0: String?): MutableList<String?> {
        if (tableKeys0 == null) return this.tables

        val result: MutableList<String?> = ArrayList<String?>()
        for (key in getKeysWithOp("table")) {
            // add if it matches one of the input table keys
            for (tableKey0 in tableKeys0) {
                val fields = key.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val tableKey = fields[0]
                if (tableKey == tableKey0) {
                    result.add(getString(key))
                }
            }
        }
        return result
    }

    // get value
    fun getString(key: String?): String {
        try {
            var str: String = this.resourceMap.get(key)!!
            if (str == null) throw MissingResourceException(key, Resources::class.java.getName(), key)

            // System.out.print("\n>[" + key + "] " + str);
            str = expandPercent(str)
            // System.out.println("\n< " + str);
            return str
        } catch (e: MissingResourceException) {
            System.err.println("NO KEY:" + e.getKey())
            throw e // '!' + key + '!';
        }
    }

    private fun expandPercent(str: String): String {
        val REGEX = "%[^%]*%"
        val pattern = Pattern.compile(REGEX)

        // percents
        var percentCount = 0
        var p = 0
        while ((str.indexOf('"', p + 1).also { p = it }) != -1) {
            percentCount++
        }
        if (percentCount % 2 != 0) return null

        // macro map
        val map: MutableMap<String?, String?> = HashMap<String?, String?>()
        val matcher = pattern.matcher(str) // get a matcher object
        while (matcher.find()) {
            val match = matcher.group()
            val key = match.substring(1, match.length - 1)
            // System.out.println(" *" + key);
            if (!map.containsKey(key)) {
                val value = getString(key)
                map.put(key, value)
            }
        }

        // macro substitution
        var result = str
        for (entry in map.entries) {
            val key = entry.key
            val value: String = entry.value!!
            // String regExp = "%" + key.replace("$", "\\$") + "%";
            // result = result.replaceAll(regExp, value);
            result = result.replace("%" + key + "%", value)
        }
        return result
    }

    companion object {

        val resources: Resources = Resources()

        init {
            val resourceName = Resources::class.java.getPackage().getName() + '.' + "normalizer"
            resources.addResource(ResourceBundle.getBundle(resourceName))
        }

        // concat arrays
        fun concat(vararg strs: String?): Array<String?>? {
            return strs
        }

        fun concat(vararg strss: Array<String?>?): Array<String?> {
            val result: MutableList<String?> = ArrayList<String?>()
            for (strs in strss) {
                Collections.addAll<String?>(result, *strs)
            }
            return result.toArray<String?>(arrayOf<String?>())
        }

        // join
        fun join(strs: MutableCollection<String?>?): String? {
            if (strs == null) return null
            val list: MutableList<String?> = ArrayList<String?>(strs)
            Collections.sort<String?>(list)
            val sb = StringBuilder()
            for (str in list) {
                sb.append(str)
                sb.append(' ')
            }
            return sb.toString()
        }

        // property factory
        fun makeProps(vararg strs: String?): Properties {
            val props = Properties()
            var i = 0
            while (i < strs.size) {
                val key: String? = strs[i]
                val value: String? = strs[i + 1]
                props.put(key, value)
                i += 2
            }
            return props
        }

        fun expand(str: String, vararg strs: String?): String {
            return expand(str, makeProps(*strs))
        }

        fun expand(str: String, props: Properties): String {
            val REGEX = "\\$[^$]+\\$"
            val pattern = Pattern.compile(REGEX)

            // match
            var result = str
            val matcher = pattern.matcher(str) // get a matcher object
            while (matcher.find()) {
                val match = matcher.group()
                val key = match.substring(1, match.length - 1)
                val value = props.getProperty(key)
                if (value == null) {
                    System.err.println("[" + match + "] -> null")
                    throw NoSuchElementException(match)
                }
                result = result.replace(("\\$" + key + "\\$").toRegex(), value)
            }
            return result
        }
    }
}
