package com.example.test;

public class Activity {
    private int activityId;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String introduction;
    private String mainLabel;
    private String subLabel;
    private String activityLabel;
    private String place;
    private String host;
    private String url;
    private int year;
    private int month;
    private int day;
    private String name;

    public Activity() {
        this.activityId = activityId;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.introduction = introduction;
        this.mainLabel = mainLabel;
        this.subLabel = subLabel;
        this.activityLabel = activityLabel;
        this.place = place;
        this.host = host;
        this.url = url;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public int getYear(){return year;}
    public int getMonth(){return month;}
    public int getDay(){return day;}
    public int getActivityId() {
        return activityId;
    }
    public String getIntroduction() { return introduction; }
    public String getPlace() {
        return place;
    }
    public String getHost() {
        return host;
    }
    public int getStartHour(){return startHour;}
    public int getStartMinute(){return startMinute;}
    public int getEndHour(){return endHour;}
    public int getEndMinute(){return endMinute;}
    public String getMainLabel(){return mainLabel;}
    public String getSubLabel(){return subLabel;}
    public String getActivityLabel(){return activityLabel;}
    public String getUrl(){return url; }
    public String getName(){return name;}
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) { this.day = day; }
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public void setMainLabel(String mainLabel) {
        this.mainLabel = mainLabel;
    }
    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }
    public void setActivityLabel(String activityLabel) { this.activityLabel = activityLabel; }
    public void setPlace(String place) { this.place = place; }
    public void setHost(String host) { this.host = host; }
    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }




}
