<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="https://image.flaticon.com/icons/svg/1998/1998693.svg" alt="Funktional Parser"></a>
</p>

<h3 align="center">Funktional Parser</h3>

---

<p align="center">
    This project is a Kotlin implementation of a functional parser, mainly made for practicing with development techniques and learning about new things.
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Usage](#usage)
- [Built Using](#built_using)
- [TODO](../TODO.md)
- [Contributing](../CONTRIBUTING.md)
- [Authors](#authors)
- [Acknowledgments](#acknowledgement)

## 🧐 About <a name = "about"></a>

This small project is an idea from @jmdesprez: implementing in Kotlin a functional parser as presented in [that Youtube video](https://youtu.be/dDtZLm7HIJs).

In the video, the presented parser is implemented in Haskell. Our goal with Jean-Marc was to implement a similar kind of parser in Kotlin.

The aim of the parser we're developing is actually fairly simple: it should allow to parse expressions such as `(1+3*(2+4)*2+4-(2*4))` and be able to calculate the actual result. The parser computation should also validate the expression by parsing only things which are allowed.

## 🏁 Getting Started <a name = "getting_started"></a>

If you'd like to discover what we did or play a bit with the source code as well, you can have a look at our test file located in `src/test/kotlin` to understand how we built our parser.

The actual implementation is located in `src/main/kotlin`.

Do not hesitate to have a look at each commit to understand the evolution of the parser. Each brick has been added in a dedicated commit.

### Prerequisites

We decided to stick with the basic Kotlin SDK, and KotlinTest for writing our unit tests.

### Installing

This project retrieves its dependencies from Maven. We developed it using IntelliJ IDEA. There's basically nothing much needed for running the project or contributing to it.

## 🔧 Running the tests <a name = "tests"></a>

Simply running `mvn test` should be sufficient to execute the unit tests.

## 🎈 Usage <a name="usage"></a>

We do not recommend to use the functions we developed in a production project since they're completely dedicated to our practice exercise.

But you can use our functions or code to write your own parser or practice yourself.

## 🚀 Deployment <a name = "deployment"></a>

The project won't be deployed anywhere, and we'll just share the source code here on GitHub.

## ⛏️ Built Using <a name = "built_using"></a>

- [Kotlin](https://kotlinlang.org/)
- [KotlinTest](https://github.com/kotest/kotest)

## ✍️ Authors <a name = "authors"></a>

- [@aneveux](https://github.com/aneveux)
- [@jmdesprez](https://github.com/jmdesprez)

## 🎉 Acknowledgements <a name = "acknowledgement"></a>

- [Professor Graham Hutton for his explanations and presentation](https://youtu.be/dDtZLm7HIJs)
