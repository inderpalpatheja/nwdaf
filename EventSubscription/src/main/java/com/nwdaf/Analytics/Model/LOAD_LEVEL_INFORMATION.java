package com.nwdaf.Analytics.Model;

public class LOAD_LEVEL_INFORMATION {

  private String snssais;
  private int currentLoadLevel;

    public LOAD_LEVEL_INFORMATION() {
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

    public LOAD_LEVEL_INFORMATION(String snssais, int currentLoadLevel) {
        this.snssais = snssais;
        this.currentLoadLevel = currentLoadLevel;
    }
}
