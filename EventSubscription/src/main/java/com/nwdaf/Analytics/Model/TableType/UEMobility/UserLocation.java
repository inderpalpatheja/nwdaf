package com.nwdaf.Analytics.Model.TableType.UEMobility;
/*
 * @created 17/03/2020 - 10:46 AM
 * @project EventSubscription
 * @author  sheetalkumar
 */

public class UserLocation {
    private int ID;
    private String tai;
    private String cellID;
    private Integer timeDuration;

//    @Override
//    public String toString() {
//        return "UserLocation{" +
//                "ID=" + ID +
//                ", tai='" + tai + '\'' +
//                ", cellID='" + cellID + '\'' +
//                ", timeDuration=" + timeDuration +
//                '}';
//    }

    public UserLocation() {
    }
//    public int getID() {
//        return ID;
//    }

//    public void setID(int ID) {
//        this.ID = ID;
//    }

    public String getTai() {
        return tai;
    }

    public void setTai(String tai) {
        this.tai = tai;
    }

    public String getCellID() {
        return cellID;
    }

    public void setCellID(String cellID) {
        this.cellID = cellID;
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        this.timeDuration = timeDuration;
    }

    public UserLocation(int ID, String tai, String cellID, Integer timeDuration) {
        this.ID = ID;
        this.tai = tai;
        this.cellID = cellID;
        this.timeDuration = timeDuration;
    }
}
