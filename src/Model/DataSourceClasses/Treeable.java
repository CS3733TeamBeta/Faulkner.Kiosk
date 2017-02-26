package Model.DataSourceClasses;

import javafx.collections.ObservableList;

import java.util.Observable;

/**
 * Created by benhylak on 2/25/17.
 */
public abstract class Treeable extends Observable implements HierarchyData<Treeable>
{
    public abstract ObservableList<Treeable> getChildren();
}
