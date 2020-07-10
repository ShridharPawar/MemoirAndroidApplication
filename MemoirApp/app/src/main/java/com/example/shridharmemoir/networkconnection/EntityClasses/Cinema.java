package com.example.shridharmemoir.networkconnection.EntityClasses;

public class Cinema {
    private int cinemaid;
    private String cinemaname;
    private String location;

    public Cinema(){}
    public Cinema(int cinemaid){this.cinemaid = cinemaid;}

    public Cinema(int cinemaid,String cinemaname,String location)
    {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.location = location;
    }
    public int getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(int cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
