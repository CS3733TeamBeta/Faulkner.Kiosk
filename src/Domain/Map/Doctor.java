package Domain.Map;


import java.util.HashSet;
import java.util.UUID;

public class Doctor
{

    String name;
    String description;
    String hours;
    protected Image view;

    UUID docID;
    HashSet<Destination> destinations;
    String phoneNum = "N/A";

    public Doctor(String name, String description, String hours, HashSet<Destination> destinations) {
        this.name = name;
        this.description = description;
        this.hours = hours;
        this.docID = UUID.randomUUID();
        this.destinations = destinations;
    }

    public Doctor(UUID docID, String name, String description, String hours, HashSet<Destination> destinations) {
        this.name = name;
        this.description = description;
        this.hours = hours;
        this.docID = docID;
        this.destinations = destinations;
    }

    public UUID getDocID() {
        return docID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNumber) {
        this.phoneNum = phoneNumber;
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

    public HashSet<Destination> getDestinations() {
        return this.destinations;
    }

    public void setDestinations(HashSet<Destination> suites) {
        this.destinations = suites;
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
