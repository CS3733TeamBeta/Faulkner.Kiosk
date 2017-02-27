package Map.Entity;

import java.util.UUID;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office
{
    UUID id;
    String name;
    Destination destination;

    protected String department;

    public Office(String name, Destination destination) {
        this.name = name;
        this.destination = destination;
        this.id = UUID.randomUUID();
    }

    public Office(UUID id, String name, Destination destination) {
        this.id = id;
        this.name = name;
        this.destination = destination;
    }

    public Office()
    {
        super();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Destination getDestination() {
        return this.destination;
    }

    public UUID getId() {
        return this.id;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDepartment()
    {
        return this.department;
    }

    public void setSuite(Destination d) {
        this.destination = d;
    }
}
