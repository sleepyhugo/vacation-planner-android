package com.example.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private int vacationID;
    private String excursionDate;

    private Double price;

    public Excursion(int excursionID, String excursionName, int vacationID, Double price) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.vacationID = vacationID;
        this.price = price;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
    public String getExcursionDate() {
        return excursionDate;
    }
    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
