package com.example.now_this_pill;

import java.util.Random;
import java.util.ArrayList;

public class UserAccount {
    private String usertype;    // 복용자, 보호자 선택
    private String email;       // 이메일
    private String password;    // 비밀번호
    private String name;        // 이름
    private String idToken;
    private ArrayList<PillSchedule> pillSchedule1;
    private ArrayList<PillSchedule> pillSchedule2;
    private ArrayList<PillSchedule> pillSchedule3;

    public UserAccount() {
        generateIdToken(); // idToken을 생성하는 메서드 호출

        // 빈 생성자가 필요 (firebase 관련)
        pillSchedule1 = new ArrayList<>();
        pillSchedule2 = new ArrayList<>();
        pillSchedule3 = new ArrayList<>();
    }

    public String getUsertype() {
        return usertype;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    // 알약 주기 설정을 추가하는 메서드
    public void addPillSchedule1(PillSchedule schedule) {
        pillSchedule1.add(schedule);
    }

    public void addPillSchedule2(PillSchedule schedule) {
        pillSchedule2.add(schedule);
    }

    public void addPillSchedule3(PillSchedule schedule) {
        pillSchedule3.add(schedule);
    }

    // 알약 주기 설정을 가져오는 메서드
    public ArrayList<PillSchedule> getPillSchedules1() {
        return pillSchedule1;
    }

    public ArrayList<PillSchedule> getPillSchedules2() {
        return pillSchedule2;
    }

    public ArrayList<PillSchedule> getPillSchedules3() {
        return pillSchedule3;
    }

    // idToken을 생성하는 메서드
    private void generateIdToken() {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        int length = 6; // 원하는 idToken 길이를 6으로 지정해요

        // 숫자만으로 구성된 랜덤 문자열 생성
        for (int i = 0; i < length; i++) {
            builder.append(rand.nextInt(10)); // 0부터 9까지의 숫자 중에서 랜덤으로 선택
        }

        this.idToken = builder.toString();

    }



    public static class PillSchedule1 {
        private String pillName;    // 알약 이름
        private String weekdays;    // 복용 요일
        private int pillCount;      // 한 번에 복용하는 알약 수
        private int pillFrequency;  // 하루에 몇 번 복용하는지
        private int hour1, minute1; // 1번 시간
        private int hour2, minute2; // 2번 시간
        private int hour3, minute3; // 3번 시간
        private int hour4, minute4; // 4번 시간
        private int hour5, minute5; // 5번 시간
        private String memo;        // 메모

        public PillSchedule1(String pillName, String weekdays, int pillCount, int pillFrequency,
                             int hour1, int minute1, int hour2, int minute2,
                             int hour3, int minute3, int hour4, int minute4,
                             int hour5, int minute5, String memo) {
            this.pillName = pillName;
            this.weekdays = weekdays;
            this.pillCount = pillCount;
            this.pillFrequency = pillFrequency;
            this.hour1 = hour1;
            this.minute1 = minute1;
            this.hour2 = hour2;
            this.minute2 = minute2;
            this.hour3 = hour3;
            this.minute3 = minute3;
            this.hour4 = hour4;
            this.minute4 = minute4;
            this.hour5 = hour5;
            this.minute5 = minute5;
            this.memo = memo;
        }

        // Getters and Setters
        public String getPillName() { return pillName; }
        public void setPillName(String pillName) { this.pillName = pillName; }

        public String getPillDay() { return weekdays; }
        public void setPillDay(String pillDay) { this.weekdays = pillDay; }

        public int getPillCount() { return pillCount; }
        public void setPillCount(int pillCount) { this.pillCount = pillCount; }

        public int getPillFrequency() { return pillFrequency; }
        public void setPillFrequency(int pillFrequency) { this.pillFrequency = pillFrequency; }

        public int getHour1() { return hour1; }
        public void setHour1(int hour1) { this.hour1 = hour1; }

        public int getMinute1() { return minute1; }
        public void setMinute1(int minute1) { this.minute1 = minute1; }

        public int getHour2() { return hour2; }
        public void setHour2(int hour2) { this.hour2 = hour2; }

        public int getMinute2() { return minute2; }
        public void setMinute2(int minute2) { this.minute2 = minute2; }

        public int getHour3() { return hour3; }
        public void setHour3(int hour3) { this.hour3 = hour3; }

        public int getMinute3() { return minute3; }
        public void setMinute3(int minute3) { this.minute3 = minute3; }

        public int getHour4() { return hour4; }
        public void setHour4(int hour4) { this.hour4 = hour4; }

        public int getMinute4() { return minute4; }
        public void setMinute4(int minute4) { this.minute4 = minute4; }

        public int getHour5() { return hour5; }
        public void setHour5(int hour5) { this.hour5 = hour5; }

        public int getMinute5() { return minute5; }
        public void setMinute5(int minute5) { this.minute5 = minute5; }

        public String getMemo() { return memo; }
        public void setMemo(String memo) { this.memo = memo; }
    }
    public class PillSchedule2 {
        // PillSchedule1 클래스와 동일한 구조를 가지고 있음
        // 필요한 경우 수정 가능
    }

    public class PillSchedule3 {
        // PillSchedule1 클래스와 동일한 구조를 가지고 있음
        // 필요한 경우 수정 가능
    }
}