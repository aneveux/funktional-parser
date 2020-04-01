package com.github.aneveux.funktional

import io.kotlintest.matchers.numerics.shouldBeExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ParserTest : StringSpec() {
    init {

        "A digit parser shouln't parse an empty String" {
            val results = digit("")
            results.size shouldBeExactly 0
        }

        "A digit parser shouldn't parse a String with no digits" {
            val results = digit("abc")
            results.size shouldBeExactly 0
        }

        "A digit parser should parse a String starting with a digit" {
            val results = digit("123")
            results.size shouldBeExactly 1
            results.first().parsed shouldBeExactly 1
            results.first().unparsed shouldBe "23"
        }

        "A digit parser shouldn't parse a String with a digit not in the first position" {
            val results = digit("a1b")
            results.size shouldBeExactly 0
        }

    }
}
