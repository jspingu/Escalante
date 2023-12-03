package cat.pingu.escalante.parser

import cat.pingu.escalante.error.SyntaxError
import cat.pingu.escalante.lex.Token
import cat.pingu.escalante.lex.TokenType
import cat.pingu.escalante.parser.statements.VariableDeclarationStatement

val parsers = listOf(VariableDeclarationStatement())

fun parse(tokens: List<Token>) {
    val buffer = mutableListOf<Token>()

    for (token in tokens) {
        if (token.type == TokenType.END) {
            parseBuffer(buffer)
            buffer.clear()
        } else buffer.add(token)
    }

    parseBuffer(buffer)
}

fun parseBuffer(buffer: List<Token>): Statement? {
    if (buffer.isEmpty()) return null

    for (parser in parsers) {
        if (parser.matches(buffer)) {
            return null
        }
    }

    throw SyntaxError("Unknown statement", buffer.first().line)
}
