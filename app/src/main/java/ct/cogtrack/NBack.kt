package ct.cogtrack

import kotlin.math.max


class NBack(val nBack: Int = 2,
            val maxRounds: Int = 10,
            val charSet: String = "ABCDEF",
            val randInt: (Int)->Int) // KFunction1<@ParameterName(name = "callArg") Int, Int>
{

    // TODO: Rename to correct, wrongMatch, missedMatch or such
    var right = 0
    var wrong = 0
    var noResponse = 0
    var reactionSum= 0.0
    var reactionSum2= 0.0

    var rounds = 0
    var isStopped = false
    var hasGuessed = true

    var history = Array<Char>(nBack + 1, { ' ' })

    fun nextChar(): Char {
        // TODO: Reconsider end condition. How to signal end of game? Return space? Check finished? Should it be finished after last match?

        if(finished())
            return ' '


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
        if(!hasGuessed)
            if(isMatch())
                this.right++
            else
                this.wrong++

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
                "reaction_time_avg" to 0.0,
                "reaction_time_stdev" to 0.0
                )
    }

    fun stop() {
        // TODO: Handle non-counting of last character if ending early.
        this.isStopped = true
    }


}
