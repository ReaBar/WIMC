package com.example.reabar.wimc.Model;

import android.media.Image;

import java.util.Date;

/**
 * Created by reabar on 25.5.2016.
 */
public class Parking {
    private String parkingObjectId, carId, street, city, parkingLotName,parkingLotRowColor;
    private String streetNumber, parkingLotFloor;
    private double latitude, longitude;
    private boolean parkingIsActive;
    private Image parkingImage;
    private Date startParking;

    public Parking(){}

    private Parking(ParkingBuilder parkingBuilder){
        this.carId = parkingBuilder.carId;
        this.street = parkingBuilder.street;
        this.city = parkingBuilder.city;
        this.parkingLotName = parkingBuilder.parkingLotName;
        this.parkingLotRowColor = parkingBuilder.parkingLotRowColor;
        this.streetNumber = parkingBuilder.streetNumber;
        this.parkingLotFloor = parkingBuilder.parkingLotFloor;
        this.latitude = parkingBuilder.latitude;
        this.longitude = parkingBuilder.longitude;
        this.parkingImage = parkingBuilder.parkingImage;
        this.parkingIsActive = parkingBuilder.parkingIsActive;
        this.startParking = parkingBuilder.startParking;
    }

    public void setParkingObjectId(String parkingObjectId) {
        this.parkingObjectId = parkingObjectId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public String getParkingLotRowColor() {
        return parkingLotRowColor;
    }

    public void setParkingLotRowColor(String parkingLotRowColor) {
        this.parkingLotRowColor = parkingLotRowColor;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }


    public String getParkingLotFloor() {
        return parkingLotFloor;
    }

    public void setParkingLotFloor(String parkingLotFloor) {
        this.parkingLotFloor = parkingLotFloor;
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

    public boolean isParkingIsActive() {
        return parkingIsActive;
    }

    public void setParkingIsActive(boolean parkingIsActive) {
        this.parkingIsActive = parkingIsActive;
    }

    public Image getParkingImage() {
        return parkingImage;
    }

    public void setParkingImage(Image parkingImage) {
        this.parkingImage = parkingImage;
    }


    public void setStartParking(Date startParking) { this.startParking = startParking; }
    public Date getStartParking() { return this.startParking; }

    public static class ParkingBuilder {
        private String carId, street, city, parkingLotName,parkingLotRowColor;
        private String streetNumber, parkingLotNumber, parkingLotFloor;
        private double latitude, longitude;
        private boolean parkingIsActive;
        private Image parkingImage;
        private Date startParking;

        public ParkingBuilder(String carId) {
            this.carId = carId;
            this.parkingIsActive = true;
        }

        public ParkingBuilder parkingLonitude(double longitude){
            this.longitude = longitude;
            return this;
        }

        public ParkingBuilder parkingLatitude(double latitude){
            this.latitude = latitude;
            return this;
        }

        public ParkingBuilder parkingLotFloor(String parkingLotFloor){
            this.parkingLotFloor = parkingLotFloor;
            return this;
        }

        public ParkingBuilder parkingImage(Image parkingImage){
            this.parkingImage = parkingImage;
            return this;
        }

        public ParkingBuilder street(String street){
            this.street = street;
            return this;
        }

        public ParkingBuilder city(String city){
            this.city = city;
            return this;
        }

        public ParkingBuilder parkingLotName(String parkingLotName){
            this.parkingLotName = parkingLotName;
            return this;
        }

        public ParkingBuilder parkingLotRowColor(String parkingLotRowColor){
            this.parkingLotRowColor = parkingLotRowColor;
            return this;
        }

        public ParkingBuilder streetNumber(String streetNumber){
            this.streetNumber = streetNumber;
            return this;
        }

        public ParkingBuilder startParking(Date startParking){
            this.startParking = startParking;
            return this;
        }


        public Parking build() {
            return new Parking(this);
        }
    }
}
