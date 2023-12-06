package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.parseExpression
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

object FunctionCallSyntax: Syntax<FunctionCall>({
    then(TokenType.OTHER)
    then(TokenType.OPAR)
    and(matchingParenthesis(1) != -1)
}) {
    override fun create(tokens: List<Token>): FunctionCall {
        val arguments = mutableListOf<Expression>()
        val buffer = mutableListOf<Token>()

        for (i in 2..tokens.size - 2) {
            val token = tokens[i]

            if (token.type == TokenType.COMMA) {
                arguments.add(parseExpression(buffer))
            } else buffer.add(token)
        }

        if (buffer.isNotEmpty()) arguments.add(parseExpression(buffer))

        return FunctionCall(tokens[0], arguments)
    }
}

data class FunctionCall(private val name: Token, private val arguments: List<Expression>): Expression, Statement