/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Ghenet
 */
@Entity
@NamedQueries({
@NamedQuery(name = "Booking.findRoom",
            query = "SELECT DISTINCT r FROM Room r WHERE r.hotel.id = :id AND r.id NOT IN ("
                    + "SELECT DISTINCT b.room.id FROM RoomBooking b WHERE b.dateFrom BETWEEN :dateFrom and :dateTo "
                    + "OR b.dateTo between :dateFrom and :dateTo)"),
       @NamedQuery(name = "Booking.checkConfNum",
            query = "SELECT b FROM RoomBooking b WHERE b.confirmation = :confirmation")

    
})
public class RoomBooking implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateFrom;
    @Temporal(TemporalType.DATE)
    private Date dateTo; 
    private Long confirmation;
    @ManyToOne
    private Room room;
    private int no_of_people;
    @ManyToOne
    private Account account;
    @Transient
    private int noRoom =1;
    
            

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(int no_of_people) {
        this.no_of_people = no_of_people;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Long confirmation) {
        this.confirmation = confirmation;
    }

    public int getNoRoom() {
        return noRoom;
    }

    public void setNoRoom(int noRoom) {
        this.noRoom = noRoom;
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
        if (!(object instanceof RoomBooking)) {
            return false;
        }
        RoomBooking other = (RoomBooking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  confirmation + " "+id + " ]";
    }
    
}
