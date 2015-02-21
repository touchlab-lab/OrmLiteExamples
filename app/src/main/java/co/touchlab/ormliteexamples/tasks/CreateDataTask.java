package co.touchlab.ormliteexamples.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.ormliteexamples.database.DatabaseHelper;
import co.touchlab.ormliteexamples.database.Message;

/**
 * Created by kgalligan on 2/20/15.
 */
public class CreateDataTask extends DataSetupMarkerTask
{
    private final int dataCount;

    public CreateDataTask(int dataCount)
    {
        this.dataCount = dataCount;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        final DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        dbHelper.runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    makeData(dbHelper);
                }
                catch(SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        Dao<Message, Integer> messageDao = dbHelper.getMessageDao();
        List<Message> list = messageDao.query(messageDao.queryBuilder().prepare());

        for(Message message : list)
        {
            System.out.println(message.message);
        }
    }

    private void makeData(DatabaseHelper dbHelper) throws SQLException
    {
        Dao<Message, Integer> dao = dbHelper.getMessageDao();

        for(int i=0; i<dataCount; i++)
        {
            Message data = new Message();
            data.message = "Message "+ i;
            dao.create(data);
        }
    }
}
