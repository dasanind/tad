package com.research.tad;

import android.os.Parcel;
import android.os.Parcelable;


public class UserDetailsClass implements Parcelable
{
    public String name,email,uid,country,state,city,accType,profilePicture,description,firstName,lastName,address1,address2, zipCode, contactNumber;

    public UserDetailsClass() {

    }

    public UserDetailsClass(Parcel in ) {
        this.name = in.readString();
        this.email = in.readString();
        this.uid = in.readString();
        this.country=in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.accType = in.readString();
        this.description=in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.address1= in.readString();
        this.address2= in.readString();
        this.zipCode= in.readString();
        this.contactNumber=in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.uid);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.accType);
        dest.writeString(this.description);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
        dest.writeString(this.zipCode);
        dest.writeString(this.contactNumber);
    }
    public static final Creator<UserDetailsClass> CREATOR = new Creator<UserDetailsClass>() {
        @Override
        public UserDetailsClass createFromParcel(Parcel in) {
            return new UserDetailsClass(in);
        }

        @Override
        public UserDetailsClass[] newArray(int size) {
            return new UserDetailsClass[size];
        }
    };

    public void setName(String name){this.name= name;}
    public void setCountry(String country){this.country = country;}
    public void setState(String state){this.state = state;}
    public void setCity(String city){this.city = city;}
    public void setEmail(String email){this.email=email;}
    public void setUid(String uid){this.uid= uid;}
    public void setAccType(String accType){this.accType= accType;}
    public void setDescription(String desc) {this.description=desc;}
    public String getName(){return name;}
    public String getCountry(){return this.country;}
    public String getState(){return this.state;}
    public String getCity(){return this.city;}
    public String getEmail(){return email;}
    public String getUid(){return uid;}
    public String getAccType(){return accType;}
    public String getDescription(){ return this.description;}
    public void setFirstName(String firstNameStr){this.firstName = firstNameStr;}
    public String getFirstName(){return firstName;}
    public void setLastName(String lastNameStr){this.lastName = lastNameStr;}
    public String getLastName(){return lastName;}
    public void setAddress1(String address1Str){this.address1 = address1Str;}
    public String getAddress1(){return address1;}
    public void setAddress2(String address2Str){this.address2 = address2Str;}
    public String getAddress2(){return address2;}
    public void setZipCode(String code){this.zipCode = code;}
    public String getZipCode(){return zipCode;}
    public void setContactNumber(String number){this.contactNumber = number;}
    public String getContactNumber(){return contactNumber;}


}
