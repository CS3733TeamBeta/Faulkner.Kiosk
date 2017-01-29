package Domain.Map;

/**
 * Info specific for a doctor
 */
public class Doctor extends Info
{
    String department;
    String phoneNumber;
    Office myOffice;

    public Doctor(String name, String description, String hours)
    {
        super(name, description, hours);
    }
}
