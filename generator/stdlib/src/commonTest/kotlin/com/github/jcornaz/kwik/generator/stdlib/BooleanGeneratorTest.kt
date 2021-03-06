package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.randomSequence
import com.github.jcornaz.kwik.generator.test.AbstractGeneratorTest
import kotlin.test.Test
import kotlin.test.assertTrue

class BooleanGeneratorTest : AbstractGeneratorTest() {
    override val generator: Generator<*> = Generator.booleans()

    @Test
    fun produceTrueAndFalse() {
        val values: Sequence<Boolean> = Generator.booleans().randomSequence(42).take(200)
        assertTrue(values.any { it })
        assertTrue(values.any { !it })
    }
}
