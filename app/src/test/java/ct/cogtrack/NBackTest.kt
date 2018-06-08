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


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NBackTest {

    @Test
    fun t1_MaxRounds() {
        val nback = NBack(maxRounds = 3,
                charGenerator=CharSequence("ABCDE")::next)

        assertEquals(0, nback.rounds)
        assertFalse(nback.isFinished())

        assertEquals('A', nback.nextChar())
        assertEquals(1, nback.rounds)
        assertFalse(nback.isFinished())

        assertEquals('B', nback.nextChar())
        assertFalse(nback.isFinished())
        assertEquals(2, nback.rounds)

        assertEquals('C', nback.nextChar())
        assertTrue(nback.isFinished())
        assertEquals(3, nback.rounds)

        assertEquals(' ', nback.nextChar())
        assertTrue(nback.isFinished())
        assertEquals(3, nback.rounds)

    }

    @Test
    fun t2_NextChar() {
        //val rnd = MockRandom(listOf(0,2,1), callArgs = listOf(3,3,3))
        val nback = NBack(maxRounds = 3,
                charGenerator=CharSequence("ACB")::next)

        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())
        assertEquals('B', nback.nextChar())

    }

    @Test
    fun t21_isMatch() {
        val nback = NBack(maxRounds = 10,
                charGenerator=CharSequence("XAXBXCDXEX")::next)
        assertEquals('X', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('A', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('X', nback.nextChar())
        assertEquals(true, nback.isMatch())
        assertEquals('B', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('X', nback.nextChar())
        assertEquals(true, nback.isMatch())
        assertEquals('C', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('D', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('X', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('E', nback.nextChar())
        assertEquals(false, nback.isMatch())
        assertEquals('X', nback.nextChar())
        assertEquals(true, nback.isMatch())
        assertEquals(' ', nback.nextChar())
    }


    @Test
    fun t4_History() {
        val nback = NBack(nBack=2,
                charGenerator=CharSequence("ABCDE")::next)

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
    fun t5_guessIsMatchAndStats() {
        val nback = NBack(nBack=2,
                maxRounds=7,
                charGenerator=CharSequence("ACACCCD")::next)

        assertEquals(0, nback.right)
        assertEquals(0, nback.wrongGuess)
        assertEquals(0, nback.wrongNoResponse)

        assertEquals('A', nback.nextChar())
        assertEquals(false, nback.guessIsMatch())
        assertEquals(false, nback.guessIsMatch())
        assertEquals(0, nback.right)
        assertEquals(1, nback.wrongGuess)
        assertEquals(0, nback.wrongNoResponse)

        assertEquals('C', nback.nextChar())
        assertEquals('A', nback.nextChar())
        assertEquals(true, nback.guessIsMatch())
        assertEquals(true, nback.guessIsMatch())
        assertEquals(1, nback.right)
        assertEquals(1, nback.wrongGuess)
        assertEquals(0, nback.wrongNoResponse)

        assertEquals('C', nback.nextChar())
        assertEquals(true, nback.guessIsMatch())
        assertEquals(2, nback.right)
        assertEquals('C', nback.nextChar())
        assertEquals(false, nback.guessIsMatch())
        assertEquals(2, nback.wrongGuess)
        assertEquals('C', nback.nextChar())
        assertEquals(0, nback.wrongNoResponse)
        assertEquals('D', nback.nextChar())
        assertEquals(1, nback.wrongNoResponse)


        assertEquals(2, nback.right)
        assertEquals(2, nback.wrongGuess)
        assertEquals(1, nback.wrongNoResponse)

        assertEquals(' ', nback.nextChar())
        assertEquals(false, nback.guessIsMatch())

        assertEquals(2, nback.right)
        assertEquals(2, nback.wrongGuess)
        assertEquals(1, nback.wrongNoResponse)

        assert(nback.reactionTime > 0)
        assert(nback.reactionTimeStdev > 0)
    }

    @Test
    fun t6_MatchOnFirst() {
        val nback = NBack(nBack=2,
                maxRounds=6,
                charGenerator=CharSequence("XAXCB")::next)

        assertEquals(0, nback.wrongNoResponse)
        assertEquals('X', nback.nextChar())
        assertEquals('A', nback.nextChar())
        assertEquals('X', nback.nextChar())
        assertEquals(0, nback.wrongNoResponse)
        assertEquals('C', nback.nextChar())
        assertEquals(1, nback.wrongNoResponse)
        assertEquals('B', nback.nextChar())
        assertEquals(1, nback.wrongNoResponse)
    }


}
