package cat.pingu.escalante.parser.statements

import cat.pingu.escalante.tokenize.TokenType.*
import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.createSyntax
import cat.pingu.escalante.parser.or

class VariableDeclaration: Statement(createSyntax {
    then(KEYWORD_STRING or KEYWORD_INT or KEYWORD_BOOL)
    then(OTHER)
    then(ASSIGNMENT)
    statement()
})