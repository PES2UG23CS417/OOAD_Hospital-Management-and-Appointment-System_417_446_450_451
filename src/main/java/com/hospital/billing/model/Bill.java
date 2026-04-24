package com.hospital.billing.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "bills")
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private Long patientId;
    @Column(nullable=false) private String patientName;
    @Column(nullable=false) private Integer patientAge;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="bill_items", joinColumns=@JoinColumn(name="bill_id"))
    private List<BillItem> billItems;
    @Column(nullable=false) private Double grossAmount;
    @Column(nullable=false) private String discountType;
    @Column(nullable=false) private Double discountPercentage;
    @Column(nullable=false) private Double netAmount;
    private String paymentMethod;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private BillStatus status;
    @Column(nullable=false) private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist  public void prePersist()  { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate   public void preUpdate()   { updatedAt = LocalDateTime.now(); }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getPatientId(){return patientId;} public void setPatientId(Long p){this.patientId=p;}
    public String getPatientName(){return patientName;} public void setPatientName(String n){this.patientName=n;}
    public Integer getPatientAge(){return patientAge;} public void setPatientAge(Integer a){this.patientAge=a;}
    public List<BillItem> getBillItems(){return billItems;} public void setBillItems(List<BillItem> b){this.billItems=b;}
    public Double getGrossAmount(){return grossAmount;} public void setGrossAmount(Double g){this.grossAmount=g;}
    public String getDiscountType(){return discountType;} public void setDiscountType(String d){this.discountType=d;}
    public Double getDiscountPercentage(){return discountPercentage;} public void setDiscountPercentage(Double d){this.discountPercentage=d;}
    public Double getNetAmount(){return netAmount;} public void setNetAmount(Double n){this.netAmount=n;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String p){this.paymentMethod=p;}
    public BillStatus getStatus(){return status;} public void setStatus(BillStatus s){this.status=s;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime u){this.updatedAt=u;}
}
