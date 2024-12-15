package org.semantikos.fn

import org.junit.Test

class TestNormalizer {

    @Test
    fun normalize1() {
        println("\n-- normalize ferealizations (feid)")
        Normalizer("Fn_fes", "fetypeid", "feid")
            .targets("Fn_ferealizations", "fetypeid", "feid")
            .referenceThrough("Fn_lexunits", "luid", "Fn_frames", "frameid")
            .dump()
    }

    @Test
    fun normalize2() {
        println("\n-- normalize fegrouprealizations (feid)")
        Normalizer("Fn_fes", "fetypeid", "feid")
            .targets("Fn_fegrouprealizations_fes", "fetypeid", "feid")
            .referenceThrough("Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid", "Fn_frames", "frameid")
            .dump()
    }

    @Test
    fun normalize3() {
        println("\n-- normalize lexunits (incorporatedfeid)")
        Normalizer("Fn_fes", "fetypeid", "feid")
            .targets("Fn_lexunits", "incorporatedfetypeid", "incorporatedfeid")
            .referenceThrough("Fn_frames", "frameid")
            .dump()
    }

    @Test
    fun normalize4() {
        println("\n-- normalize grouppatterns_patterns (feid)")
        Normalizer("Fn_fes", "fetypeid", "feid")
            .targets("Fn_grouppatterns_patterns", "fetypeid", "feid")
            .referenceThrough("Fn_patterns", "patternid", "Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid")
            .dump()
    }
}
