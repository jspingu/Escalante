package cat.pingu.escalante.lex

import cat.pingu.escalante.util.isInt

class Lexer(private var src: String) {
    private val tokens = mutableListOf<Token>()

    fun getTokens(): List<Token> {
        val builder = StringBuilder()

        for (char in src.toCharArray()) {
            when {
                char == '=' -> parseCurrent(builder, char, TokenType.EQUALS)
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
            string.isInt() -> Token(TokenType.LITERAL_INT, string)
            else -> Token(TokenType.OTHER, string)
        }

        tokens.add(token)

        builder.clear()
    }
}