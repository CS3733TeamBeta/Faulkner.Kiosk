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

    /**
     * Creates an info with the given name, description, hours, and an empty phoneNumber
     * @param name
     * @param description
     * @param hours
     */
    public Info(String name, String description, String hours) {
        this.name = name;
        this.description = description;
        this.hours = hours;
        this.phoneNumber = "";
    }

    /**
     * Creates an info with empty name, description, hours, and phoneNumber
     */
    public Info(){
        this.name = "";
        this.description = "";
        this.hours = "";
        this.phoneNumber = "";
    }

    /**
     * Sets this Info's name to the given name
     * @param name the desired name as a String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves name of this info
     * @return the name of this info, as a string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a string representation of this Info.
     * @return
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns true if this Info is currently open
     * @return
     */
    public boolean isOpen() {
        return true;
        //@TODO Fix this
    }

    /**
     * Retrieves the description of this info
     * @return this's description as a String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets this Info's description to be the given description
     * @param description the desired description, as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves this Info's hours
     * @return this Info's hours, as a stirng
     */
    public String getHours() {
        return this.hours;
    }

    /**
     * Sets this Info's hours to be the given hours
     * @param hours the desired hours, as a String
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * Retrieves this info's phoneNumber
     * @return this info's phoneNumber, as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets this info's phoneNumber to the given phoneNumber
     * @param phoneNumber the desired phoneNumber, as a String
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
