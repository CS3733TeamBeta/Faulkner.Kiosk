package Domain.Map;

import javafx.scene.image.Image;

/**
 * Info for a destination
 */
public class Info
{
    String name;
    String description;
    String hours;
    Image view;

    public Info(String name, String description, String hours)
    {
        this.name = name;
        this.description = description;
        this.hours = hours;
    }
    @Override
    public String toString()
    {
        return super.toString();
    }

    public boolean isOpen()
    {
        return true;
    }
}
