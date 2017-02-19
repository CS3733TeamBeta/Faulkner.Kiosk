package Domain.Map;

import java.util.UUID;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office
{
    UUID id;
    String name;
    Suite suite;

    protected String department;

    public Office(String name, Suite suite) {
        this.name = name;
        this.suite = suite;
    }

    public Office(UUID id, String name, Suite suite) {
        this.id = id;
        this.name = name;
        this.suite = suite;
    }

    public Office()
    {
        super();
    }

    public String getName() {
        return this.name;
    }

    public Suite getSuite() {
        return this.suite;
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
}
