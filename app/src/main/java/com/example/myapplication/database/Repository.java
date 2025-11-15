package com.example.myapplication.database;

import android.app.Application;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.util.List;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;

    private List<Vacation> mAllVacations;
    private  List<Excursion> mAllExcursions;

    public Repository(Application application){
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    // ---- Vacations ----
    public List<Vacation> getmAllVacations() {
        return mVacationDAO.getAllVacations();
    }

    public void insert(Vacation vacation) {
        mVacationDAO.insert(vacation);
    }

    public void update(Vacation vacation) {
        mVacationDAO.update(vacation);
    }

    public boolean deleteVacationSafely(Vacation vacation) {
        int count = mExcursionDAO.countByVacationId(vacation.getVacationID());
        if (count > 0) return false;
        mVacationDAO.delete(vacation);
        return true;
    }

    // ---- Excursions ----
    public List<Excursion> getAllExcursions() {
        return mExcursionDAO.getAllExcursions();
    }

    public List<Excursion> getExcursionsForVacation(int vacationID) {
        return mExcursionDAO.getAssociatedExcursions(vacationID);
    }

    public void insert(Excursion excursion) {
        mExcursionDAO.insert(excursion);
    }

    public void update(Excursion excursion) {
        mExcursionDAO.update(excursion);
    }

    public void delete(Excursion excursion) {
        mExcursionDAO.delete(excursion);
    }

    public int nextExcursionId() {
        return mExcursionDAO.getMaxExcursionId() + 1;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        return mExcursionDAO.getAssociatedExcursions(vacationID);
    }

    public Vacation getVacationById(int id) {
        return mVacationDAO.getVacationById(id);
    }

}
