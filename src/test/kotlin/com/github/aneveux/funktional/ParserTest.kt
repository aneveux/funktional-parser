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

        "A char parser shouldn't parse an empty String" {
            val results = char('a')("")
            results.size shouldBeExactly 0
        }

        "A char parser shouldn't parse a String with no chars" {
            val results = char('a')("123")
            results.size shouldBeExactly 0
        }

        "A char parser should parse a String starting with the specified char" {
            val results = char('a')("abc")
            results.size shouldBeExactly 1
            results.first().parsed shouldBe 'a'
            results.first().unparsed shouldBe "bc"
        }

        "A char parser should allow multiple characters to be searched for" {
            val results = char('a', 'b')("bcd")
            results.size shouldBeExactly 1
            results.first().parsed shouldBe 'b'
            results.first().unparsed shouldBe "cd"
        }

        "A char parser shouldn't parse a String with the characters not being in the first position" {
            val results = char('a', 'b')("xabc")
            results.size shouldBeExactly 0
        }

        "A letter parser shouldn't parse an empty String" {
            val results = letter("")
            results.size shouldBeExactly 0
        }

        "A letter parser shouln't parse a String with no letters" {
            val results = letter("123")
            results.size shouldBeExactly 0
        }

        "A letter parser should parse a String starting with a letter" {
            val results = letter("abc")
            results.size shouldBeExactly 1
            results.first().parsed shouldBe 'a'
            results.first().unparsed shouldBe "bc"
        }

        "A letter parser shouldn't parse a String with a letter not in the first position" {
            val results = letter("1abc")
            results.size shouldBeExactly 0
        }

    }
}
