package co.touchlab.ormliteexamples.activities;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;
import co.touchlab.ormliteexamples.R;
import co.touchlab.ormliteexamples.tasks.CreateDataTask;
import co.touchlab.ormliteexamples.tasks.DataSetupMarkerTask;
import co.touchlab.ormliteexamples.tasks.DeleteDataTask;

/**
 * Created by kgalligan on 2/21/15.
 */
public class BaseQueryExampleActivity extends ActionBarActivity
{
    private ListView    listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshProgress();

        EventBusExt.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }

    private void refreshProgress()
    {
        boolean dataRunning = TaskQueueHelper
                .hasTasksOfType(TaskQueue.loadQueueDefault(this), DeleteDataTask.class,
                                CreateDataTask.class);

        listView.setVisibility(dataRunning
                                       ? View.GONE
                                       : View.VISIBLE);
        progressBar.setVisibility(dataRunning
                                          ? View.VISIBLE
                                          : View.GONE);
    }

    protected ListView getListView()
    {
        return listView;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(DataSetupMarkerTask task)
    {
        refreshProgress();
    }
}
