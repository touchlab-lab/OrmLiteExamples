package co.touchlab.ormliteexamples.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.loader.support.OrmLiteListLoader;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.touchlab.ormliteexamples.R;
import co.touchlab.ormliteexamples.database.DatabaseHelper;
import co.touchlab.ormliteexamples.database.Message;

public class LoaderListExampleActivity extends BaseQueryExampleActivity
{
    public static void callMe(Context c)
    {
        Intent i = new Intent(c, LoaderListExampleActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            getListView().setAdapter(
                    new QueryArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Message>()));
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Toast.makeText(LoaderListExampleActivity.this,
                                   ((Message) getListView().getAdapter().getItem(position)).message,
                                   Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        startLoader();
    }

    private void startLoader()
    {
        LoaderManager.LoaderCallbacks<List<Message>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Message>>()
        {
            @Override
            public Loader<List<Message>> onCreateLoader(int id, Bundle args)
            {
                try
                {
                    DatabaseHelper databaseHelper = DatabaseHelper
                            .getInstance(LoaderListExampleActivity.this);
                    return new OrmLiteListLoader<>(LoaderListExampleActivity.this,
                                                      databaseHelper.getMessageDao(),
                                                      makePreparedQuery(databaseHelper));
                }
                catch(SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onLoadFinished(Loader<List<Message>> loader, List<Message> data)
            {
                QueryArrayAdapter adapter = (QueryArrayAdapter) getListView().getAdapter();
                adapter.clear();
                adapter.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<List<Message>> loader)
            {

            }
        };
        getLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    private PreparedQuery<Message> makePreparedQuery(DatabaseHelper databaseHelper) throws SQLException
    {
        return databaseHelper.getMessageDao().queryBuilder().where().gt("id", 20000).prepare();
    }

    private class QueryArrayAdapter extends ArrayAdapter<Message>
    {

        public QueryArrayAdapter(Context context, int resource, List<Message> objects)
        {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, null);

            ((TextView) convertView.findViewById(R.id.text)).setText(getItem(position).message);

            return convertView;
        }
    }

    /*private class QueryAdapter extends OrmLiteResultsAdapter<Message, Integer>
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
    }*/
}
