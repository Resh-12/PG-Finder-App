package com.example.payingguest.model;

public class BookModel {
    String Rent,Roomtype,BookedDate,user,Amountpaid,Image,pId;
    public BookModel(){

    }

    public BookModel(String rent, String roomtype, String bookedDate, String user,String amountpaid,String image,String pId) {
        Rent = rent;
        Roomtype = roomtype;
        BookedDate = bookedDate;
        this.user = user;
        Amountpaid=amountpaid;
        Image=image;
        this.pId=pId;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String rent) {
        Rent = rent;
    }

    public String getRoomtype() {
        return Roomtype;
    }

    public void setRoomtype(String roomtype) {
        Roomtype = roomtype;
    }

    public String getBookedDate() {
        return BookedDate;
    }

    public void setBookedDate(String bookedDate) {
        BookedDate = bookedDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAmountpaid() {
        return Amountpaid;
    }

    public void setAmountpaid(String amountpaid) {
        Amountpaid = amountpaid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
