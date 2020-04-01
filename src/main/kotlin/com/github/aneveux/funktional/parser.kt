package com.github.aneveux.funktional

/*
    Funktional Parser:

    Kotlin implementation of a functional parser,
    based on the presentation and explanations from the
    Professor Graham Hutton explanations in this Youtube
    video: https://youtu.be/dDtZLm7HIJs.

    The parser type declaration is made upon the professor's
    explanations of what a parser is and what it should return.

    Here's one thing to remember what a parser is, shared by the
    professor:

    - A parser for things
    - Is a function from strings
    - To list of pairs
    - Of things and strings
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

/**
 * A char parser is a function receiving a String to be parsed, and a
 * list of chars to be matched from the parsing.
 * The function should return the result of the parsing:
 * If the providing String starts with one of the provided char, this char should
 * be parsed, and the remaining part of the String should be left unparsed.
 * If the provided String doesn't start with one of the provided char, the function
 * shouldn't return a result cause the String cannot be parsed.
 */
fun char(vararg characters: Char): Parser<Char> = { input ->
    if (input.isNotEmpty() && characters.contains(input.first()))
        listOf(ParsingResult(input.first(), input.drop(1)))
    else
        listOf()
}
