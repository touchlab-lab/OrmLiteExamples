package co.touchlab.ormliteexamples.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by kgalligan on 2/21/15.
 */
public abstract class DataSetupMarkerTask extends Task
{
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
