package Controller.Admin;

import java.util.LinkedList;

/**
 * Created by jw97 on 2/2/2017.
 */

public class doctorProfile {
    private String firstName;
    private String lastName;
    private String roomNum;
    // Should I include roomNum, phoneNum, etc.
    // Discussion: LinkedList?
    private LinkedList<String> departments;

    public doctorProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.departments = new LinkedList<String>();
    }

    // Would it be relevant to have methods that change the doctor's first and last name?

    public int addDepartment(String department) {
        // Checks if the doctor is currently in the department
        if (this.departments.contains(department)) {
            return 1; // Exception
        }

        this.departments.add(department);
        return 0;
    }

    public int removeDepartment(String department) {
        if (this.departments.contains(department)) {
            this.departments.remove(department);
        } else {
            return 1; // Exception
        }

        return 0;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public LinkedList<String> getDepartments() {
        return this.departments;
    }
}
