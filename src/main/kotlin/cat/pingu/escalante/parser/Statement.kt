package cat.pingu.escalante.parser

import cat.pingu.escalante.tokenize.Token

abstract class Statement(private val syntax: List<Matchable>) {
    fun matches(tokens: List<Token>): Boolean {
        var clone = tokens.toList()

        for (matchable in syntax) {
            val (match, new) = matchable.matches(clone)

            if (!match) return false
            clone = new
        }

        return clone.isEmpty()
    }
}