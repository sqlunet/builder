package org.semantikos.su

import org.sigma.core.IterableFormula
import org.sigma.core.kif.StreamTokenizer_s
import org.semantikos.su.objects.Arg
import org.semantikos.su.objects.Formula
import org.semantikos.su.objects.SUFormula
import java.io.IOException
import java.io.Reader
import java.io.StreamTokenizer
import java.io.StringReader
import java.text.ParseException

/**
 * A class designed to read a file in SUO-KIF format into memory. See [suo-kif](https://suo.ieee.org/suo-kif.html) for a language specification.
 *
 * @author Adam Pease
 * @author Bernard Bou (rewrite)
 */
object FormulaParser {

    /**
     * Parse
     *
     * @param formula formula
     * @return parses
     * @throws IllegalArgumentException illegal
     * @throws ParseException parse
     * @throws IOException io exception
     */
    @Throws(IllegalArgumentException::class, ParseException::class, IOException::class)
    fun parse(formula: SUFormula): Map<String, Arg> {
        val reader: Reader = StringReader(formula.form)
        try {
            return parse(reader)
        } catch (e: IllegalArgumentException) {
            System.err.println("Formula: ${formula.form}")
            throw e
        }
    }

    /**
     * Parse
     *
     * @param reader reader
     * @return parses
     * @throws IllegalArgumentException illegal
     * @throws ParseException parse
     * @throws IOException io
     */
    @Throws(IllegalArgumentException::class, ParseException::class, IOException::class)
    fun parse(reader: Reader): Map<String, Arg> {
        // reader
        requireNotNull(reader) { "Null reader" }

        val map: MutableMap<String, Arg> = HashMap<String, Arg>()
        val sb = StringBuilder(40)

        // tokenizer
        val tokenizer = StreamTokenizer_s(reader)
        setupStreamTokenizer(tokenizer)

        // parser state
        var parenLevel = 0
        var inRule = false
        var argumentNum = -1
        var inAntecedent = false
        var inConsequent = false

        // parser line state
        var startLine = 0
        var isEOL = false

        do {
            // get next token
            val lastTokenType = tokenizer.ttype
            tokenizer.nextToken()

            // E N D O F L I N E

            // Check the situation when multiple KIF statements read as one
            // This relies on extra blank line to separate KIF statements
            if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
                if (isEOL) {
                    // two line separators in a row, shows a new KIF statement is to start.
                    // check if a new statement has already been generated, otherwise report error
                    if (sb.isNotEmpty()) {
                        throw ParseException("Parsing error : possible missed closing parenthesis", tokenizer.lineno())
                    }
                    continue
                }
                // Found a first end of line character.
                isEOL = true // Turn on flag, to watch for a second consecutive one.
                continue
            } else if (isEOL) {
                isEOL = false // Turn off isEOL if a non-space token encountered
            }

            // O P E N P A R E N T H E S I S
            if (tokenizer.ttype == 40) {
                if (parenLevel == 0) {
                    startLine = tokenizer.lineno()
                }
                parenLevel++
                if (inRule && !inAntecedent && !inConsequent) {
                    inAntecedent = true
                } else {
                    if (inRule && inAntecedent && parenLevel == 2) {
                        inAntecedent = false
                        inConsequent = true
                    }
                }
                if (parenLevel != 0 && lastTokenType != 40 && sb.isNotEmpty()) {
                    sb.append(" ")
                }
                sb.append("(")
            } else if (tokenizer.ttype == 41) {
                parenLevel--
                sb.append(")")

                if (parenLevel == 0) {
                    // end of the statement

                    // create formula

                    val f = SUFormula.of(sb.toString())
                    f.startLine = startLine
                    f.endLine = tokenizer.lineno()

                    // check argument validity
                    var errors = f.hasValidArgs(null, null)
                    if (errors == null) {
                        errors = f.hasValidQuantification()
                    }
                    if (errors != null) {
                        throw ParseException("Parsing error in : Invalid arguments $errors", startLine)
                    }

                    // reset state
                    inConsequent = false
                    inRule = false
                    argumentNum = -1
                    startLine = tokenizer.lineno() + 1 // start next statement from next line
                    sb.delete(0, sb.length)
                } else if (parenLevel < 0) {
                    throw ParseException("Parsing error : Extra closing parenthesis found.", startLine)
                }
            } else if (tokenizer.ttype == 34) {
                tokenizer.sval = tokenizer.sval.replace("\"", "\\\"")
                if (lastTokenType != 40) {
                    sb.append(" ")
                }
                sb.append("\"")
                sb.append(tokenizer.sval)
                sb.append("\"")
                if (parenLevel < 2) {
                    argumentNum = argumentNum + 1
                }
            } else if (tokenizer.ttype == StreamTokenizer.TT_NUMBER || tokenizer.sval != null && Character.isDigit(tokenizer.sval[0])) {
                if (lastTokenType != 40) {
                    sb.append(" ")
                }
                if (tokenizer.nval == 0.0) {
                    sb.append(tokenizer.sval)
                } else {
                    sb.append(tokenizer.nval)
                }
                if (parenLevel < 2) {
                    argumentNum = argumentNum + 1 // RAP - added on 11/27/04
                }
            } else if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
                checkNotNull(tokenizer.sval)
                if ((tokenizer.sval.compareTo("=>") == 0 || tokenizer.sval.compareTo("<=>") == 0) && parenLevel == 1) {
                    // RAP - added parenLevel clause on 11/27/04 to
                    // prevent implications embedded in statements from being rules
                    inRule = true
                }
                if (parenLevel < 2) {
                    argumentNum = argumentNum + 1
                }
                if (lastTokenType != 40) {
                    sb.append(" ")
                }
                sb.append(tokenizer.sval)
                if (sb.length > 64000) {
                    throw ParseException("Parsing error : Sentence over 64000 characters", startLine)
                }

                // Build the terms list and create special keys
                // Variables are not terms
                if (tokenizer.sval[0] != '?' && tokenizer.sval[0] != '@') {
                    // term
                    val term = tokenizer.sval

                    // term's relation to formula
                    val tokenRelation = Arg(inAntecedent, inConsequent, argumentNum, parenLevel)
                    tokenRelation.check()

                    map.put(term, tokenRelation)
                }
            } else if (tokenizer.ttype != StreamTokenizer.TT_EOF) {
                throw ParseException("Parsing error : Illegal character", startLine)
            }
        } while (tokenizer.ttype != StreamTokenizer.TT_EOF)

        if (sb.isNotEmpty()) {
            throw ParseException("Parsing error : Missed closing parenthesis", startLine)
        }

        return map
    }

    /**
     * Sets up the StreamTokenizer_s so that it parses SUO-KIF. = &lt; &gt; are treated as word characters, as are normal alphanumerics.
     * ';' is the line comment character and " is the quote character.
     *
     * @param st stream tokenizer
     */
    fun setupStreamTokenizer(st: StreamTokenizer_s) {
        st.whitespaceChars(0, 32)
        st.ordinaryChars(33, 44) // !"#$%&'()*+,
        st.wordChars(45, 46) // -.
        st.ordinaryChar(47) // /
        st.wordChars(48, 57) // 0-9
        st.ordinaryChars(58, 59) // :;
        st.wordChars(60, 64) // <=>?@
        st.wordChars(65, 90) // A-Z
        st.ordinaryChars(91, 94) // [\]^
        st.wordChars(95, 95) // _
        st.ordinaryChar(96) // `
        st.wordChars(97, 122) // a-z
        st.ordinaryChars(123, 255) // {|}~
        st.quoteChar('"'.code)
        st.commentChar(';'.code)
        st.eolIsSignificant(true)
    }

    fun parseArg(formula0: Formula): Map<String, Arg> {
        val map = HashMap<String, Arg>()
        var i = 0
        val f = IterableFormula(formula0.formula.form)
        while (!f.empty()) {
            val arg = f.car()
            if (arg != null && !arg.isEmpty()) {
                map.put(arg, Arg(false, false, i, 1))
            }
            f.pop()
            i++
        }
        return map
    }
}
