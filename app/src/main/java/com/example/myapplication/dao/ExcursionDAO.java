package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE vacationID = :vacationID ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacationID);

    @Query("SELECT COUNT(*) FROM excursions WHERE vacationID = :vacationID")
    int countByVacationId(int vacationID);

    @Query("SELECT IFNULL(MAX(excursionID), 0) FROM excursions")
    int getMaxExcursionId();
}
