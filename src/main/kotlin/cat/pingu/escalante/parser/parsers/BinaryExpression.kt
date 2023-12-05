package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.parseExpression
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object BinaryExpressionSyntax: Syntax<BinaryExpression>({
    any()
    then(PLUS, MINUS, MULTIPLY, DIVIDE, MODULO)
    any()
}) {
    private val order = listOf(
        listOf(PLUS, MINUS),
        listOf(MULTIPLY, DIVIDE, MODULO),
    )

    override fun create(tokens: List<Token>): BinaryExpression {
        val types = tokens.map { it.type }

        var operationIndex = -1
        loop@ for (operations in order) {
            for ((index, type) in types.withIndex().reversed()) {
                if (!operations.contains(type)) continue

                operationIndex = index
                break@loop
            }
        }

        return BinaryExpression(
            parseExpression(tokens, to = operationIndex),
            tokens[operationIndex],
            parseExpression(tokens, from = operationIndex + 1),
        )
    }
}

data class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression,
): Expression