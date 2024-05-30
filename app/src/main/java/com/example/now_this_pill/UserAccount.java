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
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private String time5;
        private String memo;        // 메모

        public PillSchedule1(String pillName, String weekdays, int pillCount, int pillFrequency, String time1, String time2, String time3, String time4, String time5, String memo) {

            this.pillName = pillName;
            this.weekdays = weekdays;
            this.pillCount = pillCount;
            this.pillFrequency = pillFrequency;
            this.time1 = time1;
            this.time2 = time2;
            this.time3 = time3;
            this.time4 = time4;
            this.time5 = time5;
            this.memo = memo;
        }

        // Getters and Setters
        public String getPillName() { return pillName; }
        public void setPillName(String pillName) { this.pillName = pillName; }

        public String getPillDay() { return weekdays; }

        public int getPillCount() { return pillCount; }
        public void setPillCount(int pillCount) { this.pillCount = pillCount; }

        public int getPillFrequency() { return pillFrequency; }
        public void setPillFrequency(int pillFrequency) { this.pillFrequency = pillFrequency; }

        public String getTime1() {
            return time1;
        }
        public String getTime2() {
            return time2;
        }
        public String getTime3() {
            return time3;
        }
        public String getTime4() {
            return time4;
        }
        public String getTime5() {
            return time5;
        }


        public String getMemo() { return memo; }
        public void setMemo(String memo) { this.memo = memo; }
    }
    public static class PillSchedule2 {
        private String pillName;    // 알약 이름
        private String weekdays;    // 복용 요일
        private int pillCount;      // 한 번에 복용하는 알약 수
        private int pillFrequency;  // 하루에 몇 번 복용하는지
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private String time5;
        private String memo;        // 메모

        public PillSchedule2(String pillName, String weekdays, int pillCount, int pillFrequency, String time1, String time2, String time3, String time4, String time5, String memo) {

            this.pillName = pillName;
            this.weekdays = weekdays;
            this.pillCount = pillCount;
            this.pillFrequency = pillFrequency;
            this.time1 = time1;
            this.time2 = time2;
            this.time3 = time3;
            this.time4 = time4;
            this.time5 = time5;
            this.memo = memo;
        }

        // Getters and Setters
        public String getPillName() { return pillName; }
        public void setPillName(String pillName) { this.pillName = pillName; }

        public String getPillDay() { return weekdays; }

        public int getPillCount() { return pillCount; }
        public void setPillCount(int pillCount) { this.pillCount = pillCount; }

        public int getPillFrequency() { return pillFrequency; }
        public void setPillFrequency(int pillFrequency) { this.pillFrequency = pillFrequency; }

        public String getTime1() {
            return time1;
        }
        public String getTime2() {
            return time2;
        }
        public String getTime3() {
            return time3;
        }
        public String getTime4() {
            return time4;
        }
        public String getTime5() {
            return time5;
        }


        public String getMemo() { return memo; }
        public void setMemo(String memo) { this.memo = memo; }
    }

    public static class PillSchedule3 {
        private String pillName;    // 알약 이름
        private String weekdays;    // 복용 요일
        private int pillFrequency;  // 하루에 몇 번 복용하는지
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private String time5;
        private String memo;        // 메모

        public PillSchedule3(String pillName, String weekdays, int pillFrequency, String time1, String time2, String time3, String time4, String time5, String memo) {

            this.pillName = pillName;
            this.weekdays = weekdays;
            this.pillFrequency = pillFrequency;
            this.time1 = time1;
            this.time2 = time2;
            this.time3 = time3;
            this.time4 = time4;
            this.time5 = time5;
            this.memo = memo;
        }

        // Getters and Setters
        public String getPillName() { return pillName; }
        public void setPillName(String pillName) { this.pillName = pillName; }

        public String getPillDay() { return weekdays; }

        public int getPillFrequency() { return pillFrequency; }
        public void setPillFrequency(int pillFrequency) { this.pillFrequency = pillFrequency; }

        public String getTime1() {
            return time1;
        }
        public String getTime2() {
            return time2;
        }
        public String getTime3() {
            return time3;
        }
        public String getTime4() {
            return time4;
        }
        public String getTime5() {
            return time5;
        }


        public String getMemo() { return memo; }
        public void setMemo(String memo) { this.memo = memo; }
    }

}