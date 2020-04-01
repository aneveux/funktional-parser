package com.github.aneveux.funktional

/*
    Funktional Parser:

    Kotlin implementation of a functional parser,
    based on the presentation and explanations from the
    Professor Graham Hutton explanations in this Youtube
    video: https://youtu.be/dDtZLm7HIJs.

    The parser type declaration is made upon the professor's
    explanations of what a parser is and what it should return.
 */

data class ParsingResult<T>(val parsed: T, val unparsed: String) {
    override fun toString() = "(parsed=$parsed, unparsed=$unparsed)"
}

typealias Parser<T> = (String) -> List<ParsingResult<T>>

/*
    Implementations of the different parsers presented in the video.

    Remember (from the video) that a parser is a function, taking a String
    as an input, and producing a list of results. It is a list cause a parser
    could return different variations of the parsing.
 */

/**
 * A digit parser is a function receiving a String to be parsed.
 * Once called, the function should return the result of the parsing:
 * If the provided String starts with a digit, this digit should be parsed,
 * and the remaining part of the String should be left unparsed.
 * If the provided String doesn't start with a digit, the function
 * shouldn't return a result cause the String cannot be parsed.
 */
val digit: Parser<Int> = { input ->
    if (input.firstOrNull()?.isDigit() == true)
        listOf(ParsingResult(input.first().numericValue(), input.drop(1)))
    else
        listOf()
}
