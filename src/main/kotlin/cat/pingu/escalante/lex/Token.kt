package cat.pingu.escalante.lex

data class Token(val type: TokenType, val raw: String)

enum class TokenType {
    KEYWORD_STRING,
    KEYWORD_INT,
    KEYWORD_BOOL,

    LITERAL_STRING,
    LITERAL_INT,
    LITERAL_BOOL,

    EQUALS,
    DOT,
    OPAR,
    CPAR,

    OTHER,
}
