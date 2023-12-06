package cat.pingu.escalante.tokenize

import cat.pingu.escalante.util.isInt

class Tokenizer(private var src: String) {
    private val tokens = mutableListOf<Token>()

    fun getTokens(): List<Token> {
        val builder = StringBuilder()

        var inString = false
        var line = 1

        for (char in src.toCharArray()) {
            when {
                char == '"' && !inString -> inString = true
                char == '"' && inString -> {
                    tokens.add(Token(TokenType.LITERAL_STRING, builder.toString(), line))
                    builder.clear()
                    inString = false
                }
                inString -> builder.append(char)

                char == '+' -> parseCurrent(builder, char, TokenType.PLUS, line)
                char == '-' -> parseCurrent(builder, char, TokenType.MINUS, line)
                char == '*' -> parseCurrent(builder, char, TokenType.MULTIPLY, line)
                char == '/' -> parseCurrent(builder, char, TokenType.DIVIDE, line)
                char == '%' -> parseCurrent(builder, char, TokenType.MODULO, line)
                char == '=' -> parseCurrent(builder, char, TokenType.ASSIGNMENT, line)
                char == '.' -> parseCurrent(builder, char, TokenType.DOT, line)
                char == ',' -> parseCurrent(builder, char, TokenType.COMMA, line)
                char == '(' -> parseCurrent(builder, char, TokenType.OPAR, line)
                char == ')' -> parseCurrent(builder, char, TokenType.CPAR, line)
                char == '\n' -> {
                    parseCurrent(builder, char, TokenType.END, line)
                    line++
                }

                char.isWhitespace() -> parseString(builder, line)
                else -> builder.append(char)
            }
        }

        parseString(builder, line)

        return tokens
    }

    private fun parseCurrent(builder: StringBuilder, char: Char, type: TokenType, line: Int) {
        parseString(builder, line)
        tokens.add(Token(type, char.toString(), line))
    }

    private fun parseString(builder: StringBuilder, line: Int) {
        if (builder.isEmpty()) return

        val string = builder.toString().trim()
        val token = when {
            string == "int" -> Token(TokenType.KEYWORD_INT, string, line)
            string == "string" -> Token(TokenType.KEYWORD_STRING, string, line)
            string == "bool" -> Token(TokenType.KEYWORD_BOOL, string, line)
            string == "true" -> Token(TokenType.LITERAL_TRUE, string, line)
            string == "false" -> Token(TokenType.LITERAL_FALSE, string, line)
            string.isInt() -> Token(TokenType.LITERAL_INT, string, line)
            else -> Token(TokenType.OTHER, string, line)
        }

        tokens.add(token)

        builder.clear()
    }
}