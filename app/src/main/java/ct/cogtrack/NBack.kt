package ct.cogtrack

import java.util.Random
import kotlin.math.max
import kotlin.math.sqrt


class Stat
{
    var count: Int = 0
    var sum: Double = 0.0
    var sum2: Double = 0.0

    val avg get() = sum / count
    val stdev get() = sqrt((sum2 - sum * sum / count) / (count - 1))

    operator fun plusAssign(x: Double): Unit
    {
        this.invoke(x)
    }
    // operator fun plus(x: Double) = this.invoke(x)

    operator fun invoke(x: Double): Stat
    {
        count++
        sum += x
        sum2 += x * x
        return this
    }
}

class RandomChar(val chars: String = "ABCDEF",
                 val rnd: Random = Random())
{

    fun next(): Char
    {
        return chars[rnd.nextInt(chars.length)]
    }
}

class CharSequence(val chars: String = "ABCDEF")
{
    var i = -1

    fun next(): Char
    {
        i = (i + 1) % chars.length
        return chars[i]
    }
}

// TODO: How to handle charGenerator properties? Seed?
// TODO: Probably want charGenerator with predetermined number of matches. 5/20 or so. Exakt same number of matches, just different chars and order.
class NBack(val nBack: Int = 2,
            val maxRounds: Int = 10,
            val charGenerator: () -> Char = RandomChar()::next)
// val charSet: String = "ABCDEF",
// val randInt: (Int)->Int) // KFunction1<@ParameterName(name = "callArg") Int, Int>
{

    private val stoppedReturnChar = ' '

    var correctMatch = 0
    var correctNoResponse = 0
    var wrongMatch = 0
    var wrongNoResponse = 0

    private var charCount = 0

    val round get() = charCount - nBack

    var isStopped = false
    var history = Array<Char>(nBack + 1, { ' ' })

    private var hasGuessed = true

    private var reactionStartTime: Long = 0

    // TODO: Reaction time should only apply to guesses (both correct/wrong).
    var reactionStat = Stat()
    val reactionTime get() = reactionStat.avg
    val reactionTimeStdev get() = reactionStat.stdev

    fun isAfterWarmup(): Boolean
    {
        return this.charCount > this.nBack
    }

    fun nextChar(): Char
    {

        if (!hasGuessed
                && this.isAfterWarmup()
                && !this.isStopped)
        {
            if (isMatch())
                wrongNoResponse++
            else correctNoResponse++

        }

        if (isFinished())
        {
            this.stop()
            return stoppedReturnChar
        }

        reactionStartTime = System.nanoTime()

        val char = charGenerator()
        history[charCount % history.size] = char
        charCount++

        hasGuessed = false
        return char
    }

    fun isMatch(): Boolean
    {
        if (charCount < history.size - 1)
            return false

        val cur = (charCount - 1) % history.size
        val prev = (cur + 1) % history.size
        return history[cur] == history[prev]
    }


    fun guessIsMatch(): Boolean
    {
        if (isStopped || !isAfterWarmup())
            return false

        if (!hasGuessed)
        {
            val dt = (System.nanoTime() - this.reactionStartTime) / 1e9
            this.reactionStat += dt

            if (isMatch())
                this.correctMatch++
            else
                this.wrongMatch++
        }

        hasGuessed = true
        return isMatch()
    }


    fun score(): Result
    {
        return arrayListOf(
                "nBack" to nBack.toDouble(),
                "rounds" to round.toDouble(),
                "correctMatch" to correctMatch.toDouble(),
                "correctNoResponse" to correctNoResponse.toDouble(),
                "wrongMatch" to wrongMatch.toDouble(),
                "wrongNoResponse" to wrongNoResponse.toDouble(),
                "reactionTime" to this.reactionStat.avg,
                "reactionTimeStdev" to this.reactionStat.stdev
        )
    }

    // Either stopped by user or max count.
    fun isFinished(): Boolean
    {
        return isStopped || (round == maxRounds)

    }

    // User has ended the game early.
    fun stop()
    {
        this.isStopped = true
    }


}
