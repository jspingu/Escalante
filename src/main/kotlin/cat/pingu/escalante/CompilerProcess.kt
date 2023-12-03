package cat.pingu.escalante

import cat.pingu.escalante.error.ErrorHandler
import cat.pingu.escalante.lex.Lexer
import cat.pingu.escalante.parser.parse
import java.io.File
import java.io.FileNotFoundException

class CompilerProcess(inputName: String, private val outputName: String?): Runnable {
    private val input = File(inputName)

    override fun run() {
        if (!input.exists()) throw FileNotFoundException("Input file does not exist")

        val src = input.readText()
        ErrorHandler.src = src

        val lexer = Lexer(src)
        val tokens = lexer.getTokens()

        parse(tokens)
    }
}