package com.example.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;

    @ColumnInfo(name = "vacationName")
    private String vacationName;

    @ColumnInfo(name = "hotel")
    private String hotel;

    @ColumnInfo(name = "startDate")
    private String startDate;

    @ColumnInfo(name = "endDate")
    private String endDate;

    @ColumnInfo(name = "price")
    private Double price;

    public Vacation() { }

    @Ignore
    public Vacation(int vacationID, String vacationName, String hotel, String startDate, String endDate, Double price) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public int getVacationID() { return vacationID; }
    public void setVacationID(int vacationID) { this.vacationID = vacationID; }

    public String getVacationName() { return vacationName; }
    public void setVacationName(String vacationName) { this.vacationName = vacationName; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
