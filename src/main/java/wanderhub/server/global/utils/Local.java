package wanderhub.server.global.utils;

import lombok.Getter;

public enum Local {
    Seoul("서울"),
    Jeju("제주"),
    Gyeonggi("경기"),
    Gangwon("강원"),
    Busan("부산"),
    Ulsan("울산"),
    Pohang("포항"),
    Daegu("대구"),
    Daejeon("대전"),
    Gwangju("광주"),
    Sejong("세종"),
    Incheon("인천"),
    Chungcheongnam("충청남도"),
    Chungcheongbuk("충청북도"),
    Gyeongsangnam("경상남도"),
    Gyeongsangbuk("경상북도"),
    Jeollanam("전라남도"),
    Jeollabuk("전라북도"),
    NotSelected("Not selected");


    @Getter
    private String localString;

    Local(String localString) {
        this.localString = localString;
    }

    public static Local getLocal(String localString) {
        for (Local local : values()) {
            if (localString.equals(local.getLocalString())) {
                return local;
            }
        } return NotSelected;
    }
}
