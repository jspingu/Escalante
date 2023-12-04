package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.createSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object ConstantExpressionSyntax: Syntax<ConstantExpression>(createSyntax {
    then(LITERAL_INT, LITERAL_STRING, LITERAL_TRUE, LITERAL_FALSE)
}) {
    override fun create(tokens: List<Token>) = ConstantExpression(tokens[0])
}

data class ConstantExpression(private val value: Token): Expression