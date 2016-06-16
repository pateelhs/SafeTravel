package com.agiledge.keocometemployee.dto;

public class EmployeeDto {

    private final String displayname;
    private final String emailaddress;
    private final String empid;
    private final String gender;


    public EmployeeDto(String displayname,String emailaddress,String empid,String gender) {
        this.displayname = displayname;
        this.emailaddress=emailaddress;
        this.empid=empid;
        this.gender=gender;

    }

    public String getGender() {
        return gender;
    }

    public String getEmpid() {
        return empid;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getDisplayname() {
        return displayname;
    }
}