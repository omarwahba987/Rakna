package com.example.omar.rakna.Users;

public class Garage  implements Comparable< Garage >{
    String id , name ,address ;
    int numOfsSpaces , numOfFreeSpaces,hourPrice;
    double latitude ,longitude ;
    Integer distance;

    public Garage() {
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Garage(String id, String name, String address, int numOfsSpaces, int numOfFreeSpaces, int hourPrice, double latitude, double longitude, Integer distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.numOfsSpaces = numOfsSpaces;
        this.numOfFreeSpaces = numOfFreeSpaces;
        this.hourPrice = hourPrice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumOfsSpaces() {
        return numOfsSpaces;
    }

    public void setNumOfsSpaces(int numOfsSpaces) {
        this.numOfsSpaces = numOfsSpaces;
    }

    public int getNumOfFreeSpaces() {
        return numOfFreeSpaces;
    }

    public void setNumOfFreeSpaces(int numOfFreeSpaces) {
        this.numOfFreeSpaces = numOfFreeSpaces;
    }

    public int getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(int hourPrice) {
        this.hourPrice = hourPrice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    @Override
    public int compareTo(Garage o) {
        return this.getDistance().compareTo(o.getDistance());
    }
}
