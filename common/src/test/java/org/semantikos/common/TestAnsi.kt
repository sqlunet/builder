package org.semantikos.common

import org.junit.Test
import org.semantikos.common.AnsiColors.magenta
import org.semantikos.common.AnsiColors.red
import org.semantikos.common.AnsiColors.yellow
import org.semantikos.common.AnsiColors.white
import org.semantikos.common.AnsiColors.black
import org.semantikos.common.AnsiColors.blink
import org.semantikos.common.AnsiColors.blue
import org.semantikos.common.AnsiColors.bold
import org.semantikos.common.AnsiColors.cyan
import org.semantikos.common.AnsiColors.green
import org.semantikos.common.AnsiColors.greenb
import org.semantikos.common.AnsiColors.grey
import org.semantikos.common.AnsiColors.redb
import org.semantikos.common.AnsiColors.blueb

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
