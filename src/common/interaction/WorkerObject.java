package common.interaction;

import common.data.Coordinates;
import common.data.Person;
import common.data.Position;
import common.data.Status;
import java.io.Serializable;

public class WorkerObject implements Serializable {
    private String name;
    private Coordinates coordinates;
    private int salary;
    private Position position;
    private Status status;
    private Person person;
    private static final long serialVersionUID = 1L;

    public WorkerObject() {

    }

    public WorkerObject(String name, Coordinates coordinates,
                        int salary, Position position, Status status, Person person) {
        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getSalary() {
        return salary;
    }

    public Position getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        String result = String.format("Name: %s\nCoordinates: {x: %d, y: %d}\nSalary: %d\nPosition: %s\nStatus: %s\nPerson: {Birthday: %S\nEyeColor: %s\nHairColor: %s\nCountry: %s\nLocation{x: %f\n y: %d\nz: %f\nName: %s}\n}", getName(), getCoordinates().getX(), getCoordinates().getY(), getSalary(), getPosition(), getStatus(), getPerson().getBirthday(), getPerson().getEyeColor(), getPerson().getHairColor(), getPerson().getNationality(), getPerson().getLocation().getX(), getPerson().getLocation().getY(), getPerson().getLocation().getZ(), getPerson().getLocation().getName());
        return result;
    }
}
