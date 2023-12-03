package cat.pingu.escalante.error

open class CompilerError: RuntimeException {
    final override var message = ""

    constructor(message: String, line: Int) {
        if (line < 0) {
            this.message = message
            return
        }

        val split = ErrorHandler.src.split("\n")
        val errorMessage = StringBuilder("\n")

        if (line == 1) errorMessage.append("      +---------->")
        else errorMessage.append("    ").append(line - 1).append(" | ").append(split[line - 2])

        errorMessage.append("\n>>> ").append(line).append(" | ").append(split[line - 1]).append("\n")

        if (line == split.size) errorMessage.append("      +---------->") else errorMessage.append("    ").append(line + 1).append(" | ").append(split[line])

        errorMessage.append("\n").append(message)

        this.message = errorMessage.toString()
    }

    constructor(message: String) {
        this.message = message
    }
}