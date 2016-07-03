package com.abraxas.spps.alma;

import java.io.Serializable;

public class Name implements Serializable{
    private String _firstName;
    private String _lastName;
    private String _position;

    public Name(String firstName, String lastName, String position){
        this._firstName = firstName;
        this._lastName = lastName;
        this._position = position;
    }

    public String getFirstName(){
        return this._firstName;
    }

    public String getLastName(){
        return this._lastName;
    }

    public String getPosition(){
        return this._position;
    }

    public String toString(){
        return this._firstName+" "+this._lastName;
    }
}
