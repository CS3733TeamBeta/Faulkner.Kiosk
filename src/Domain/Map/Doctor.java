package Domain.Map;


/**
 * Info specific for a doctor
 */
public class Doctor extends Info
{

    int docID;
    String department;
    String phoneNumber;
    Office myOffice;

    public Doctor(String dept, String phoneNum, Office docOff, String name, String description, String hours)
    {
        super(name, description, hours);

        this.department = dept;
        this.phoneNumber = phoneNum;
        this.myOffice = docOff;
        super.name = name;
        super.description = description;
        super.hours = hours;
    }

    public int getDocID() {
        return docID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber1) {
        this.phoneNumber = phoneNumber1;
    }

    public Office getMyOffice() {
        return myOffice;
    }

    public void setMyOffice(Office office) {
        this.myOffice = office;
    }

    public String getName() {
        return super.name;
    }

    public void setName(String name) {
        super.name = name;
    }

    public String getDescription() {
        return super.description;
    }

    public void setDescription(String description) {
        super.description = description;
    }

    public String getHours() {
        return super.hours;
    }

    public void setHours(String hours) {
        super.hours = hours;
    }

}
