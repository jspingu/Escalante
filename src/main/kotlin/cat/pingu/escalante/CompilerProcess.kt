package cat.pingu.escalante

import cat.pingu.escalante.error.ErrorHandler
import cat.pingu.escalante.parser.parse
import cat.pingu.escalante.tokenize.Tokenizer
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileNotFoundException

class CompilerProcess(inputName: String, private val outputName: String?): Runnable {
    private val input = File(inputName)

    override fun run() {
        if (!input.exists()) throw FileNotFoundException("Input file does not exist")

        val src = input.readText()
        ErrorHandler.src = src

        val tokenizer = Tokenizer(src)
        val tokens = tokenizer.getTokens()

        println(tokens)


        val gson = GsonBuilder().setPrettyPrinting().create()
        println(gson.toJson(parse(tokens)))
    }
}