package com.hospital.billing.dto;
import com.hospital.billing.model.BillItem;
import com.hospital.billing.model.BillStatus;
import java.time.LocalDateTime;
import java.util.List;

public class BillResponseDTO {
    private Long billId;
    private String patientName;
    private Integer patientAge;
    private List<BillItem> billItems;
    private Double grossAmount, discountPercentage, netAmount;
    private String discountType, paymentMethod;
    private BillStatus status;
    private LocalDateTime createdAt;

    public Long getBillId(){return billId;} public void setBillId(Long b){this.billId=b;}
    public String getPatientName(){return patientName;} public void setPatientName(String n){this.patientName=n;}
    public Integer getPatientAge(){return patientAge;} public void setPatientAge(Integer a){this.patientAge=a;}
    public List<BillItem> getBillItems(){return billItems;} public void setBillItems(List<BillItem> b){this.billItems=b;}
    public Double getGrossAmount(){return grossAmount;} public void setGrossAmount(Double g){this.grossAmount=g;}
    public Double getDiscountPercentage(){return discountPercentage;} public void setDiscountPercentage(Double d){this.discountPercentage=d;}
    public Double getNetAmount(){return netAmount;} public void setNetAmount(Double n){this.netAmount=n;}
    public String getDiscountType(){return discountType;} public void setDiscountType(String d){this.discountType=d;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String p){this.paymentMethod=p;}
    public BillStatus getStatus(){return status;} public void setStatus(BillStatus s){this.status=s;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
}
