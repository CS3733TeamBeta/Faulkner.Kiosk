package Domain.Map;


import java.util.HashSet;
import java.util.UUID;

/**
 * Info specific for a doctor
 */
public class Doctor extends Info
{

    UUID docID;
    HashSet<Suite> suites;
    HashSet<String> myOffice;
    String phoneNum = "N/A";
    private String name;
    private String description;
    private HashSet<Suite> suites;

    /*
    HashSet<String> lstNames;
    HashSet<HashSet<Suite>> lstSuites;
    HashSet<String> lstDescriptions;
*/


    public Doctor(String name, String description, String hours, HashSet<Suite> suites) {
        super(name, description, hours);
        this.docID = UUID.randomUUID();
        this.suites = suites;

        this.myOffice = new HashSet<>();
    }

    public Doctor(UUID docID, String name, String description, String hours, HashSet<Suite> suites) {
        super(name, description, hours);
        this.docID = docID;
        this.suites = suites;
        this.myOffice = new HashSet<>();
        this.name = name;
        this.description = description;


    }

//    public Doctor(String dept, String phoneNum, Office docOff, String name, String description, String hours)
//    {
//        super(name, description, hours);
//
//        //this.department.add(dept);
//        this.phoneNum = phoneNum;
//        //this.myOffice.add(docOff);
//        super.name = name;
//        super.description = description;
//        super.hours = hours;
//    }

    public UUID getDocID() {
        return docID;
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

    public HashSet<Suite> getSuites() {
        return this.suites;
    }

    public void setSuites(HashSet<Suite> suites) {
        this.suites = suites;
    }

    public String[] splitName() {
        String[] name = this.name.split(", ");

        return name;
    }

    public String[] splitPhoneNum() {
        String[] phoneNum = this.phoneNum.split("-");

        return phoneNum;
    }

    public String[] splitHours() {
        String[] hours = this.hours.split(" - ");

        return hours;
    }
}
