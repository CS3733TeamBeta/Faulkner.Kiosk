package Model.Database;

import Entity.Hospital;

import java.sql.SQLException;

/**
 * Created by benhylak on 2/25/17.
 */
public class DataCache
{
    static DataCache dC =null;
    DatabaseManager db;

    Hospital h;

    protected DataCache()
    {
        try
        {
            db = new DatabaseManager();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            h = db.loadData();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static DataCache getInstance()
    {
        if(dC == null)
        {
            dC = new DataCache();
        }

        return dC;
    }

    public Hospital getHospital()
    {
        return h;
    }

    public void save()
    {
        try
        {
            db.saveData(h);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
