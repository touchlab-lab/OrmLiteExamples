package co.touchlab.ormliteexamples;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;
import co.touchlab.ormliteexamples.activities.LoaderExampleActivity;
import co.touchlab.ormliteexamples.activities.QueryExampleActivity;
import co.touchlab.ormliteexamples.tasks.CreateDataTask;
import co.touchlab.ormliteexamples.tasks.DeleteDataTask;


public class MainActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void exampleQuery(View v)
    {
        resetDataToCount(1000);
        QueryExampleActivity.callMe(this);
    }

    public void exampleLoader(View v)
    {
        resetDataToCount(50000);
        LoaderExampleActivity.callMe(this);
    }

    private void resetDataToCount(int dataCount)
    {
        if(! TaskQueueHelper.hasTasksOfType(TaskQueue.loadQueueDefault(this), DeleteDataTask.class,
                                            CreateDataTask.class))
        {
            TaskQueue.loadQueueDefault(this).execute(new DeleteDataTask());
            TaskQueue.loadQueueDefault(this).execute(new CreateDataTask(dataCount));
        }
    }
}
