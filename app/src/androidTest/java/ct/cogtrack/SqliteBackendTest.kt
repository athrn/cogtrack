package ct.cogtrack

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


fun callback(name: String): String
{
    when (name)
    {
        "createdb" -> { return "Create Table X (name Text);"}
        else -> { throw NotImplementedError("unexpected script ${name}") }
    }
}

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SqliteBackendTest
{
    @Test
    fun t1_helper()
    {
        val helper = SqliteBackendHelper(::callback, databaseName=null)
        var db = helper.writableDatabase
        db.beginTransaction()
        var cur = db.rawQuery("Select * From X;", emptyArray())
        assertEquals(1, cur.columnCount)
        db.endTransaction()


    }
}