package Domain.Map;


import java.util.HashSet;

/**
 * Info specific for a doctor
 */
public class Doctor extends Info {

    int docID;
    HashSet<String> department;
    HashSet<String> myOffice;
    Office office;
    String phoneNum = "N/A";
    String depts = ""; // For iteration 2, demonstration purposes

    /**
     * Creates a new Doctor with the given name, description, and hours with an empty set of departments and offices.
     * @param name The name of the doctor
     * @param description The description of the doctor
     * @param hours The hours of the doctor
     */
    public Doctor(String name, String description, String hours) {
        super(name, description, hours);
        this.department = new HashSet<>();
        this.myOffice = new HashSet<>();
    }

    /**
     * Creates a new Doctor with the given department, phone number, office, name, description, and hours
     * @param dept A department of the doctor
     * @param phoneNum The phone number of the doctor
     * @param docOff A office of the doctor
     * @param name The name of the doctor
     * @param description The description of the doctor
     * @param hours The hours of the doctor
     */
    public Doctor(String dept, String phoneNum, Office docOff, String name, String description, String hours) {
        super(name, description, hours);
        //this.department.add(dept);
        this.depts = dept;
        this.phoneNum = phoneNum;
        //this.myOffice.add(docOff);
        super.name = name;
        super.description = description;
        super.hours = hours;

        this.office = docOff;
    }

    public String getDepts() {
        return this.depts;
    }

    public void setDepts(String dept) {
        this.depts = dept;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    /**
     * Retrieves the doctorID of this
     * @return the DocId of this doctor
     */
    public int getDocID() {
        return docID;
    }

    /**
     * Retrieves the Departments of this docotr
     * @return A hashSet of Strings representing the departments
     */
    public HashSet<String> getDepartment() {
        return department;
    }

    /**
     * Sets the departments of this doctor to the given HashSet of departments.
     * @param department
     */
    public void setDepartment(HashSet<String> department) {
        this.department = department;
    }

    /**
     * Retrieves the phone number of this doctor
     * @return The phone number of this doctor as a String
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Sets the phone number of this doctor to be the given string.
     * @param phoneNumber
     */
    public void setPhoneNum(String phoneNumber) {
        this.phoneNum = phoneNumber;
    }

    /**
     * Retrieves the offices that this doctor has
     * @return A hashset of strings representing this doctor's office
     */
    public HashSet<String> getMyOffice() {
        return myOffice;
    }

    /**
     * Sets this doctor's office(s) to be the given set of offices
     * @param office A hashSet of Strings representing a hashset of offices.
     */
    public void setMyOffice(HashSet<String> office) {
        this.myOffice = office;
    }

    /**
     * Adds the given department to this doctor's departments
     * @param department as a String
     */
    public void addDepartment(String department) {
        this.department.add(department);
    }

    /**
     * Removes the given department from this doctor's departments
     * @param department as a String
     */
    public void removeDepartment(String department) {
        this.department.remove(department);
    }

    /**
     * Adds the given office to this doctor's offices
     * @param office as a String
     */
    public void addOffice(String office) {
        this.myOffice.add(office);
    }

    /**
     * Removes the given office to this doctor's offices
     * @param office as a String
     */
    public void removeOffice(String office) {
        this.myOffice.remove(office);
    }

    /**
     * Retrieves this doctor's name
     * @return this doctor's name as a string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves this doctor's description
     * @return this doctor's description as a string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Retrieves this doctor's hours
     * @return this doctor's hours, as a string.
     */
    public String getHours() {
        return this.hours;
    }
}
