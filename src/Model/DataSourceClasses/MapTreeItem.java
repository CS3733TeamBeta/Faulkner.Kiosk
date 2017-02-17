package Model.DataSourceClasses;

import Domain.Map.Floor;
import javafx.collections.ObservableList;

/**
 * Created by benhylak on 2/16/17.
 */
public class MapTreeItem implements HierarchyData<MapTreeItem>
{
    ObservableList<MapTreeItem> children;
    Object value;

    /**
     * Should be mapnode or floor
     *
     * @param value Mapnode or Floor
     * @TODO Interface for expected value type?
     */
    public MapTreeItem(Object value)
    {
        super();
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public ObservableList getChildren()
    {
        return null;
    }

    public boolean isFloor()
    {
        return (value instanceof Floor);
    }

    @Override
    public String toString()
    {
       return getValue().toString();
    }
}
