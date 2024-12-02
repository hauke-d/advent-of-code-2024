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
fun <T> List<String>.splitEachToPairs(transform: (String) -> T) : List<Pair<T, T>> = this.map {
    val values = it.split("\\s+".toRegex())
    transform(values[0]) to transform(values[1])
}

fun <T> List<String>.splitEach(transform: (String) -> T): List<List<T>> = this.map {
    it.split("\\s+".toRegex()).map(transform)
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
