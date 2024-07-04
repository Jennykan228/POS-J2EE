package com.cafedemetro.pos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Entity;

@Entity
public class Items implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @ManyToOne
    @JoinColumn(name = "icId")
    private ItemCategories itemCategory;

    @Column(nullable = false, length = 10, unique = true)
    private String itemCode;

    @Column(nullable = false, length = 50)
    private String itemName;

    @OneToMany(mappedBy = "items", fetch = FetchType.EAGER)
    private List<OrderDetails> details;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public ItemCategories getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategories itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<OrderDetails> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetails> details) {
        this.details = details;
    }   
}