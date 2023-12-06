package cat.pingu.escalante.gen

import cat.pingu.escalante.parser.Statement
import java.io.File
import java.util.function.Supplier
import cat.pingu.escalante.gen.targets.JVM as JVMTarget

interface Compilable {
    fun compile(output: File, ast: List<Statement>)
}

enum class Target(val fileType: String, val compiler: Supplier<Compilable>) {
    JVM("jar", Supplier { JVMTarget() })
}