package com.example.shridharmemoir.networkconnection.EntityClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    private int personid;
    private String firstname;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;

    public Person(){}

    public Person(int personid)
    {
        this.personid = personid;
    }

    public Person(int personid,String firstname,String surname,String gender,String dob,String address,String state,String postcode)
    {
        this.personid = personid;
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.state = state;
        this.postcode = postcode;
        this.address = address;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob)
    {
        this.dob = dob;
    }

    public String getAddress()
    {return  address;}

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

     public void setState(String state) {
        this.state = state;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getPersonidpersonid(){
        return personid;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getSurname(){
        return surname;
    }

    public String getGender(){
        return gender;
    }

    public String getdob(){
        return dob;
    }

    public String getState(){
        return state;
    }

    public String getPostcode(){
        return postcode;
    }



}
