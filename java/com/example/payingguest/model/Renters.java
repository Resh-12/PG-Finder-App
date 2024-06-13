package com.example.payingguest.model;

public class Renters {
    String RenterName, ContactNo, Address;

    public Renters(String renterName, String contactNo, String address) {
        RenterName = renterName;
        ContactNo = contactNo;
        Address = address;
    }

    public String getRenterName() {
        return RenterName;
    }

    public void setRenterName(String renterName) {
        RenterName = renterName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}