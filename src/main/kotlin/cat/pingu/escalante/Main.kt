package cat.pingu.escalante

import cat.pingu.escalante.gen.Target
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
    val parser = ArgParser("escalante")

    val input by parser.option(ArgType.String, shortName = "i", fullName = "input", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", fullName = "output", description = "Output file")
    val target by parser.option(ArgType.Choice<Target>(), shortName = "t", fullName = "target", description = "Compile target").required()

    parser.parse(args)

    CompilerProcess(input, output, target).run()
}