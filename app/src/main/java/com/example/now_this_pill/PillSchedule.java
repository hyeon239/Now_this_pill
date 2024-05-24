package com.example.now_this_pill;

public class PillSchedule {
    private String pillName;
    private int pillCount;
    private int dosageCount;
    private String time1;
    private String time2;
    private String time3;
    private String time4;
    private String time5;
    private String memo;

    public PillSchedule(String pillName, int pillCount, int dosageCount, String time1, String time2, String time3, String time4, String time5, String memo) {
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.dosageCount = dosageCount;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.time4 = time4;
        this.time5 = time5;
        this.memo = memo;
    }

    // Getter와 Setter 메서드를 구현해야 합니다.
    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getPillCount() {
        return pillCount;
    }

    public void setPillCount(int pillCount) {
        this.pillCount = pillCount;
    }

    public int getDosageCount() {
        return dosageCount;
    }

    public void setDosageCount(int dosageCount) {
        this.dosageCount = dosageCount;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getTime4() {
        return time4;
    }

    public void setTime4(String time4) {
        this.time4 = time4;
    }

    public String getTime5() {
        return time5;
    }

    public void setTime5(String time5) {
        this.time5 = time5;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}