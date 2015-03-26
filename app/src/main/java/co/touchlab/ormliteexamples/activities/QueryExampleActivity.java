package co.touchlab.ormliteexamples.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.ormliteexamples.database.Message;
import co.touchlab.ormliteexamples.tasks.CreateDataTask;
import co.touchlab.ormliteexamples.tasks.GetDataTask;


public class QueryExampleActivity extends BaseQueryExampleActivity
{
    public static void callMe(Context c)
    {
        Intent i = new Intent(c, QueryExampleActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        reloadData();
    }

    private void reloadData()
    {
        TaskQueue.loadQueueDefault(this).execute(new GetDataTask());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CreateDataTask task)
    {
        reloadData();
    }

    public void onEventMainThread(GetDataTask task)
    {
        getListView().setAdapter(
                new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1,
                                          task.messages));
    }
}
