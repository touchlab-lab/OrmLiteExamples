package co.touchlab.ormliteexamples.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by kgalligan on 2/20/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    public static final String DBNAME = "dbname";
    public static final int DATABASE_VERSION = 1;

    private static DatabaseHelper INSTANCE;

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new DatabaseHelper(context.getApplicationContext());
        }

        return INSTANCE;
    }

    public DatabaseHelper(Context context)
    {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Message.class);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, Message.class, true);
            onCreate(database, connectionSource);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Dao<Message, Integer> getMessageDao() throws SQLException
    {
        return getDao(Message.class);
    }

    public void runInTransaction(Runnable runnable)
    {
        SQLiteDatabase database = getWritableDatabase();
        if(!database.inTransaction())
        {
            database.beginTransaction();
        }

        try
        {
            runnable.run();
            database.setTransactionSuccessful();
        }
        finally
        {
            database.endTransaction();
        }

    }
}
