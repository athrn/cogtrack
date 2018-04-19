package ct.cogtrack
class NBack(val setChar: (Char)->Unit ) {

    var i = 0

    fun match(){
        nextChar()
    }

    fun nextChar(){
        val chars = "ABCDEF"
        this.setChar(chars[i])
        i = (i+1) % chars.length
    }
}