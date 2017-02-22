

import Controller.Admin.AdminList;
import Controller.Admin.AdminProfile;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;
import Model.Directory;
import Model.DoctorProfile;
import Model.RoomInfo;
import Model.RoomList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import junit.framework.TestCase;

import java.util.*;

/**
 * Created by Samuel on 2/4/2017.
 */
public class UI_Tests1 extends TestCase{

   public void UI_Tests () throws AddFoundException, RemoveNotFoundException{
        this.test_admin();
        this.test_dr();
        this.test_room();

    }

    public void test_admin () {
        AdminProfile newAdmin = new AdminProfile("testAdmin", "testAdmin");
        String usr = newAdmin.getUsername();


        if (!Objects.equals(usr, "testAdmin")) {
            fail("Username not found");
        }
        String pwd = newAdmin.getPassword();
        if (!Objects.equals(pwd, "testAdmin")) {
            fail("Password not found");
        }

        AdminList testList = new AdminList();
        testList.addAdmin("testAdmin2", "testAdmin2");
        testList.addAdmin("testAdmin3", "testAdmin3");


        boolean newUsr = testList.changeUsername("testAdmin2", "admin3");
        assertEquals ("Username not changed", false, newUsr);
        boolean newPwd = testList.changePassword("admin3", "teastAdmin2", "admin3");
        assertEquals("Password not changed", true, newPwd);

        testList.changeUsername("admin3", "testAdmin3"); //fix that you can have two fo the same admin profiles
        try {
            testList.changePassword("testAdmin3", "testAdmin3", "testAdmin4");//What password does this change
            testList.changePassword("testAdmin3", "admin3", "testAdmin4");
        } catch (Exception e) {
            fail ("cant change password");
        }

    }


    public void test_dr () throws AddFoundException, RemoveNotFoundException{
        DoctorProfile testDr = new DoctorProfile("A", "1", "100");
        StringProperty test = new StringProperty() {
            @Override
            public void bind(ObservableValue<? extends String> observable) {}
            @Override
            public void unbind() {}
            @Override
            public boolean isBound() {
                return false;
            }
            @Override
            public Object getBean() {
                return null;
            }
            @Override
            public String getName() {
                return null;
            }
            @Override
            public String get() {
                return null;
            }
            @Override
            public void addListener(ChangeListener<? super String> listener) {}
            @Override
            public void removeListener(ChangeListener<? super String> listener) {}
            @Override
            public void addListener(InvalidationListener listener) {}
            @Override
            public void removeListener(InvalidationListener listener) {}
            @Override
            public void set(String value) {}
        };
        test.setValue("radiology");
        testDr.addDepartment(test);
        HashSet<StringProperty> testDept = testDr.getDepartments();
        if (!(testDept.contains(test))) {
            fail("Departments not added");
        }

        try {
            testDr.addDepartment(test);
            fail("Department already added");
        }catch (AddFoundException e) {
            assertTrue(e instanceof AddFoundException);
        }


        try {
            testDr.removeDepartment(test);
        } catch (RemoveNotFoundException e){
           fail("Department not removed");
        }

    }

    public void test_room () throws AddFoundException, RemoveNotFoundException{
        HashSet<String> testDrs = new HashSet<String>();
        DoctorProfile drSmith = new DoctorProfile("Adam", "Smith", "101");
        RoomInfo testRoom = new RoomInfo("101");
        testRoom.changeRoomNum("103");
        assertEquals("Room # successfully changed", "103", testRoom.getRoomNum());

        testRoom.addDoctor(drSmith);
        HashSet<DoctorProfile> drs = testRoom.getDoctorsList();
        if (! drs.contains(drSmith)) {
            fail("Doctor not added");
        }

        testRoom.removeDoctor(drSmith);
        drs = testRoom.getDoctorsList();
        if (drs.contains(drSmith)) {
            fail("Doctor not removed");
        }

        RoomList testList = new RoomList();
        StringProperty test = new StringProperty() {
            @Override
            public void bind(ObservableValue<? extends String> observable) {}
            @Override
            public void unbind() {}
            @Override
            public boolean isBound() {
                return false;
            }
            @Override
            public Object getBean() {
                return null;
            }
            @Override
            public String getName() {
                return null;
            }
            @Override
            public String get() {
                return null;
            }
            @Override
            public void addListener(ChangeListener<? super String> listener) {}
            @Override
            public void removeListener(ChangeListener<? super String> listener) {}
            @Override
            public void addListener(InvalidationListener listener) {}
            @Override
            public void removeListener(InvalidationListener listener) {}
            @Override
            public void set(String value) {}
        };
        test.setValue("103");
        testList.addRoom(test, testRoom);
        assert(testList.hospitalHasRoom(test));


        try {
            testList.removeRoom(test);
            fail("Room not removed");
        } catch (Exception e) {
            assertTrue(e instanceof RemoveNotFoundException);
        }


    }


    public void test_Directory () throws AddFoundException, RemoveNotFoundException{
        Directory testDir = new Directory();
        DoctorProfile drA = new DoctorProfile("A", "1", "101");
        DoctorProfile drB = new DoctorProfile("B", "2", "102");
        DoctorProfile drC = new DoctorProfile("C", "3", "103");
        DoctorProfile drD = new DoctorProfile("D", "4", "104");
        DoctorProfile drE = new DoctorProfile("D", "4", "104"); //For adding the same doctor

        testDir.addToDirectory(drA);
        testDir.addToDirectory(drB);
        testDir.addToDirectory(drC);
        testDir.addToDirectory(drD);

        try {
            testDir.addToDirectory(drE);
        } catch (AddFoundException e) {
            fail("Doctor already added");
        }

        testDir.removeFromDirectory(drA);
        testDir.removeFromDirectory(drB);
        testDir.removeFromDirectory(drC);
        testDir.removeFromDirectory(drD);
        testDir.removeFromDirectory(drE);

        try {
            testDir.removeFromDirectory(drA);
            fail("Doctor Already removed");
        } catch (Exception e) {
            assertTrue(e instanceof RemoveNotFoundException);
        }


    }

}
