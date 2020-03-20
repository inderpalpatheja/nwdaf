package com.nwdaf.Analytics.Model;

public class UserLocation {


    private int ID;
    private String Tai;
    private String cellID;
    private int timeDuration;

    public UserLocation() {
    }

    public UserLocation(int ID, String tai, String cellID, int timeDuration) {
        this.ID = ID;
        Tai = tai;
        this.cellID = cellID;
        this.timeDuration = timeDuration;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTai() {
        return Tai;
    }

    public void setTai(String tai) {
        Tai = tai;
    }

    public String getCellID() {
        return cellID;
    }

    public void setCellID(String cellID) {
        this.cellID = cellID;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(int timeDuration) {
        this.timeDuration = timeDuration;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "ID=" + ID +
                ", Tai='" + Tai + '\'' +
                ", cellID='" + cellID + '\'' +
                ", timeDuration=" + timeDuration +
                '}';
    }
}
