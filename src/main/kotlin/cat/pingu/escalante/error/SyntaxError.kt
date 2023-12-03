package cat.pingu.escalante.error

class SyntaxError: CompilerError {
    constructor(message: String, line: Int): super(message, line)
    constructor(message: String): super(message)
}