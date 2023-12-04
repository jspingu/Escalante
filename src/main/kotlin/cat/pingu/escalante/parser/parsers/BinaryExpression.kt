package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.createSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object BinaryExpressionSyntax: Syntax<BinaryExpression>(createSyntax {
    statement(1) // TODO: parse this properly
    then(PLUS, MINUS, MULTIPLY, DIVIDE, MODULO)
    statement(1)
}) {
    override fun create(tokens: List<Token>) = BinaryExpression(
        tokens[0],
        tokens[1],
        tokens[2],
    )
}

data class BinaryExpression(
    private val left: Token,
    private val operator: Token,
    private val right: Token,
): Expression