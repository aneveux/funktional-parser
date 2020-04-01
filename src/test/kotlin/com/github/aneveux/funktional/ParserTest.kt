package com.github.aneveux.funktional

import io.kotlintest.matchers.collections.shouldContainExactly
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

        "A some parser shouldn't run the provided parser if it doesn't match the input" {
            val results = some(digit)("abc")
            results.size shouldBeExactly 0
        }

        "A some parser should run the provided parser until it stops matching the input" {
            val results = some(digit)("123456a")
            results.size shouldBeExactly 1
            results.first().parsed shouldContainExactly listOf(1, 2, 3, 4, 5, 6)
            results.first().unparsed shouldBe "a"
        }

        "A some parser should run any provided parser on the input" {
            val results = some(letter)("abc123")
            results.size shouldBeExactly 1
            results.first().parsed shouldContainExactly listOf('a', 'b', 'c')
            results.first().unparsed shouldBe "123"
        }

        "An or parser should return the result of the first provided parser if it produces a result" {
            val results = or(digit, letter)("123abc")
            results.size shouldBeExactly 1
            results.first().parsed shouldBe 1
            results.first().unparsed shouldBe "23abc"
        }

        "An or parser should return the result of the second provided parser if the first one fails and not the second" {
            val results = or(digit, letter)("abc123")
            results.size shouldBeExactly 1
            results.first().parsed shouldBe 'a'
            results.first().unparsed shouldBe "bc123"
        }

        "An or parser shouldn't return a result if both provided parsers are not producing results" {
            val results = or(digit, letter)("!123abc")
            results.size shouldBeExactly 0
        }

        "An or parser could be used in combination with some parser to match a provided input" {
            val results = some(or(digit, letter))("123abc!")
            results.size shouldBeExactly 1
            results.first().let { (parsed, unparsed) ->
                parsed shouldContainExactly listOf(1, 2, 3, 'a', 'b', 'c')
                unparsed shouldBe "!"
            }
        }

    }
}
