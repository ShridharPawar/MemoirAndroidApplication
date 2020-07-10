/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoirws;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shrid
 */
@Entity
@Table(name = "MEMOIR")
@XmlRootElement
@NamedQueries({
   @NamedQuery(name = "Memoir.findAll", query = "SELECT m FROM Memoir m")
    , @NamedQuery(name = "Memoir.findByMemoirid", query = "SELECT m FROM Memoir m WHERE m.memoirid = :memoirid")
    , @NamedQuery(name = "Memoir.findByMoviename", query = "SELECT m FROM Memoir m WHERE lower(m.moviename) = lower(:moviename)")
    , @NamedQuery(name = "Memoir.findByMoviereleasedate", query = "SELECT m FROM Memoir m WHERE m.moviereleasedate = :moviereleasedate")
    , @NamedQuery(name = "Memoir.findByDatetimewatched", query = "SELECT m FROM Memoir m WHERE m.datetimewatched = :datetimewatched")
    , @NamedQuery(name = "Memoir.findByComment", query = "SELECT m FROM Memoir m WHERE lower(m.comment) = lower(:comment)")
    , @NamedQuery(name = "Memoir.findByUserrating", query = "SELECT m FROM Memoir m WHERE m.userrating = :userrating")
    , @NamedQuery(name = "Memoir.findByCinemaNameAndCommentStatic", query = "SELECT m FROM Memoir m WHERE lower(m.comment) = lower(:comment) AND lower(m.cinemaid.cinemaname) = lower(:cinemaname)")
})
public class Memoir implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMOIRID")
    private Integer memoirid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MOVIENAME")
    private String moviename;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MOVIERELEASEDATE")
    private String moviereleasedate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DATETIMEWATCHED")
    private String datetimewatched;
    @Size(max = 400)
    @Column(name = "COMMENT")
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERRATING")
    private double userrating;
    @JoinColumn(name = "CINEMAID", referencedColumnName = "CINEMAID")
    @ManyToOne(optional = false)
    private Cinema cinemaid;
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    @ManyToOne(optional = false)
    private Person personid;

    public Memoir() {
    }

    public Memoir(Integer memoirid) {
        this.memoirid = memoirid;
    }

    public Memoir(Integer memoirid, String moviename, String moviereleasedate, String datetimewatched, double userrating) {
        this.memoirid = memoirid;
        this.moviename = moviename;
        this.moviereleasedate = moviereleasedate;
        this.datetimewatched = datetimewatched;
        this.userrating = userrating;
    }

    public Integer getMemoirid() {
        return memoirid;
    }

    public void setMemoirid(Integer memoirid) {
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

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(Person personid) {
        this.personid = personid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memoirid != null ? memoirid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoir)) {
            return false;
        }
        Memoir other = (Memoir) object;
        if ((this.memoirid == null && other.memoirid != null) || (this.memoirid != null && !this.memoirid.equals(other.memoirid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoirws.Memoir[ memoirid=" + memoirid + " ]";
    }
    
}
