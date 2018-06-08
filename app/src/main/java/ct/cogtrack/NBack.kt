package ct.cogtrack

import java.util.*
import kotlin.math.sqrt


class Stat {
    var count: Int = 0
    var sum: Double = 0.0
    var sum2: Double = 0.0

    val avg get() = sum / count
    val stdev get() = sqrt((sum2 - sum*sum/count)/(count-1))

    operator fun plusAssign(x: Double): Unit { this.invoke(x) }
    // operator fun plus(x: Double) = this.invoke(x)

    operator fun invoke(x: Double): Stat
    {
        count++
        sum += x
        sum2 += x*x
        return this
    }
}

class RandomChar(val chars: String = "ABCDEF",
                 val rnd: Random = Random()) {

    fun next(): Char {
        return chars[rnd.nextInt(chars.length)]
    }
}

class CharSequence(val chars: String = "ABCDEF") {
    var i = -1

    fun next(): Char {
        i = (i + 1) % chars.length
        return chars[i]
    }
}

class NBack(val nBack: Int = 2,
            val maxRounds: Int = 10,
            val charGenerator: ()->Char)
            // val charSet: String = "ABCDEF",
            // val randInt: (Int)->Int) // KFunction1<@ParameterName(name = "callArg") Int, Int>
{

    // TODO: Rename to correct, wrongMatch, missedMatch or such
    var right = 0
    var wrongGuess = 0
    var wrongNoResponse = 0
//    var reactionSum= 0.0
//    var reactionSum2= 0.0

    var rounds = 0
    var isStopped = false
    var hasGuessed = true

    var history = Array<Char>(nBack + 1, { ' ' })

 //   private val reactionSum: Double
 //   private val reactionSum2: Double
    private var reactionStartTime: Long = 0

    var reactionStat = Stat()

    val reactionTime get() = reactionStat.avg

    val reactionTimeStdev get() = reactionStat.stdev

    fun nextChar(): Char {

        if(isFinished()) {
            this.stop()
            return ' '
        }

        reactionStartTime = System.nanoTime()

        if(isMatch() && !hasGuessed)
            wrongNoResponse++

        var char = charGenerator()
        history[rounds % history.size] = char
        rounds++

        hasGuessed = false
        return char
    }

    fun isMatch(): Boolean {
        if(rounds < history.size - 1)
            return false

        val cur = (rounds-1) % history.size
        val prev = (cur+1) % history.size
        return history[cur] == history[prev]
    }


    fun guessIsMatch(): Boolean
    {
        if(isStopped)
            return false

        if (!hasGuessed) {
            val dt = (System.nanoTime() - this.reactionStartTime) / 1e9
            this.reactionStat += dt

            if(isMatch())
                this.right++
            else
                this.wrongGuess++
        }

        hasGuessed = true
        return isMatch()
    }



    fun score(): Result
    {
        return arrayListOf(
                "rounds" to (rounds.toDouble()),
                "right" to right.toDouble(),
                "wrongGuess" to wrongGuess.toDouble(),
                "wrongNoResponse" to wrongNoResponse.toDouble(),
                "reactionTime" to this.reactionStat.avg,
                "reactionTimeStdev" to this.reactionStat.stdev
                )
    }

    // Either stopped by user or max count.
    fun isFinished(): Boolean {
        return isStopped || (rounds == maxRounds)

    }

    // User has ended the game early.
    fun stop() {
        // TODO: Handle non-counting of last character if ending early.
        this.isStopped = true
    }


}
