package cat.pingu.escalante.util

fun String.isInt() = try {
    Integer.valueOf(this)
    true
} catch (e: Throwable) {
    false
}