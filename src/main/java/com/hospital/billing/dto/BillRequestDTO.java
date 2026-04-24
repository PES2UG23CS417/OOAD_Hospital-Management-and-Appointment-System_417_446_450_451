package com.hospital.billing.dto;
import com.hospital.billing.model.BillItem;
import jakarta.validation.constraints.*;
import java.util.List;

public class BillRequestDTO {
    @NotNull(message="Patient ID required") private Long patientId;
    @NotBlank(message="Patient name required") private String patientName;
    @NotNull @Min(0) private Integer patientAge;
    private boolean insuranceApplicable;
    private String insuranceProvider;
    @NotEmpty(message="At least one item required") private List<BillItem> billItems;

    public Long getPatientId(){return patientId;} public void setPatientId(Long p){this.patientId=p;}
    public String getPatientName(){return patientName;} public void setPatientName(String n){this.patientName=n;}
    public Integer getPatientAge(){return patientAge;} public void setPatientAge(Integer a){this.patientAge=a;}
    public boolean isInsuranceApplicable(){return insuranceApplicable;} public void setInsuranceApplicable(boolean i){this.insuranceApplicable=i;}
    public String getInsuranceProvider(){return insuranceProvider;} public void setInsuranceProvider(String i){this.insuranceProvider=i;}
    public List<BillItem> getBillItems(){return billItems;} public void setBillItems(List<BillItem> b){this.billItems=b;}
}
