package cat.pingu.escalante.lex

data class Token(val type: TokenType, val raw: String)

enum class TokenType {
    KEYWORD_STRING,
    KEYWORD_INT,
    KEYWORD_BOOL,

    LITERAL_STRING,
    LITERAL_INT,
    LITERAL_TRUE,
    LITERAL_FALSE,

    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MODULO,

    ASSIGNMENT,
    DOT,
    OPAR,
    CPAR,

    END,
    OTHER,
}
