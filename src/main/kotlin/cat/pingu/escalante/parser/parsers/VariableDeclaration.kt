package cat.pingu.escalante.parser.parsers

import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.parseExpression
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType.*

object VariableDeclarationSyntax: Syntax<VariableDeclaration>({
    then(KEYWORD_STRING, KEYWORD_INT, KEYWORD_BOOL)
    then(OTHER)
    then(ASSIGNMENT)
    any()
}) {
    override fun create(tokens: List<Token>) = VariableDeclaration(
        tokens[0],
        tokens[1],
        parseExpression(tokens, from = 3),
    )
}

data class VariableDeclaration(
    val type: Token,
    val name: Token,
    val value: Expression,
): Statement