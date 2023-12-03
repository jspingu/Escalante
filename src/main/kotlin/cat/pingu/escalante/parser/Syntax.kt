package cat.pingu.escalante.parser

import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

//annotation class Syntax(val syntax: Array<Matchable>)

infix fun TokenType.or(type: TokenType) = listOf(this, type)
infix fun List<TokenType>.or(type: TokenType) = this + type

fun createSyntax(builder: StatementSyntax.() -> Unit) = StatementSyntax().apply(builder).syntax

class StatementSyntax {
    val syntax = mutableListOf<Matchable>()

    fun then(type: TokenType) = syntax.add(TokenTypeMatchable(type))
    fun then(types: List<TokenType>) = syntax.add(TokenTypeListMatchable(types))
    fun statement(length: Int = 0) = syntax.add(StatementMatchable(length))
}

interface Matchable {
    fun matches(tokens: List<Token>): Pair<Boolean, List<Token>>
}

class TokenTypeMatchable(private val type: TokenType): Matchable {
    override fun matches(tokens: List<Token>): Pair<Boolean, List<Token>> {
        val matches = tokens.firstOrNull()?.type == type

        if (!matches) return Pair(false, tokens)
        return Pair(true, tokens.subList(1, tokens.size))
    }
}

class TokenTypeListMatchable(private val types: List<TokenType>): Matchable {
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
