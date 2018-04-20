package ct.cogtrack

import org.junit.Test

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MockRandom(val returnValues: List<Int>, val callArgs: List<Int> = emptyList()) {
    var i = 0

    fun nextInt(callArg: Int): Int {

        if(callArgs.isNotEmpty()) {
            assert(i < callArgs.size)
            assertEquals(callArgs[i], callArg)
        }

        // println("$i ${returnValues.size}")
        assert(i < returnValues.size)
        return returnValues[i++]
    }
}

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NBackTest {
    val mockRnd = MockRandom(listOf(0,1,2,3,4), listOf(5,5,5,5,5))

    @Test
    fun t1_MaxRounds() {
        val nback = NBack(
                maxRounds = 3,
                charSet="ABCDE",
                randInt = mockRnd::nextInt)

        assertFalse(nback.finished())
        assertEquals(0, nback.rounds)

        assertEquals('A', nback.nextChar())
        assertFalse(nback.finished())
        assertEquals(1, nback.rounds)

        assertEquals('B', nback.nextChar())
        assertFalse(nback.finished())
        assertEquals(2, nback.rounds)

        assertEquals('C', nback.nextChar())
        assertFalse(nback.finished())
        nback.hasGuessed = true
        assertTrue(nback.finished())
        assertEquals(3, nback.rounds)

        assertEquals(' ', nback.nextChar())
        assertTrue(nback.finished())
        assertEquals(3, nback.rounds)

    }

    @Test
    fun t2_RandomCharacter() {
        val rnd = MockRandom(listOf(0,2,1), callArgs = listOf(3,3,3))
        val nback = NBack(
                maxRounds = 3,
                charSet="ABC",
                randInt=rnd::nextInt)

        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())
        assertEquals('B', nback.nextChar())

    }


    @Test
    fun t4_History() {
        val nback = NBack(nBack=2,
                charSet="ABCDE",
                randInt=mockRnd::nextInt)

        assertEquals('A', nback.nextChar())
        assertEquals(listOf('A', ' ', ' '), nback.history.toList())
        assertEquals('B', nback.nextChar())
        assertEquals(listOf('A', 'B', ' '), nback.history.toList())
        assertEquals('C', nback.nextChar())
        assertEquals(listOf('A', 'B', 'C'), nback.history.toList())
        assertEquals('D', nback.nextChar())
        assertEquals(listOf('D', 'B', 'C'), nback.history.toList())
        assertEquals('E', nback.nextChar())
        assertEquals(listOf('D', 'E', 'C'), nback.history.toList())
    }

    @Test
    fun t5_Match2() {
        val rnd = MockRandom(listOf(0,2,0,2,2,1))
        val nback = NBack(nBack=2,
                maxRounds = 6,
                charSet="ABC",
                randInt=rnd::nextInt)

        assertEquals(0, nback.right)
        assertEquals(0, nback.wrong)
        assertEquals(0, nback.noResponse)

        assertEquals('A', nback.nextChar())
        assertEquals(false, nback.guessMatch())
        assertEquals(false, nback.guessMatch())
        assertEquals(0, nback.right)
        assertEquals(1, nback.wrong)
        assertEquals(0, nback.noResponse)

        assertEquals('C', nback.nextChar())
        assertEquals('A', nback.nextChar())
        assertEquals(true, nback.guessMatch())
        assertEquals(true, nback.guessMatch())
        assertEquals(1, nback.right)
        assertEquals(1, nback.wrong)
        assertEquals(1, nback.noResponse)

        assertEquals('C', nback.nextChar())
        assertEquals(true, nback.guessMatch())
        assertEquals('C', nback.nextChar())
        assertEquals(false, nback.guessMatch())
        assertEquals(2, nback.wrong)
        assertEquals('B', nback.nextChar())
        assertEquals(false, nback.guessMatch())

        assertEquals(2, nback.right)
        assertEquals(3, nback.wrong)
        assertEquals(1, nback.noResponse)

        assertEquals(' ', nback.nextChar())
        assertEquals(false, nback.guessMatch())

        assertEquals(2, nback.right)
        assertEquals(3, nback.wrong)
        assertEquals(1, nback.noResponse)

        assert(nback.reactionSum > 0)
        assert(nback.reactionSum2 > 0)
    }


}
