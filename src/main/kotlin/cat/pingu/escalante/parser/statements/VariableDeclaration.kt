package cat.pingu.escalante.parser.statements

import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.Syntax
import cat.pingu.escalante.parser.createSyntax
import cat.pingu.escalante.parser.parseStatement
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
        parseStatement(tokens, from = 3),
    )
}

class VariableDeclaration(
    private val type: Token,
    private val name: Token,
    private val value: Statement,
): Statement()