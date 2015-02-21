package co.touchlab.ormliteexamples.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OrmLiteResultsAdapter;
import com.j256.ormlite.android.apptools.loader.OrmLiteResultsLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.ormliteexamples.R;
import co.touchlab.ormliteexamples.database.DatabaseHelper;
import co.touchlab.ormliteexamples.database.Message;
import co.touchlab.ormliteexamples.tasks.CreateDataTask;

public class LoaderExampleActivity extends BaseQueryExampleActivity
{
    public static void callMe(Context c)
    {
        Intent i = new Intent(c, LoaderExampleActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            getListView().setAdapter(
                    new QueryAdapter(this, DatabaseHelper.getInstance(this).getMessageDao()));
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Toast.makeText(LoaderExampleActivity.this, ((Message)getListView().getAdapter().getItem(position)).message, Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }

        startLoader();
    }

    private void startLoader()
    {
        LoaderManager.LoaderCallbacks<AndroidDatabaseResults> loaderCallbacks = new LoaderManager.LoaderCallbacks<AndroidDatabaseResults>()
        {
            @Override
            public Loader<AndroidDatabaseResults> onCreateLoader(int id, Bundle args)
            {
                try
                {
                    DatabaseHelper databaseHelper = DatabaseHelper
                            .getInstance(LoaderExampleActivity.this);
                    return new OrmLiteResultsLoader<>(LoaderExampleActivity.this,
                                                                    databaseHelper.getMessageDao(),
                                                                    makePreparedQuery(
                                                                            databaseHelper));
                }
                catch(SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onLoadFinished(Loader<AndroidDatabaseResults> loader, AndroidDatabaseResults data)
            {
                ((QueryAdapter) getListView().getAdapter()).changeResults(data);
            }

            @Override
            public void onLoaderReset(Loader<AndroidDatabaseResults> loader)
            {

            }
        };
        getLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    private PreparedQuery<Message> makePreparedQuery(DatabaseHelper databaseHelper) throws SQLException
    {
        return databaseHelper.getMessageDao().queryBuilder().prepare();
    }

    private class QueryAdapter extends OrmLiteResultsAdapter<Message, Integer>
    {
        public QueryAdapter(Context context, Dao<Message, Integer> dao) throws SQLException
        {
            super(context, dao);
        }

        @Override
        public View newView(Context context, int position, Message data, ViewGroup parent)
        {
            return LayoutInflater.from(context).inflate(R.layout.list_row, null);
        }

        @Override
        public void bindView(View view, int position, Message data)
        {
            ((TextView) view.findViewById(R.id.text)).setText(data.message);
        }
    }
}
