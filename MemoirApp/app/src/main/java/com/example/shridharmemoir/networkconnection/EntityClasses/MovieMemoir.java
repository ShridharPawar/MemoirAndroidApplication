package com.example.shridharmemoir.networkconnection.EntityClasses;

public class MovieMemoir {
    private int memoirid;
    private String moviename;
    private String moviereleasedate;
    private String datetimewatched;
    private String comment;
    private double userrating;
    private Person personid;
    private Cinema cinemaid;

    public MovieMemoir(){}
    public MovieMemoir(int memoirid,Person personid, Cinema cinemaid, String moviename, String moviereleasedate, String datetimewatched, String comment, double userrating) {
        this.memoirid = memoirid;
        this.moviename = moviename;
        this.moviereleasedate = moviereleasedate;
        this.datetimewatched = datetimewatched;
        this.comment = comment;
        this.userrating = userrating;
        this.personid = personid;
        this.cinemaid = cinemaid;
    }





    public int getMemoirid() {
        return memoirid;
    }

    public void setMemoirid(int memoirid) {
        this.memoirid = memoirid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getMoviereleasedate() {
        return moviereleasedate;
    }

    public void setMoviereleasedate(String moviereleasedate) {
        this.moviereleasedate = moviereleasedate;
    }

    public String getDatetimewatched() {
        return datetimewatched;
    }

    public void setDatetimewatched(String datetimewatched) {
        this.datetimewatched = datetimewatched;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getUserrating() {
        return userrating;
    }

    public void setUserrating(double userrating) {
        this.userrating = userrating;
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(Person personid) {
        this.personid = personid;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }



}
