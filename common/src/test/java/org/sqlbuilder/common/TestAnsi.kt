package org.sqlbuilder.common

import org.junit.Test
import org.sqlbuilder.common.AnsiColors.magenta
import org.sqlbuilder.common.AnsiColors.red
import org.sqlbuilder.common.AnsiColors.yellow
import org.sqlbuilder.common.AnsiColors.white
import org.sqlbuilder.common.AnsiColors.black
import org.sqlbuilder.common.AnsiColors.blink
import org.sqlbuilder.common.AnsiColors.blue
import org.sqlbuilder.common.AnsiColors.bold
import org.sqlbuilder.common.AnsiColors.cyan
import org.sqlbuilder.common.AnsiColors.green
import org.sqlbuilder.common.AnsiColors.greenb
import org.sqlbuilder.common.AnsiColors.grey
import org.sqlbuilder.common.AnsiColors.redb
import org.sqlbuilder.common.AnsiColors.blueb

class TestAnsi {


    @Test
    fun testAnsi() {
        println(white("white"))
        println(grey("grey"))
        println(black("black"))
        println(red("red"))
        println(blue("blue"))
        println(green("green"))
        println(magenta("magenta"))
        println(yellow("yellow"))
        println(cyan("cyan"))
        println(redb("RED"))
        println(greenb("GREEN"))
        println(blueb("BLUE"))
        println(bold("bold"))
        println(blink("blink"))
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            TestAnsi().testAnsi()
        }
    }
}
