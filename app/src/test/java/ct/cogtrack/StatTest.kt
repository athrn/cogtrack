package ct.cogtrack

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class StatTest
{

    @Test
    fun t1_singleValue()
    {
        var s = Stat()
        s(1.0)
        assertEquals(1, s.count)
        assertEquals(1.0, s.sum, 1e-14)
        assertEquals(1.0, s.avg, 1e-14)
    }

    @Test
    fun t2_manyValues()
    {
        var s = Stat()
        s(3.0)
        s(1.0)(2.0)
        assertEquals(3, s.count)
        assertEquals(6.0, s.sum, 1e-14)
        assertEquals(2.0, s.avg, 1e-14)
        assertEquals(1.0, s.stdev, 1e-14)
    }


    @Test
    fun t3_plusAssign()
    {
        var s = Stat()
        s += 4.0
        s += 1.0 + 1
        s += 0.0
        assertEquals(3, s.count)
        assertEquals(6.0, s.sum, 1e-14)
        assertEquals(2.0, s.avg, 1e-14)
        assertEquals(2.0, s.stdev, 1e-14)
    }
}