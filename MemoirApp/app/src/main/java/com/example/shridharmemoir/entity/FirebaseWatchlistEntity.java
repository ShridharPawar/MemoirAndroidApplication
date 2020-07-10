package com.example.shridharmemoir.entity;

public class FirebaseWatchlistEntity {
    public String fid;

    public String movieName;

    public int personid;

    public String releaseDate;

    public String addedDateTime;

    public FirebaseWatchlistEntity(){}

    public FirebaseWatchlistEntity(String movieName,String releaseDate,String addedDateTime,int personid)
    {
        this.addedDateTime = addedDateTime;
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.personid = personid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddedDateTime() {
        return addedDateTime;
    }

    public void setAddedDateTime(String addedDateTime) {
        this.addedDateTime = addedDateTime;
    }
}
