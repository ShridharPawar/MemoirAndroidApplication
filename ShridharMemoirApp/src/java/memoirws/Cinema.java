/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoirws;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author shrid
 */
@Entity
@Table(name = "CINEMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cinema.findAll", query = "SELECT c FROM Cinema c")
    , @NamedQuery(name = "Cinema.findByCinemaid", query = "SELECT c FROM Cinema c WHERE c.cinemaid = :cinemaid")
    , @NamedQuery(name = "Cinema.findByCinemaname", query = "SELECT c FROM Cinema c WHERE lower(c.cinemaname) = lower(:cinemaname)")
    , @NamedQuery(name = "Cinema.findByLocation", query = "SELECT c FROM Cinema c WHERE c.location = :location")})
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CINEMAID")
    private Integer cinemaid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CINEMANAME")
    private String cinemaname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "LOCATION")
    private String location;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cinemaid")
    private Collection<Memoir> memoirCollection;

    public Cinema() {
    }

    public Cinema(Integer cinemaid) {
        this.cinemaid = cinemaid;
    }

    public Cinema(Integer cinemaid, String cinemaname, String location) {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.location = location;
    }

    public Integer getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Integer cinemaid) {
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

    @XmlTransient
    public Collection<Memoir> getMemoirCollection() {
        return memoirCollection;
    }

    public void setMemoirCollection(Collection<Memoir> memoirCollection) {
        this.memoirCollection = memoirCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cinemaid != null ? cinemaid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cinema)) {
            return false;
        }
        Cinema other = (Cinema) object;
        if ((this.cinemaid == null && other.cinemaid != null) || (this.cinemaid != null && !this.cinemaid.equals(other.cinemaid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoirws.Cinema[ cinemaid=" + cinemaid + " ]";
    }
    
}
