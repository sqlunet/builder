package org.sqlbuilder.common

object AnsiColors {

    private const val ESC="\u001b"
    
    private const val BLACK="$ESC[30m"
    private const val RED="$ESC[31m"
    private const val GREEN="$ESC[32m"
    private const val BLUE="$ESC[34m"
    private const val YELLOW="$ESC[33m"
    private const val MAGENTA="$ESC[35m"
    private const val CYAN="$ESC[36m"
    private const val WHITE="$ESC[37m"

    private const val BG_BLACK="$ESC[40m"
    private const val BG_RED="$ESC[41m"
    private const val BG_GREEN="$ESC[42m"
    private const val BG_YELLOW="$ESC[43m"
    private const val BG_BLUE="$ESC[44m"
    private const val BG_MAGENTA="$ESC[45m"
    private const val BG_CYAN="$ESC[46m"
    private const val BG_WHITE="$ESC[47m"

    private const val LIGHT_BLACK="$ESC[90m"
    private const val LIGHT_RED="$ESC[91m"
    private const val LIGHT_GREEN="$ESC[92m"
    private const val LIGHT_YELLOW="$ESC[93m"
    private const val LIGHT_BLUE="$ESC[94m"
    private const val LIGHT_MAGENTA="$ESC[95m"
    private const val LIGHT_CYAN="$ESC[96m"
    private const val LIGHT_WHITE="$ESC[97m"

    private const val LIGHT_BG_BLACK="$ESC[100m"
    private const val LIGHT_BG_RED="$ESC[101m"
    private const val LIGHT_BG_GREEN="$ESC[102m"
    private const val LIGHT_BG_YELLOW="$ESC[103m"
    private const val LIGHT_BG_BLUE="$ESC[104m"
    private const val LIGHT_BG_MAGENTA="$ESC[105m"
    private const val LIGHT_BG_CYAN="$ESC[106m"
    private const val LIGHT_BG_WHITE="$ESC[107m"

    private const val BOLD="$ESC[1m"
    private const val STOP_BOLD="$ESC[21m"
    private const val UNDERLINE="$ESC[4m"
    private const val STOP_UNDERLINE="$ESC[24m"
    private const val BLINK="$ESC[5m"

    private const val RESET="$ESC[0m"

    private const val R=RED
    private const val G=GREEN
    private const val B=BLUE
    private const val Y=YELLOW
    private const val M=MAGENTA
    private const val C=CYAN
    private const val W=WHITE
    private const val K=BLACK

    private const val Rl=LIGHT_RED
    private const val Gl=LIGHT_GREEN
    private const val Bl=LIGHT_BLUE
    private const val Yl=LIGHT_YELLOW
    private const val Ml=LIGHT_MAGENTA
    private const val Cl=LIGHT_CYAN
    private const val Wl=LIGHT_WHITE

    private const val bR=BG_RED
    private const val bG=BG_GREEN
    private const val bB=BG_BLUE
    private const val bY=BG_YELLOW
    private const val bM=BG_MAGENTA
    private const val bC=BG_CYAN
    private const val bW=BG_WHITE

    private const val E=BOLD
    private const val ZE=STOP_BOLD

    private const val Z=RESET

    fun white(s: String): String{
        return "$Wl$s$Z"
    }

    fun grey(s: String): String{
        return "$W$s$Z"
    }

    fun black(s: String): String{
        return "$K$s$Z"
    }

    fun red(s: String): String{
        return "$R$s$Z"
    }

    fun green(s: String): String{
        return "$G$s$Z"
    }

    fun blue(s: String): String{
        return "$B$s$Z"
    }

    fun magenta(s: String): String{
        return "$M$s$Z"
    }

    fun yellow(s: String): String{
        return "$Y$s$Z"
    }

    fun cyan(s: String): String{
        return "$C$s$Z"
    }

    fun redb(s: String): String{
        return "$bR$Wl$s$Z"
    }

    fun greenb(s: String): String{
        return "$bG$Wl$s$Z"
    }

    fun blueb(s: String): String{
        return "$bB$Wl$s$Z"
    }

    fun bold(s: String): String{
        return "$BOLD$s$STOP_BOLD"
    }

    fun blink(s: String): String{
        return "$BLINK$s$Z"
    }
}