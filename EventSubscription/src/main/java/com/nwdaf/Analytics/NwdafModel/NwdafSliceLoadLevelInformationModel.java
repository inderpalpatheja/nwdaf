package com.nwdaf.Analytics.NwdafModel;

public class NwdafSliceLoadLevelInformationModel {

    private String snssais;
    private int currentLoadLevel;

    public NwdafSliceLoadLevelInformationModel() {
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

    public NwdafSliceLoadLevelInformationModel(String snssais, int currentLoadLevel) {
        this.snssais = snssais;
        this.currentLoadLevel = currentLoadLevel;
    }
}
