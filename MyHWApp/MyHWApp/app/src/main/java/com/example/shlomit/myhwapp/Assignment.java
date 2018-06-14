package com.example.shlomit.myhwapp;

/**
 * Created by shlomit on 07/02/2018.
 */

public class Assignment {
    private String _aName;
    private String _aDate;
    private String _aDetails;

    public Assignment(String _aName, String _aDate, String _aDetails) {
        this._aName = _aName;
        this._aDate = _aDate;
        this._aDetails = _aDetails;
    }

    public Assignment() {
    }

    public String get_aName() {
        return _aName;
    }

    public void set_aName(String _aName) {
        this._aName = _aName;
    }

    public String get_aDate() {
        return _aDate;
    }

    public void set_aDate(String _aDate) {
        this._aDate = _aDate;
    }

    public String get_aDetails() {
        return _aDetails;
    }

    public void set_aDetails(String _aDetails) {
        this._aDetails = _aDetails;
    }
}