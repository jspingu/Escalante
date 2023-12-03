package cat.pingu.escalante.lex

import cat.pingu.escalante.util.isInt

class Lexer(private var src: String) {
    private val tokens = mutableListOf<Token>()

    fun getTokens(): List<Token> {
        val builder = StringBuilder()

        var inString = false

        for (char in src.toCharArray()) {
            when {
                char == '"' && !inString -> inString = true
                char == '"' && inString -> {
                    tokens.add(Token(TokenType.LITERAL_STRING, builder.toString()))
                    builder.clear()
                    inString = false
                }
                inString -> builder.append(char)

                char == '+' -> parseCurrent(builder, char, TokenType.PLUS)
                char == '-' -> parseCurrent(builder, char, TokenType.MINUS)
                char == '*' -> parseCurrent(builder, char, TokenType.MULTIPLY)
                char == '/' -> parseCurrent(builder, char, TokenType.DIVIDE)
                char == '%' -> parseCurrent(builder, char, TokenType.MODULO)
                char == '=' -> parseCurrent(builder, char, TokenType.ASSIGNMENT)
                char == '.' -> parseCurrent(builder, char, TokenType.DOT)
                char == '(' -> parseCurrent(builder, char, TokenType.OPAR)
                char == ')' -> parseCurrent(builder, char, TokenType.CPAR)
                char == '\n' -> parseCurrent(builder, char, TokenType.END)

                char.isWhitespace() -> parseString(builder)
                else -> builder.append(char)
            }
        }

        parseString(builder)

        return tokens
    }

    private fun parseCurrent(builder: StringBuilder, char: Char, type: TokenType) {
        parseString(builder)
        tokens.add(Token(type, char.toString()))
    }

    private fun parseString(builder: StringBuilder) {
        if (builder.isEmpty()) return

        val string = builder.toString().trim()
        val token = when {
            string == "int" -> Token(TokenType.KEYWORD_INT, string)
            string == "string" -> Token(TokenType.KEYWORD_STRING, string)
            string == "bool" -> Token(TokenType.KEYWORD_BOOL, string)
            string == "true" -> Token(TokenType.LITERAL_TRUE, string)
            string == "false" -> Token(TokenType.LITERAL_FALSE, string)
            string.isInt() -> Token(TokenType.LITERAL_INT, string)
            else -> Token(TokenType.OTHER, string)
        }

        tokens.add(token)

        builder.clear()
    }
}