package cat.pingu.escalante

import cat.pingu.escalante.error.CompilerError
import cat.pingu.escalante.lex.Lexer
import java.io.File

class CompilerProcess(inputName: String, private val outputName: String?): Runnable {
    private val input = File(inputName)

    override fun run() {
        if (!input.exists()) throw CompilerError("Input file does not exist")

        val src = input.readText()

        val lexer = Lexer(src)

        println(lexer.getTokens())
    }
}