package com.cafedemetro.pos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name="Branches.getBranchByCode", query="select b from Branches b where b.branchCode = :branchCode")
public class Branches implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bId;

    @Column(nullable = false, length = 10, unique = true)
    private String branchCode;
    
    @Column(nullable = false, length = 50)
    private String branchName;

    @Column(nullable = false)
    private int totalSeats;

    @OneToMany(mappedBy = "branch", fetch = FetchType.EAGER)
    private List<Orders> orders;    

    public long getbId() {
        return bId;
    }

    public void setbId(long bId) {
        this.bId = bId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
}