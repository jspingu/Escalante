package cat.pingu.escalante.parser

import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

abstract class Syntax<T: Parsed>(private val check: Matcher.() -> Unit) {
    abstract fun create(tokens: List<Token>): T

    fun matches(tokens: List<Token>) = Matcher(tokens).apply(check).matches
}

//fun matcher(builder: MatchBuilder.(tokens: List<Token>) -> Unit) = MatchBuilder().apply(builder).syntax

class Matcher(val tokens: List<Token>) {
    var matches = true
    private var index = 0
    val types get() = tokens.map { it.type }

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
