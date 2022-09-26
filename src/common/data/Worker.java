package common.data;

import java.time.LocalDateTime;

import common.interaction.User;

public class Worker implements Comparable<Worker> {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private java.time.LocalDateTime creationDate;
    private int salary;
    private Position position;
    private Status status;
    private Person person;
    private User owner;

    public Worker(){

    }

    public Worker(Long id, String name, Coordinates coordinates,
                  LocalDateTime creationDate, int salary, Position position, Status status, Person person, User owner){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
        this.owner = owner;
    }

    public Long getID(){
        return id;
    }

    public void setID(Long id){
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creatioDate){
        this.creationDate = creatioDate;
    }

    public void setSalary(int salary){
        this.salary = salary;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void setPerson(Person person){
        this.person = person;
    }

    public String getName(){
        return name;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public int getSalary(){
        return salary;
    }

    public Position getPosition(){
        return position;
    }

    public Status getStatus(){
        return status;
    }

    public Person getPerson(){
        return person;
    }

    public int compareTo(Worker worker){
        return name.compareTo(worker.getName());
    }


    @Override
    public String toString(){
        String result = String.format("ID: %d\nName: %s\nCoordinates: {x: %d, y: %d}\nCreation Time: %s\nSalary: %d\nPosition: %s\nStatus: %s\nPerson: {Birthday: %S\nEyeColor: %s\nHairColor: %s\nCountry: %s\nLocation{x: %f\n y: %d\nz: %f\nName: %s}\n}", getID(), getName(),getCoordinates().getX(), getCoordinates().getY(), getCreationDate(), getSalary(), getPosition(), getStatus(), getPerson().getBirthday(), getPerson().getEyeColor(), getPerson().getHairColor(), getPerson().getNationality(), getPerson().getLocation().getX(), getPerson().getLocation().getY(), getPerson().getLocation().getZ(), getPerson().getLocation().getName());
        return result;
    }
}