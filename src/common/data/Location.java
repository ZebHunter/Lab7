package common.data;

import java.io.Serializable;

public class Location implements Serializable {
    private Double x;
    private Long y;
    private float z;
    private String name;

    public Location(){

    }

    public Location(Double x, Long y, float z, String name){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public void setX(Double x){
        this.x = x;
    }

    public void setY(Long y){
        this.y = y;
    }

    public void setZ(float z){
        this.z = z;
    }

    public void setName(String name){
        this.name = name;
    }

    public Double getX(){
        return x;
    }

    public Long getY(){
        return y;
    }

    public float getZ(){
        return z;
    }

    public String getName(){
        return name;
    }
}