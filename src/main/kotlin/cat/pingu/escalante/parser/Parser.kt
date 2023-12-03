package cat.pingu.escalante.parser

import cat.pingu.escalante.error.SyntaxError
import cat.pingu.escalante.parser.statements.BinaryExpressionSyntax
import cat.pingu.escalante.parser.statements.VariableDeclarationSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

val parsers = listOf(VariableDeclarationSyntax, BinaryExpressionSyntax)

fun parse(tokens: List<Token>): List<Statement> {
    val statements = mutableListOf<Statement>()

    val buffer = mutableListOf<Token>()

    for (token in tokens) {
        if (token.type == TokenType.END) {
            if (buffer.isEmpty()) continue

            statements.add(parseBuffer(buffer))
            buffer.clear()
        } else buffer.add(token)
    }

    if (buffer.isNotEmpty()) statements.add(parseBuffer(buffer))

    return statements
}

fun parseBuffer(buffer: List<Token>): Statement {
    for (parser in parsers) {
        if (parser.matches(buffer)) {
            return parser.create(buffer)
        }
    }

    throw SyntaxError("Unknown statement", buffer.first().line)
}
