/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author uurtsaikh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Picture.findAll", query = "SELECT p FROM Picture p"),
    @NamedQuery(name = "Picture.findById", query = "SELECT p FROM Picture p WHERE p.id = :id"),
    @NamedQuery(name = "Picture.findByPath", query = "SELECT p FROM Picture p WHERE p.path = :path")})
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 1, max = 250)
    private String path;
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne
    private Hotel hotelId;

    public Picture() {
    }

    public Picture(Integer id) {
        this.id = id;
    }

    public Picture(Integer id, String path) {
        this.id = id;
        this.path = path;
    }
    
    public Picture(String path, Hotel hotelId) {
        this.path = path;
        this.hotelId = hotelId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Hotel getItemId() {
        return hotelId;
    }

    public void setItemId(Hotel hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Picture)) {
            return false;
        }
        Picture other = (Picture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Picture[ id=" + id + " ]";
    }
    
}
