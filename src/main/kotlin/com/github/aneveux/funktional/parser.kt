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
 *
 * We'll directly use some Kotlin sugar syntax to have the map function as an infix
 * extension of a Parser object, so we can call it this way: `parser map function`
 * instead of `map(parser,function)`.
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

// -------------------------------

/*
    This time, in addition with the functions we already have, we need a way
    to compose or aggregate parsers, so we can define high order parsers
    composed of lower level ones. For example, saying that a `hello` parser
    is the composition of `h,e,l,l,o` parsers for example.

    To do so, we simply need a function taking 2 parsers as parameters, and
    running them one after the other.

    Obviously, we could imagine lots of syntaxic sugar allowing to define
    suites of parsers to be executed, but as per now we'll limit ourselves to a
    simple composition of parsers.
 */

/**
 * This [then] function allows to run two parsers one after the other.
 * The trick here is to run the first parser, then to run the second parser on the
 * part which has not been parsed by the first parser, and then aggregating all the results
 * in a single object so we can return a proper result.
 */
infix fun <T> Parser<T>.then(secondParser: Parser<T>): Parser<List<T>> = { input ->
    this(input).flatMap { (first, intermediate) ->
        secondParser(intermediate).map { (second, unparsed) ->
            ParsingResult(listOf(first, second), unparsed)
        }
    }
}

// Using Kotlin sugar syntax to allow using the + symbol instead of calling then function
infix operator fun <T> Parser<T>.plus(secondParser: Parser<T>): Parser<List<T>> = this then secondParser

/**
 * This alternate [then] function allows to run multiple parsers one after the other by actually
 * flattening their results in the same result list instead of generating nested lists for each parser.
 * It works exactly the same way as the previous one, it'll just flatten the results.
 */
@JvmName("thenList")
infix fun <T> Parser<List<T>>.then(secondParser: Parser<T>): Parser<List<T>> = { input ->
    this(input).flatMap { (first, intermediate) ->
        secondParser(intermediate).map { (second, unparsed) ->
            ParsingResult(first + second, unparsed)
        }
    }
}

// Using Kotlin sugar syntax to allow using the + symbol instead of calling then function above
@JvmName("plusList")
infix operator fun <T> Parser<List<T>>.plus(secondParser: Parser<T>): Parser<List<T>> = this then secondParser

/**
 * An integer parser is a function taking a String as an input, and returning
 * an actual integer number if the parser matches the input.
 * The difference with the [natural] parser is that [integer] parser takes care of
 * the potential symbol we could find (+ or - or nothing).
 */
@Suppress("CAST_NEVER_SUCCEEDS") // Because we know our casts will actually succeed.
val integer: Parser<Int> = char('+', '-') + natural map { (sign, number) ->
    when (sign as Char) {
        '-' -> -(number as Int)
        // The else branch can only be the '+' character
        // Because our parser can't match anything else
        else -> (number as Int)
    }
} or natural
