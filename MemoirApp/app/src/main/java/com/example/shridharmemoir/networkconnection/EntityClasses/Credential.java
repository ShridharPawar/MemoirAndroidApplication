package com.example.shridharmemoir.networkconnection.EntityClasses;

import java.util.Date;

public class Credential {
    private int credentialid;
    private String username;
    private String passwordhash;
    private String signupdate;
    private Person personid;

    public Credential(int credentialid,String username,String passwordhash,String signupdate)
    {
        this.username=username;
        this.credentialid = credentialid;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
    }

    public void setPersonid(int personid,String firstname,String surname,String gender,String dob,String address,String state,String postcode)
    {
       this.personid = new Person(personid,firstname,surname,gender,dob,address,state,postcode);
    }

    public void setCredentialid(int credentialid)
    {
        this.credentialid=credentialid;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public void setPasswordhash(String passwordhash)
    {
        this.passwordhash=passwordhash;
    }

    public void setSignupdate(String signupdate)
    {
        this.signupdate=signupdate;
    }

    public int getCredentialid()
    {
        return credentialid;
    }

    public String getUsername()
    {return username;}

    public String getPasswordhash()
    {return passwordhash;}

    public String getSignupdate()
    {return signupdate;}

}
