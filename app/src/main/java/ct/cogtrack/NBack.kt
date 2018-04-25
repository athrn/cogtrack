package ct.cogtrack

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

class NBack(val nBack: Int = 2,
            val maxRounds: Int = 10,
            val charSet: String = "ABCDEF",
            val randInt: (Int)->Int) // KFunction1<@ParameterName(name = "callArg") Int, Int>
{

    // TODO: Rename to correct, wrongMatch, missedMatch or such
    var right = 0
    var wrong = 0
    var noResponse = 0
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
        // TODO: Reconsider end condition. How to signal end of game? Return space? Check finished? Should it be finished after last match?

        if(finished())
            return ' '

        reactionStartTime = System.nanoTime()

        var char = charSet[randInt(charSet.length)]
        history[rounds % history.size] = char
        rounds++

        if(isMatch() && !hasGuessed)
            noResponse++

        hasGuessed = false
        return char
    }

    fun isMatch(): Boolean {
        val cur = (rounds-1) % history.size
        val prev = (cur+1) % history.size
        return history[cur] == history[prev]
    }


    fun guessMatch(): Boolean
    {
        if (!hasGuessed) {
            val dt = (System.nanoTime() - this.reactionStartTime) / 1e9
            this.reactionStat += dt

            if(isMatch())
                this.right++
            else
                this.wrong++
        }


        if(finished())
            return false

        hasGuessed = true
        return isMatch()
    }

    fun finished(): Boolean {
        return isStopped || (hasGuessed && rounds == maxRounds)

    }

    fun score(): Result
    {
        return arrayListOf(
                "n" to (rounds.toDouble()),
                "right" to right.toDouble(),
                "wrong" to wrong.toDouble(),
                "no_response" to noResponse.toDouble(),
                "reaction_time" to this.reactionStat.avg,
                "reaction_time_stdev" to this.reactionStat.stdev
                )
    }

    fun stop() {
        // TODO: Handle non-counting of last character if ending early.
        this.isStopped = true
    }


}
