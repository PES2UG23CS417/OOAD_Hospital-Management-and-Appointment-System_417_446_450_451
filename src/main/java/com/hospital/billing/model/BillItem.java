package com.hospital.billing.model;
import jakarta.persistence.Embeddable;

@Embeddable
public class BillItem {
    private String serviceName;
    private Double amount;
    public BillItem() {}
    public BillItem(String s, Double a) { serviceName = s; amount = a; }
    public String getServiceName(){return serviceName;} public void setServiceName(String s){this.serviceName=s;}
    public Double getAmount(){return amount;} public void setAmount(Double a){this.amount=a;}
}
