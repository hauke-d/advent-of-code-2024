import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Read pairs and transform values
 */
fun <T> List<String>.splitEachToPairs(separator: String = "\\s+", transform: (String) -> T) : List<Pair<T, T>> = this.map {
    val values = it.split(separator.toRegex())
    transform(values[0]) to transform(values[1])
}

fun <T> List<String>.splitEach(separator: String = "\\s+", transform: (String) -> T): List<List<T>> = this.map {
    it.split(separator.toRegex()).map(transform)
}

fun <T> List<String>.mapPositionsNotNull(transform: (Int, Int, Char) -> T?): List<T> {
    return this.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> transform(x, y, c) }
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first - other.first to second - other.second
}

operator fun Pair<Int, Int>.times(other: Int): Pair<Int, Int> {
    return first * other to second * other
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
