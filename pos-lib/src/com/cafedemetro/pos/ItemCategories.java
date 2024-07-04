package com.cafedemetro.pos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Entity;

@Entity
public class ItemCategories implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long icId;

    @Column(nullable = false, length = 50)
    private String icName;

    @Column(nullable = false, unique = true, length = 10)
    private String icCode;

    @OneToMany(mappedBy = "itemCategory", fetch = FetchType.EAGER)
    private List<Items> items;

    public long getIcId() {
        return icId;
    }

    public void setIcId(long icId) {
        this.icId = icId;
    }

    public String getIcName() {
        return icName;
    }

    public void setIcName(String icName) {
        this.icName = icName;
    }

    public String getIcCode() {
        return icCode;
    }

    public void setIcCode(String icCode) {
        this.icCode = icCode;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
    
}