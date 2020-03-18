package com.nwdaf.Analytics.Model.TableType.LoadLevelInformation;

public class SliceLoadLevelInformation {

    private String snssais;
    private Integer currentLoadLevel;

    public SliceLoadLevelInformation() {
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public Integer getCurrentLoadLevel() {
        return currentLoadLevel;
    }

    public void setCurrentLoadLevel(Integer currentLoadLevel) {
        this.currentLoadLevel = currentLoadLevel;
    }

    public SliceLoadLevelInformation(String snssais, Integer currentLoadLevel) {
        this.snssais = snssais;
        this.currentLoadLevel = currentLoadLevel;
    }
}
