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

