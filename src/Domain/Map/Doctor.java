package Domain.Map;


import java.util.HashSet;

/**
 * Info specific for a doctor
 */
public class Doctor extends Info
{

    int docID;
    HashSet<Suite> suites;
    HashSet<String> myOffice;
    String phoneNum = "N/A";
    HashSet<String> lstNames;
    HashSet<HashSet<Suite>> lstSuites;
    HashSet<String> lstDescriptions;



    public Doctor(int id, String name, String description, String hours, HashSet<Suite> suites) {
        super(name, description, hours);
        this.docID = id;
        this.suites = suites;
        this.myOffice = new HashSet<>();
        lstNames.add(name);
        lstDescriptions.add(description);
        lstSuites.add(suites);

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

    public int getDocID() {
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



}
