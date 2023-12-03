package cat.pingu.escalante.parser.statements

import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.createSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object BinaryExpressionSyntax: Syntax<BinaryExpression>(createSyntax {
    then(LITERAL_INT)
    then(PLUS, MINUS, MULTIPLY, DIVIDE, MODULO)
    then(LITERAL_INT)
}) {
    override fun create(tokens: List<Token>) = BinaryExpression(
        tokens[0],
        tokens[1],
        tokens[2],
    )
}

class BinaryExpression(
    private val left: Token,
    private val operator: Token,
    private val right: Token,
): Statement()