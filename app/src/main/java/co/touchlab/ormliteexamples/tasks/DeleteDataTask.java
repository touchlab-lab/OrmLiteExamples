package co.touchlab.ormliteexamples.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import co.touchlab.android.threading.tasks.Task;
import co.touchlab.ormliteexamples.database.DatabaseHelper;
import co.touchlab.ormliteexamples.database.Message;

/**
 * Created by kgalligan on 2/20/15.
 */
public class DeleteDataTask extends DataSetupMarkerTask
{
    @Override
    protected void run(Context context) throws Throwable
    {
        final DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        Dao<Message, Integer> dao = dbHelper.getMessageDao();
        dao.deleteBuilder().delete();
    }
}
