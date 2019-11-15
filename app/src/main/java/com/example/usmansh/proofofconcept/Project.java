package com.example.usmansh.proofofconcept;

public class Project {

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }


    public int getSyear() {
        return syear;
    }

    public void setSyear(int syear) {
        this.syear = syear;
    }

    public int getSmonth() {
        return smonth;
    }

    public void setSmonth(int smonth) {
        this.smonth = smonth;
    }

    public int getSday() {
        return sday;
    }

    public void setSday(int sday) {
        this.sday = sday;
    }

    public int getShour() {
        return shour;
    }

    public void setShour(int shour) {
        this.shour = shour;
    }

    public int getSminute() {
        return sminute;
    }

    public void setSminute(int sminute) {
        this.sminute = sminute;
    }

    public int getEyear() {
        return eyear;
    }

    public void setEyear(int eyear) {
        this.eyear = eyear;
    }

    public int getEmonth() {
        return emonth;
    }

    public void setEmonth(int emonth) {
        this.emonth = emonth;
    }

    public int getEday() {
        return eday;
    }

    public void setEday(int eday) {
        this.eday = eday;
    }

    public int getEhour() {
        return ehour;
    }

    public void setEhour(int ehour) {
        this.ehour = ehour;
    }

    public int getEminute() {
        return eminute;
    }

    public void setEminute(int eminute) {
        this.eminute = eminute;
    }

    String proname;

    public String getPromanager() {
        return promanager;
    }

    public void setPromanager(String promanager) {
        this.promanager = promanager;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    String promanager;
    String isactive;
    int syear,smonth,sday,shour,sminute;
    int eyear,emonth,eday,ehour,eminute;

    public ProReminder getProreminder() {
        return proreminder;
    }

    public void setProreminder(ProReminder proreminder) {
        this.proreminder = proreminder;
    }

    ProReminder proreminder;

}
