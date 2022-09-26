package common.data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private int x;
    private Integer y;

    public Coordinates(){

    }

    public Coordinates(int x, Integer y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(Integer y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }
}
