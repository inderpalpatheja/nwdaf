package com.nwdaf.Analytics.Model.TableType;

public class SliceLoadLevelInformation {

    private String snssais;
    private int currentLoadLevel;

    public SliceLoadLevelInformation() {
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public int getCurrentLoadLevel() {
        return currentLoadLevel;
    }

    public void setCurrentLoadLevel(int currentLoadLevel) {
        this.currentLoadLevel = currentLoadLevel;
    }

    public SliceLoadLevelInformation(String snssais, int currentLoadLevel) {
        this.snssais = snssais;
        this.currentLoadLevel = currentLoadLevel;
    }
}
