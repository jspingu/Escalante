package cat.pingu.escalante.parser

import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

abstract class Syntax<T: Parsed>(private val syntax: MutableList<Matchable>) {
    fun matches(tokens: List<Token>): Boolean {
        var clone = tokens.toList()

        for (matchable in syntax) {
            val (match, new) = matchable.matches(clone)
            if (!match) return false

            clone = new
        }

        return clone.isEmpty()
    }

    abstract fun create(tokens: List<Token>): T
}

fun createSyntax(builder: SyntaxBuilder.() -> Unit) = SyntaxBuilder().apply(builder).syntax

class SyntaxBuilder {
    val syntax = mutableListOf<Matchable>()

    fun then(vararg types: TokenType) = syntax.add(TokenTypeMatchable(types.toList()))
    fun statement(length: Int = 0) = syntax.add(StatementMatchable(length))
}

interface Matchable {
    fun matches(tokens: List<Token>): Pair<Boolean, List<Token>>
}

class TokenTypeMatchable(private val types: List<TokenType>): Matchable {
    override fun matches(tokens: List<Token>): Pair<Boolean, List<Token>> {
        val matches = types.contains(tokens.firstOrNull()?.type)

        if (!matches) return Pair(false, tokens)
        return Pair(true, tokens.subList(1, tokens.size))
    }
}

class StatementMatchable(private val length: Int): Matchable {
    override fun matches(tokens: List<Token>): Pair<Boolean, List<Token>> {
        val matches = length == 0 || tokens.size >= length

        if (!matches) return Pair(false, tokens)

        return if (length == 0) Pair(true, listOf())
        else Pair(true, tokens.subList(length, tokens.size))
    }
}
