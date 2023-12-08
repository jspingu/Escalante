package cat.pingu.escalante.gen.targets

import cat.pingu.escalante.gen.Compilable
import cat.pingu.escalante.parser.Expression
import cat.pingu.escalante.parser.Statement
import cat.pingu.escalante.parser.parsers.BinaryExpression
import cat.pingu.escalante.parser.parsers.ConstantExpression
import cat.pingu.escalante.parser.parsers.FunctionCall
import cat.pingu.escalante.parser.parsers.VariableDeclaration
import cat.pingu.escalante.tokenize.TokenType.*
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class JVM: Compilable {
    private val classNode = ClassNode().apply {
        visit(V1_8, ACC_PUBLIC, "Main", "", "java/lang/Object", arrayOf())
    }
    private val flow = classNode.visitMethod(ACC_PUBLIC or ACC_STATIC, "main", "([Ljava/lang/String;)V", null, arrayOf()) as MethodNode

    private var locals = 0

    override fun compile(output: File, ast: List<Statement>) {
        ast.forEach { visitStatement(it) }
        flow.visitInsn(RETURN)

        writeJar(output)
    }

    private fun visitStatement(statement: Statement) {
        when (statement) {
            is VariableDeclaration -> visitVariableDeclaration(statement)
            is FunctionCall -> visitFunctionCall(statement)
            else -> throw IllegalStateException()
        }
    }

    private fun visitExpression(expression: Expression) {
        when (expression) {
            is BinaryExpression -> visitBinaryExpression(expression)
            is ConstantExpression -> visitConstantExpression(expression)
            is FunctionCall -> visitFunctionCall(expression)
            else -> throw IllegalStateException()
        }
    }

    private fun visitVariableDeclaration(statement: VariableDeclaration) {
        visitExpression(statement.value)

        when (statement.type.type) {
            KEYWORD_INT, KEYWORD_BOOL -> flow.visitVarInsn(ISTORE, ++locals)
            KEYWORD_STRING -> flow.visitVarInsn(ASTORE, ++locals)
            else -> throw IllegalStateException()
        }
    }

    private fun visitBinaryExpression(expression: BinaryExpression) {
        visitExpression(expression.left)
        visitExpression(expression.right)

        when (expression.operator.type) {
            PLUS -> flow.visitInsn(IADD)
            MINUS -> flow.visitInsn(ISUB)
            MULTIPLY -> flow.visitInsn(IMUL)
            DIVIDE -> flow.visitInsn(IDIV)
            MODULO -> flow.visitInsn(IREM)
            else -> throw IllegalStateException()
        }
    }

    private fun visitConstantExpression(expression: ConstantExpression) {
        when (expression.value.type) {
            LITERAL_INT -> visitInt(expression.value.raw.toInt())
            LITERAL_STRING -> flow.visitLdcInsn(expression.value.raw)
            LITERAL_TRUE -> flow.visitInsn(ICONST_1)
            LITERAL_FALSE -> flow.visitInsn(ICONST_0)
            else -> throw IllegalStateException()
        }
    }

    private fun visitFunctionCall(call: FunctionCall) {
        for (argument in call.arguments) {
            visitExpression(argument)
        }

        when (call.name.raw) {
            "print", "println" -> {
                flow.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                flow.visitInsn(SWAP)
                flow.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", call.name.raw, "(Ljava/lang/Object;)V", false)
            }
        }
    }

    private fun writeJar(output: File) {
        val writer = ClassWriter(COMPUTE_MAXS or COMPUTE_FRAMES)
        classNode.accept(writer)

        val classBytes = writer.toByteArray()

        val manifestBytes = """
            Main-Class: ${classNode.name.replace("/", ".")}
            
        """.trimIndent().toByteArray()

        val stream = ZipOutputStream(FileOutputStream(output))

        val classEntry = ZipEntry("${classNode.name}.class")
        stream.putNextEntry(classEntry)
        stream.write(classBytes, 0, classBytes.size)
        stream.closeEntry()

        val manifestEntry = ZipEntry("META-INF/MANIFEST.MF")
        stream.putNextEntry(manifestEntry)
        stream.write(manifestBytes, 0, manifestBytes.size)
        stream.closeEntry()

        stream.close()
    }

    private fun visitInt(value: Int) {
        if (value >= -1 && value <= 5) flow.visitInsn(value + 3)
        else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) flow.visitIntInsn(BIPUSH, value)
        else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) flow.visitIntInsn(SIPUSH, value)
        else flow.visitLdcInsn(value)
    }
}
