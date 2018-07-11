package ct.cogtrack

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SqliteBackendHelper(val scriptCallback: (String) -> String,
                          databaseName: String? = "cogtrack.db",
                          context: Context? = null)
    : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION)
{

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(scriptCallback("createdb"))
    }

    // TODO: Do something like this when there are updates
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {

        for (i in oldVersion..(newVersion-1))
            db.execSQL(scriptCallback("dbupgrade${i}_${i+1}"))
    }

    companion object
    {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
    }
}


class SqliteBackend
{
}