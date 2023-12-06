package cat.pingu.escalante

import cat.pingu.escalante.error.ErrorHandler
import cat.pingu.escalante.gen.Target
import cat.pingu.escalante.parser.parse
import cat.pingu.escalante.tokenize.Tokenizer
import java.io.File
import java.io.FileNotFoundException

class CompilerProcess(
    inputName: String,
    outputName: String?,
    private val target: Target
): Runnable {
    private val input = File(inputName)
    private val output = File("${outputName ?: "$inputName-out"}.${target.fileType}")

    override fun run() {
        if (!input.exists()) throw FileNotFoundException("Input file does not exist")

        val src = input.readText()
        ErrorHandler.src = src

        val tokenizer = Tokenizer(src)
        val tokens = tokenizer.getTokens()
        val ast = parse(tokens)

        target.compiler.get().compile(output, ast)
    }
}