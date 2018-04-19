package ct.cogtrack


class NBack(val n_rounds: Int = 10
            ) {

    var right = 0
    var wrong = 0
    var noResponse = 0
    var reactionSum= 0.0
    var reactionSum2= 0.0

    var i = 0
    var isStopped: Boolean = false

    fun nextChar(): Char {
        val chars = "ABCDEF"
        i++
        return chars[i % chars.length]
    }

    fun match()
    {
        // nextChar()
    }

    fun finished(): Boolean {
        return isStopped or (i > n_rounds)

    }

    fun score(): Result
    {
        return arrayListOf(
                "n" to ((i+1).toDouble()),
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