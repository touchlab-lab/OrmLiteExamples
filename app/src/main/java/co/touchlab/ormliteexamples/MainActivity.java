package co.touchlab.ormliteexamples;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;
import co.touchlab.ormliteexamples.activities.LoaderExampleActivity;
import co.touchlab.ormliteexamples.activities.QueryExampleActivity;
import co.touchlab.ormliteexamples.database.Message;
import co.touchlab.ormliteexamples.tasks.CreateDataTask;
import co.touchlab.ormliteexamples.tasks.DeleteDataTask;
import co.touchlab.ormliteexamples.tasks.GetDataTask;


public class MainActivity extends ActionBarActivity
{

    private ListView listView;

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
        if(!TaskQueueHelper.hasTasksOfType(TaskQueue.loadQueueDefault(this), DeleteDataTask.class, CreateDataTask.class))
        {
            TaskQueue.loadQueueDefault(this).execute(new DeleteDataTask());
            TaskQueue.loadQueueDefault(this).execute(new CreateDataTask(dataCount));
        }
    }
}
