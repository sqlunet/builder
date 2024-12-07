package org.sqlbuilder.common

import org.junit.Assert
import org.junit.Test
import java.io.File

class TestDeSerialize {

    @Test
    fun testDeserializeLegacy() {
        listOf(
            "../legacy/mappings/synsets30_synsets.ser",
            "../legacy/mappings/synsets30_synsets31.ser",
            "../legacy/sensekeys/senses30_sensekeys.ser",
        ).forEach {
            val obj: Any = DeSerialize.deserialize(File(it))
            println("$it ${obj.javaClass}")
            Assert.assertNotNull(obj)
        }
    }

    @Test
    fun testDeserializeBnc() {
        listOf(
            "../bnc/sers31/nid_words.ser",
            "../bnc/sers/nid_words.ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializeSn() {
        listOf(
            "../sn/sers/senses30_sensekeys.ser",
            "../sn/sers/sensekeys_words_synsets.ser",
            // "../sn/sers/nid_words.ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializeVn() {
        listOf(
            "../vn/sers/nid_words.ser",
            "../vn/sers/sensekeys_words_synsets.ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializePb() {
        listOf(
            "../pb/sers/nid_words.ser",
            "../pb/sers/nid_words.ser",
            //"../pb/sers/sensekeys_words_synsets.ser",

            "../pb/sers/vn/classes.resolve_[classtag]-[classid].ser",
            "../pb/sers/vn/roles.resolve_[classtag,roletype]-[roleid,classid,roletypeid].ser",

            "../pb/sers/fn/frames.resolve_[frame]-[frameid].ser",
            "../pb/sers/fn/fes.resolve_[frame,fetype]-[feid,frameid,fetypeid].ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializeSl() {
        listOf(
            //"../sl/sers/nid_words.ser",
            //"../sl/sers/sensekeys_words_synsets.ser",

            "../sl/sers/vn/classes.resolve_[classtag]-[classid].ser",
            "../sl/sers/vn/roles.resolve_[classtag,roletype]-[roleid,classid,roletypeid].ser",

            "../sl/sers/fn/frames.resolve_[frame]-[frameid].ser",
            "../sl/sers/fn/fes.resolve_[frame,fetype]-[feid,frameid,fetypeid].ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializeFn() {
        listOf(
            "../fn/sers/nid_words.ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializePm() {
        listOf(
            "../pm/sers/nid_words.ser",
            "../pm/sers/sensekeys_words_synsets.ser",

            "../pm/sers/vn/classes.resolve_[classtag]-[classid].ser",
            "../pm/sers/vn/roles.resolve_[classtag,roletype]-[roleid,classid,roletypeid].ser",
            "../pm/sers/vn/words.resolve_[word]-[vnwordid].ser",

            "../pm/sers/pb/rolesets.resolve_[roleset]-[rolesetid].ser",
            "../pm/sers/pb/roles.resolve_[roleset,argtype]-[roleid,rolesetid].ser",
            "../pm/sers/pb/words.resolve_[word]-[pbwordid].ser",

            "../pm/sers/fn/frames.resolve_[frame]-[frameid].ser",
            "../pm/sers/fn/fes.resolve_[frame,fetype]-[feid,frameid,fetypeid].ser",
            "../pm/sers/fn/lexunits.resolve_[frame,lexunit]-[luid,frameid].ser",
            "../pm/sers/fn/words.resolve_[word]-[fnwordid].ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    @Test
    fun testDeserializeSu() {
        listOf(
            "../su/sers/nid_words.ser",
            "../su/sers/synsets30_synsets31.ser",
            "../su/sers/synsets30_synsets31.ser",
        )
            .forEach {
                val obj: Any = DeSerialize.deserialize(File(it))
                println("$it ${obj.javaClass}")
                Assert.assertNotNull(obj)
            }
    }

    companion object {

        // @JvmStatic
        // @BeforeClass
        // fun init() {
        //     // empty
        // }
    }
}