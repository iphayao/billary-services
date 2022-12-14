package th.co.readypaper.billary.ompanies.company.model;

import lombok.Data;

@Data
public class CompanyDto {
    private String id;
    private String code;
    private String name;
    private String nameEn;
    private String address;
    private String addressEn;
    private String taxId;
    private String phone;
    private String website;
    private String office;
}
