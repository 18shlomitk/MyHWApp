package com.example.shlomit.myhwapp;

/**
 * Created by shlomit on 07/02/2018.
 */

public class Course {

    private String _cName;
    private String _cDate;
    private String _cDetails;



    public Course() {
    }

    public Course(String _cName, String _cDate, String _cDetails) {
        this._cName = _cName;
        this._cDate = _cDate;
        this._cDetails = _cDetails;
    }

    public String get_cName() {
        return _cName;
    }

    public void set_cName(String _cName) {
        this._cName = _cName;
    }

    public String get_cDate() {
        return _cDate;
    }

    public void set_cDate(String _cDate) {
        this._cDate = _cDate;
    }

    public String get_cDetails() {
        return _cDetails;
    }

    public void set_cDetails(String _cDetails) {
        this._cDetails = _cDetails;
    }
}