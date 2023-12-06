package cat.pingu.escalante.parser

import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType
import cat.pingu.escalante.tokenize.TokenType.CPAR
import cat.pingu.escalante.tokenize.TokenType.OPAR

abstract class Syntax<T: Parsed>(private val check: Matcher.() -> Unit) {
    abstract fun create(tokens: List<Token>): T

    fun matches(tokens: List<Token>) = Matcher(tokens).apply(check).matches
}

//fun matcher(builder: MatchBuilder.(tokens: List<Token>) -> Unit) = MatchBuilder().apply(builder).syntax

class Matcher(private val tokens: List<Token>) {
    var matches = true
    private var index = 0

    fun any() {
        if (index++ >= tokens.size) {
            matches = false
        }
    }

    fun then(vararg types: TokenType) {
        if (index >= tokens.size || !types.contains(tokens[index++].type)) {
            matches = false
        }
    }

    fun and(boolean: Boolean) {
        if (!boolean) matches = false
    }

    fun matchingParenthesis(index: Int): Int {
        var depth = 0

        for (i in index-1..<tokens.size) {
            when (tokens[i].type) {
                OPAR -> depth++
                CPAR -> depth--
                else -> continue
            }

            if (depth == 0) return i
        }

        return -1
    }
}

interface Matchable {
    fun matches(tokens: List<Token>): Pair<Boolean, List<Token>>
}

class TokenTypeMatchable(private val types: List<TokenType>): Matchable {
    override fun matches(tokens: List<Token>): Pair<Boolean, List<Token>> {
        val matches = types.contains(tokens.firstOrNull()?.type)

        if (!matches) return Pair(false, tokens)
        return true to tokens.subList(1, tokens.size)
    }
}

class ParseMatchable(private val length: Int): Matchable {
    override fun matches(tokens: List<Token>): Pair<Boolean, List<Token>> {
        val matches = length == 0 || tokens.size >= length

        if (!matches) return Pair(false, tokens)

        return if (length == 0) Pair(true, listOf())
        else true to tokens.subList(length, tokens.size)
    }
}
