package Domain.Map;

import javafx.scene.image.Image;

/**
 * Info for a destination
 */
public class Info
{
    protected String name;
    protected String description;
    protected String hours;
    protected String phoneNumber;
    protected Image view;

    public Info(String name, String description, String hours)
    {
        this.name = name;
        this.description = description;
        this.hours = hours;
    }

    public Info()
    {
        this.name = "";
        this.description = "";
        this.hours = "";
        this.phoneNumber = "";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return name
     */
    public String getName()
    {
        return this.name;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return this.hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
