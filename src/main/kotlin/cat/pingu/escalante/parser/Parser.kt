package cat.pingu.escalante.parser

import cat.pingu.escalante.error.SyntaxError
import cat.pingu.escalante.parser.statements.VariableDeclarationSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

val parsers = listOf(VariableDeclarationSyntax)

fun parse(tokens: List<Token>) {
    val buffer = mutableListOf<Token>()

    for (token in tokens) {
        if (token.type == TokenType.END) {
            if (buffer.isEmpty()) continue

            parseBuffer(buffer)
            buffer.clear()
        } else buffer.add(token)
    }

    parseBuffer(buffer)
}

fun parseBuffer(buffer: List<Token>): Statement {
    for (parser in parsers) {
        if (parser.matches(buffer)) {
            return parser.create(buffer)
        }
    }

    throw SyntaxError("Unknown statement", buffer.first().line)
}
