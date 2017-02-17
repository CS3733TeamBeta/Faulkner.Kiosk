package Domain.Map;


import java.util.HashSet;

/**
 * Info specific for a doctor
 */
public class Doctor extends Info
{

    int docID;
    HashSet<String> department;
    HashSet<String> myOffice;
    String phoneNum = "N/A";

    public Doctor(String name, String description, String hours) {
        super(name, description, hours);

        this.department = new HashSet<>();
        this.myOffice = new HashSet<>();
    }

    public Doctor(String dept, String phoneNum, Office docOff, String name, String description, String hours)
    {
        super(name, description, hours);

        //this.department.add(dept);
        this.phoneNum = phoneNum;
        //this.myOffice.add(docOff);
        super.name = name;
        super.description = description;
        super.hours = hours;
    }

    public int getDocID() {
        return docID;
    }

    public HashSet<String> getDepartment() {
        return department;
    }

    public void setDepartment(HashSet<String> department) {
        this.department = department;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNumber) {
        this.phoneNum = phoneNumber;
    }

    public HashSet<String> getMyOffice() {
        return myOffice;
    }

    public void setMyOffice(HashSet<String> office) {
        this.myOffice = office;
    }

    public void addDepartment(String department) {
        this.department.add(department);
    }

    public void removeDepartment(String department) {
        this.department.remove(department);
    }

    public void addOffice(String office) {
        this.myOffice.add(office);
    }

    public void removeOffice(String office) {
        this.myOffice.remove(office);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHours() {
        return this.hours;
    }
}
