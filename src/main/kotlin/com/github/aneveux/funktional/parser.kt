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

data class ParsingResult<out T>(val parsed: T, val unparsed: String) {
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

/**
 * A letter parser is a function receiving a String to be parsed.
 * The function should return the result of the parsing:
 * If the providing String starts with a letter, this letter should
 * be parsed, and the remaining part of the String should be left unparsed.
 * If the provided String doesn't start with a letter, the function shouldn't
 * return a result cause the String cannot be parsed.
 */
val letter: Parser<Char> = { input ->
    if (input.firstOrNull()?.isLetter() == true)
        listOf(ParsingResult(input.first(), input.drop(1)))
    else
        listOf()
}

/**
 * A some parser is a function receiving another parser as well as a
 * String to be parsed.
 * The some parser will actually run the provided parser on the input
 * String as long as it matches, and provide the result of the n parsings
 * which have been done.
 * The some parser won't parse anything if the provided parser doesn't
 * produce any result at all.
 */
fun <T> some(parser: Parser<T>): Parser<List<T>> = { input ->
    fun acc(parsedItems: List<T>, unparsed: String): List<ParsingResult<List<T>>> =
        parser(unparsed).let { results ->
            if (results.isEmpty()) {
                if (parsedItems.isNotEmpty()) listOf(ParsingResult(parsedItems, unparsed))
                else listOf()
            } else results.flatMap { (parsed, unparsed) ->
                acc(parsedItems + parsed, unparsed)
            }
        }

    acc(listOf(), input)
}

/**
 * An or parser is a function taking two other parsers as parameters, as well
 * as a String to be parsed.
 * The or parser really is like an or-gate. If the first provided parser matches,
 * it'll return its result. Otherwise, if the second is a success, it'll return the
 * second result.
 * If none of the provided parsers are producing results, no results will be
 * returned by the or parser.
 *
 * Please note that our [ParsingResult] object has been modified a little bit to
 * allow a better way of working with types. Since it's possible to provide to the
 * or parser two parsers matching with different types (like letter and digit for example),
 * the result returned by the or parser should match with both types depending on which
 * parser is matching. To fix that, we'll simply specify that the type of a [ParsingResult]
 * is `out T` so the returned type of the or parser will be a type matching with both
 * types provided by the parsers to be tested.
 */
@JvmName("orGate")
fun <T> or(firstParser: Parser<T>, secondParser: Parser<T>): Parser<T> = { input ->
    firstParser(input).takeIf { it.isNotEmpty() } ?: secondParser(input)
}

//Using Kotlin syntaxic sugar to have a better way of writing or parsers
infix fun <T> Parser<T>.or(secondParser: Parser<T>): Parser<T> = or(this, secondParser)

// -------------------------------

/*
    At this point in the development, we'll need a bit more than the functions we
    already defined. We need to be able to define parsers with some actual behavior
    added to them. That behavior cannot be completely generic, and we need to
    be able to complete our parsers with a way to interpret the results.

    This can be done simply by providing to a parser a simple function allowing to
    interpret its results.
 */

/**
 * This [map] function allows (exactly as the traditional map function) to apply a
 * provided function to the result of a parser.
 * The small things to take into account here are that a parser returns a list of results,
 * so the provided function needs to be executed on each result.
 * Also, the function only transforms the parsed results, and not the unparsed parts.
 */
infix fun <T, U> Parser<T>.map(function: (T) -> U): Parser<U> = { input ->
    this(input).map { (parsed, unparsed) -> ParsingResult(function(parsed), unparsed) }
}

/**
 * A natural parser is a function taking a String as an input, and parsing it searching
 * for a natural number. The main difference with searching for `some(digits)` is that
 * this time we actually want to return a number, and not a list of digits.
 * This requires us to use existing parsers to find the digits of the natural number we
 * are searching for, and then to interpret the result to convert it to an actual number.
 */
val natural: Parser<Int> = some(digit).map { digits -> digits.joinToString(separator = "").toInt() }
