package cat.pingu.escalante.parser

import cat.pingu.escalante.error.SyntaxError
import cat.pingu.escalante.parser.parsers.BinaryExpressionSyntax
import cat.pingu.escalante.parser.parsers.ConstantExpressionSyntax
import cat.pingu.escalante.parser.parsers.FunctionCallSyntax
import cat.pingu.escalante.parser.parsers.VariableDeclarationSyntax
import cat.pingu.escalante.tokenize.Token
import cat.pingu.escalante.tokenize.TokenType

val statementParsers = listOf(VariableDeclarationSyntax, FunctionCallSyntax)
val expressionParsers = listOf(BinaryExpressionSyntax, FunctionCallSyntax, ConstantExpressionSyntax)

fun parse(tokens: List<Token>): List<Statement> {
    val statements = mutableListOf<Statement>()

    val buffer = mutableListOf<Token>()

    for (token in tokens) {
        if (token.type == TokenType.END) {
            if (buffer.isEmpty()) continue

            statements.add(parseStatement(buffer))
            buffer.clear()
        } else buffer.add(token)
    }

    if (buffer.isNotEmpty()) statements.add(parseStatement(buffer))

    return statements
}

fun parseStatement(tokens: List<Token>) =
    parseBuffer(tokens, statementParsers) as? Statement
        ?: throw SyntaxError("Not a statement", tokens.first().line)

fun parseExpression(tokens: List<Token>, from: Int = 0, to: Int = tokens.size) =
    parseBuffer(tokens.subList(from, to), expressionParsers) as? Expression
        ?: throw SyntaxError("Not an expression", tokens.first().line)

fun parseBuffer(tokens: List<Token>, parsers: List<Syntax<out Parsed>>): Parsed {
    for (parser in parsers) {
        if (parser.matches(tokens)) {
            return parser.create(tokens)
        }
    }

    throw SyntaxError("Unable to parse '${tokens.joinToString(" ") { it.raw }}' with $parsers", tokens.first().line)
}
