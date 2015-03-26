package co.touchlab.ormliteexamples.database;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by kgalligan on 2/20/15.
 */
@DatabaseTable
public class Message
{
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String message;

    @DatabaseField
    public String another;

    @Override
    public String toString()
    {
        return message;
    }
}
