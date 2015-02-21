package co.touchlab.ormliteexamples.tasks;
import android.content.Context;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.ormliteexamples.database.DatabaseHelper;
import co.touchlab.ormliteexamples.database.Message;

/**
 * Created by kgalligan on 2/20/15.
 */
public class GetDataTask extends Task
{
    public List<Message> messages;

    @Override
    protected void run(Context context) throws Throwable
    {
        messages = DatabaseHelper.getInstance(context).getMessageDao().queryForAll();
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
    }
}
