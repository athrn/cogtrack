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


fun assertScore(nback: NBack,
                correctMatch: Int,
                correctNoResponse: Int,
                wrongMatch: Int,
                wrongNoResponse: Int)
{
    assertEquals("correctMatch", correctMatch, nback.correctMatch)
    assertEquals("correctNoResponse", correctNoResponse, nback.correctNoResponse)
    assertEquals("wrongMatch", wrongMatch, nback.wrongMatch)
    assertEquals("wrongNoResponse", wrongNoResponse, nback.wrongNoResponse)
}

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NBackTest
{

    @Test
    fun t1_MaxRounds()
    {
        val nback = NBack(maxRounds = 1,
                nBack = 2,
                charGenerator = CharSequence("ABCDE")::next)

        assertEquals(-2, nback.round)
        assertFalse(nback.isFinished())

        assertEquals('A', nback.nextChar())
        assertEquals(-1, nback.round)
        assertFalse(nback.isFinished())

        assertEquals('B', nback.nextChar())
        assertFalse(nback.isFinished())
        assertEquals(0, nback.round)

        assertEquals('C', nback.nextChar())
        assertTrue(nback.isFinished())
        assertEquals(1, nback.round)

        assertEquals(' ', nback.nextChar())
        assertTrue(nback.isFinished())
        assertEquals(1, nback.round)

    }

    @Test
    fun t2_NextChar()
    {
        //val rnd = MockRandom(listOf(0,2,1), callArgs = listOf(3,3,3))
        val nback = NBack(maxRounds = 3,
                charGenerator = CharSequence("ACB")::next)

        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())
        assertEquals('B', nback.nextChar())

    }

    @Test
    fun t21_isMatch()
    {
        val nback = NBack(maxRounds = 8,
                charGenerator = CharSequence("XAXBXCDXEX")::next)
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
    fun t4_History()
    {
        val nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABCDE")::next)

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
    fun t41_maxRounds()
    {
        var nback = NBack(maxRounds = 1, nBack = 1)
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertEquals(' ', nback.nextChar())

        nback = NBack(maxRounds = 2, nBack = 1)
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertEquals(' ', nback.nextChar())

        nback = NBack(maxRounds = 2, nBack = 2)
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertFalse(' ' == nback.nextChar())
        assertEquals(nback.round, 2)
        assertEquals(' ', nback.nextChar())

    }

    @Test
    fun t51_noScoringDuringWarmup()
    {
        var nback = NBack(nBack = 1,
                charGenerator = CharSequence("ABCDE")::next)

        assertScore(nback, 0, 0, 0, 0)
        nback.nextChar()
        assertScore(nback, 0, 0, 0, 0)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 0, 0)

        nback.nextChar()
        assertScore(nback, 0, 0, 0, 0)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 1, 0)

        nback = NBack(nBack = 2,
                charGenerator = CharSequence("XBXDE")::next)

        assertScore(nback, 0, 0, 0, 0)
        nback.nextChar()
        assertScore(nback, 0, 0, 0, 0)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 0, 0)

        nback.nextChar()
        assertScore(nback, 0, 0, 0, 0)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 0, 0)

        nback.nextChar()
        assertScore(nback, 0, 0, 0, 0)
        nback.guessIsMatch()
        assertScore(nback, 1, 0, 0, 0)


    }

    @Test
    fun t52_correctCount()
    {
        val nback = NBack(nBack = 2,
                maxRounds = 3,
                charGenerator = CharSequence("ACADE")::next)

        assertScore(nback, 0, 0, 0, 0)
        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())

        assertEquals('A', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals(true, nback.guessIsMatch())
        assertScore(nback, 1, 0, 0, 0)

        assertEquals('D', nback.nextChar())
        assertScore(nback, 1, 0, 0, 0)

        assertEquals('E', nback.nextChar())
        assertScore(nback, 1, 1, 0, 0)

        assertEquals(' ', nback.nextChar())
        assertScore(nback, 1, 2, 0, 0)
    }

    @Test
    fun t53_wrongCount()
    {
        val nback = NBack(nBack = 2,
                maxRounds = 4,
                charGenerator = CharSequence("ACADED")::next)

        assertScore(nback, 0, 0, 0, 0)
        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())

        assertEquals('A', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)

        assertEquals('D', nback.nextChar())
        assertScore(nback, 0, 0, 0, 1)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 1, 1)
        // Allow double guessing
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 1, 1)

        assertEquals('E', nback.nextChar())
        assertScore(nback, 0, 0, 1, 1)
        nback.guessIsMatch()
        assertScore(nback, 0, 0, 2, 1)

        assertEquals('D', nback.nextChar())
        assertScore(nback, 0, 0, 2, 1)

        assertEquals(' ', nback.nextChar())
        assertScore(nback, 0, 0, 2, 2)
    }

    @Test
    fun t54_guessIsMatchAndStats()
    {
        val nback = NBack(nBack = 2,
                maxRounds = 6,
                charGenerator = CharSequence("ACACCCDE")::next)

        assertScore(nback, 0, 0, 0, 0)

        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())

        assertEquals('A', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals(true, nback.guessIsMatch())
        assertScore(nback, 1, 0, 0, 0)

        assertEquals('C', nback.nextChar())
        assertScore(nback, 1, 0, 0, 0)
        assertEquals(true, nback.guessIsMatch())
        assertScore(nback, 2, 0, 0, 0)

        assertEquals('C', nback.nextChar())
        assertScore(nback, 2, 0, 0, 0)
        assertEquals(false, nback.guessIsMatch())
        assertScore(nback, 2, 0, 1, 0)

        assertEquals('C', nback.nextChar())
        assertScore(nback, 2, 0, 1, 0)

        assertEquals('D', nback.nextChar())
        assertScore(nback, 2, 0, 1, 1)

        assertEquals('E', nback.nextChar())
        assertScore(nback, 2, 1, 1, 1)

        assertEquals(' ', nback.nextChar())
        assertEquals(false, nback.guessIsMatch())

        assertScore(nback, 2, 2, 1, 1)

        assertEquals(nback.round, nback.correctMatch + nback.correctNoResponse + nback.wrongMatch + nback.wrongNoResponse)

        assert(nback.reactionTime > 0)
        assert(nback.reactionTimeStdev > 0)
    }

    @Test
    fun t6_stop()
    {
        // TODO: Round is incorrectly calculated if stopped. Does not equal correct + wrong

        var nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABACD")::next)
        assertEquals('A', nback.nextChar())
        assertEquals('B', nback.nextChar())
        nback.stop()
        assertScore(nback, 0, 0, 0, 0)

        // Action counts to score, inaction does not
        nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABACD")::next)
        assertEquals('A', nback.nextChar())
        assertEquals('B', nback.nextChar())
        assertEquals('A', nback.nextChar())
        nback.stop()
        assertScore(nback, 0, 0, 0, 0)

        nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABACD")::next)
        assertEquals('A', nback.nextChar())
        assertEquals('B', nback.nextChar())
        assertEquals('A', nback.nextChar())
        nback.guessIsMatch()
        nback.stop()
        assertScore(nback, 1, 0, 0, 0)

        // Continued action does nothing
        assertEquals(false, nback.guessIsMatch())
        assertEquals(' ', nback.nextChar())
        assertScore(nback, 1, 0, 0, 0)

        nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABACD")::next)
        assertEquals('A', nback.nextChar())
        assertEquals('B', nback.nextChar())
        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())
        nback.stop()
        assertScore(nback, 0, 0, 0, 1)

        nback = NBack(nBack = 2,
                charGenerator = CharSequence("ABACD")::next)
        assertEquals('A', nback.nextChar())
        assertEquals('B', nback.nextChar())
        assertEquals('A', nback.nextChar())
        assertEquals('C', nback.nextChar())
        nback.guessIsMatch()
        nback.stop()
        assertScore(nback, 0, 0, 1, 1)

    }


    @Test
    fun t7_nback()
    {
        var nback = NBack(nBack = 1,
                maxRounds = 5,
                charGenerator = CharSequence("CCCCDE")::next)
        assertEquals(-1, nback.round)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals(1, nback.round)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 1)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 2)
        assertEquals('D', nback.nextChar())
        assertScore(nback, 0, 0, 0, 3)
        assertEquals('E', nback.nextChar())
        assertScore(nback, 0, 1, 0, 3)
        assertEquals(5, nback.round)
        assertEquals(' ', nback.nextChar())
        assertScore(nback, 0, 2, 0, 3)
        assertEquals(5, nback.round)

        nback = NBack(nBack = 2,
                maxRounds = 4,
                charGenerator = CharSequence("CCCCDE")::next)


        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 1)
        assertEquals('D', nback.nextChar())
        assertScore(nback, 0, 0, 0, 2)
        assertEquals('E', nback.nextChar())
        assertScore(nback, 0, 1, 0, 2)
        assertEquals(' ', nback.nextChar())
        assertScore(nback, 0, 2, 0, 2)

        nback = NBack(nBack = 3,
                maxRounds = 3,
                charGenerator = CharSequence("CCCCDE")::next)
        assertEquals(-3, nback.round)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals(0, nback.round)

        assertEquals('C', nback.nextChar())
        assertScore(nback, 0, 0, 0, 0)
        assertEquals(1, nback.round)

        assertEquals('D', nback.nextChar())
        assertEquals(2, nback.round)

        assertScore(nback, 0, 0, 0, 1)
        assertEquals('E', nback.nextChar())
        assertScore(nback, 0, 1, 0, 1)
        assertEquals(3, nback.round)

        assertEquals(' ', nback.nextChar())
        assertScore(nback, 0, 2, 0, 1)
        assertEquals(3, nback.round)
    }



}
