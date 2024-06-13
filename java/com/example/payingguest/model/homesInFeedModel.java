package com.example.payingguest.model;

public class homesInFeedModel {
    String avail, pId,date,time,image,roomType,Meals,Amenities,gender,sharetype,monthlyRent,securityDeposit,city,locality,description,contactNo,foodPreference,guestsAllowed,Publisher,publishr,user;

//    private boolean available;
//    public boolean isAvailable() {
////        return abc.equals("true") ? true : false;
//        return available;
//    }
//
//    public void setAvailable(boolean available) {
//        this.available = available;
//    }
//    private boolean isBooked;
//
//    public boolean isBooked() {
//        return isBooked;
//    }
//
//    public void setBooked(boolean isBooked) {
//        this.isBooked = isBooked;
//    }
    private boolean isWishlisted;

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }
    public homesInFeedModel() {
    }



    public homesInFeedModel(String avail,String pId, String date, String time, String image, String roomType, String Meals, String Amenities, String gender, String sharetype, String monthlyRent, String securityDeposit, String city, String locality, String description, String contactNo, String foodPreference, String guestsAllowed, String publisher, String publishr, String user) {
        Publisher = publisher;
        this.avail=avail;
        this.pId = pId;
        this.date = date;
        this.time = time;
        this.image = image;
        this.roomType = roomType;
       this.Meals=Meals;
       this.Amenities=Amenities;
        this.gender = gender;
        this.sharetype = sharetype;
        this.monthlyRent = monthlyRent;
        this.securityDeposit = securityDeposit;
        this.city = city;
        this.locality = locality;
        this.description=description;
        this.contactNo=contactNo;
        this.foodPreference = foodPreference;
        this.guestsAllowed = guestsAllowed;
        this.publishr=publishr;
        this.user=user;


    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getMeals() {
        return Meals;
    }

    public void setMeals(String meals) {
        Meals = meals;
    }

    public String getAmenities() {
        return Amenities;
    }

    public void setAmenities(String amenities) {
        Amenities = amenities;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
    }

    public String getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(String monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(String foodPreference) {
        this.foodPreference = foodPreference;
    }

    public String getGuestsAllowed() {
        return guestsAllowed;
    }

    public void setGuestsAllowed(String guestsAllowed) {
        this.guestsAllowed = guestsAllowed;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPublishr() {
        return publishr;
    }

    public void setPublishr(String publishr) {
        this.publishr = publishr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }
    public boolean isAvailable() {
        return "true".equals(avail);
    }
}
