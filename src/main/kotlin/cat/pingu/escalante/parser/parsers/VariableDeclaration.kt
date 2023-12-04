package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.*
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object VariableDeclarationSyntax: Syntax<VariableDeclaration>(createSyntax {
    then(KEYWORD_STRING, KEYWORD_INT, KEYWORD_BOOL)
    then(OTHER)
    then(ASSIGNMENT)
    statement()
}) {
    override fun create(tokens: List<Token>) = VariableDeclaration(
        tokens[0],
        tokens[1],
        parseExpression(tokens, from = 3),
    )
}

data class VariableDeclaration(
    private val type: Token,
    private val name: Token,
    private val value: Expression,
): Statement