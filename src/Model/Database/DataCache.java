package Model.Database;

import Domain.Map.Hospital;

import java.sql.SQLException;

/**
 * Created by benhylak on 2/25/17.
 */
public class DataCache
{
    static DataCache dC =null;

    Hospital h;

    protected DataCache()
    {
        try
        {
            h = new DatabaseManager().loadData();
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
}
