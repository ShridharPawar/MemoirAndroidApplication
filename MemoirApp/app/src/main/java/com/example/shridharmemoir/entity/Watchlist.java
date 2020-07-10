package com.example.shridharmemoir.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {


    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
       this.personid = personid;
    }

    @PrimaryKey(autoGenerate = true)
    public int wid;

    @ColumnInfo(name = "movieName")
    public String movieName;

    @ColumnInfo(name = "personid")
    public int personid;

    @ColumnInfo(name = "releaseDate")
    public String releaseDate;

    @ColumnInfo(name = "addedDateTime")
    public String addedDateTime;

    public Watchlist(){}

    public Watchlist(String movieName,String releaseDate,String addedDateTime,int personid)
    {
        this.addedDateTime = addedDateTime;
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.personid = personid;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
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
