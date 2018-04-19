package ct.cogtrack

typealias NamedValue = Pair<String, Double>
typealias Result = ArrayList<NamedValue>// HashMap<String, Double>

object ResultHolder {
    var result: Result = Result()
}

