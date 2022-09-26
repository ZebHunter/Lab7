package common.data;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Person implements Serializable{
    private java.time.LocalDateTime birthday;
    private EyeColor eyeColor;
    private Color hairColor;
    private Country nationality;
    private Location location;

    public Person(){

    }

    public Person(LocalDateTime birhday, EyeColor eyeColor, Color hairColor, Country nationality, Location location){
        this.birthday = birhday;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public LocalDateTime getBirthday(){
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday){
        this.birthday = birthday;
    }

    public void setEyeColor(EyeColor eyeColor){
        this.eyeColor = eyeColor;
    }

    public void setHairColor(Color hairColor){
        this.hairColor = hairColor;
    }

    public void setNationality(Country nationality){
        this.nationality = nationality;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public EyeColor getEyeColor(){
        return eyeColor;
    }

    public Color getHairColor(){
        return hairColor;
    }

    public Country getNationality(){
        return nationality;
    }

    public Location getLocation(){
        return location;
    }
}
