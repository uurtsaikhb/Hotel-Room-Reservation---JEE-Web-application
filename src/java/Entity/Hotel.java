/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 *
 * @author Ghenet
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Hotel.findByLocation",
            query = "SELECT h FROM Hotel h WHERE h.address.city = :city AND h.address.province= :state")
})
public class Hotel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;
    @ManyToMany(mappedBy = "hotels")
    private List<FeatureHotel> hotelFeatures; 
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "hotel")
    @OrderBy("postedDate DESC")
    private List<Review> reviews;
    @Transient
    private int rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
     public int getRate() {
        rate=0;
        int count = 0;
        int total = 0;
        if(this.reviews.size()>0){
        for(Review r:this.reviews){
            total=total+r.getRank();
            count++;
        }
        rate=(int) Math.round(total/count);
        }
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

     
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    
    
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<FeatureHotel> getHotelFeatures() {
        return hotelFeatures;
    }

    public void setHotelFeatures(List<FeatureHotel> hotelFeatures) {
        this.hotelFeatures = hotelFeatures;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof Hotel)) {
            return false;
        }
        Hotel other = (Hotel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + "";
    }
    
}
