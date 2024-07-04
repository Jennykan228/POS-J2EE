package com.cafedemetro.pos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name = "Orders.GetOrdersByQrCode", query = "select o from Orders o where o.qrCode = :qrCode")
public class Orders implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oId;

    @ManyToOne
    @JoinColumn(name = "bId")
    private Branches branch;

    @Column(nullable = false, unique = true, length = 50)
    private String qrCode;
    
    private int seats;

    @Column(nullable = false, length = 1)
    private String status; // A: Active ; P: Pending ; C: Completed

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
    private List<OrderDetails> details;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDtm;

    public long getoId() {
        return oId;
    }

    public void setoId(long oId) {
        this.oId = oId;
    }

    public Branches getBranch() {
        return branch;
    }

    public void setBranch(Branches branch) {
        this.branch = branch;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDetails> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetails> details) {
        this.details = details;
    }

    public Date getCreateDtm() {
        return createDtm;
    }

    public void setCreateDtm(Date createDtm) {
        this.createDtm = createDtm;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}