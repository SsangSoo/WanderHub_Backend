package wanderhub.server.global.utils;

import lombok.Getter;

public enum Local {
    Seoul("Seoul"),
    Jeju("Jeju"),
    Gyeonggi("Gyeonggi"),
    Gangwon("Gangwon"),
    Busan("Busan"),
    Ulsan("Ulsan"),
    Pohang("Pohang"),
    Daegu("Daegu"),
    Daejeon("Daejeon"),
    Gwangju("Gwangju"),
    Sejong("Sejong"),
    Incheon("Incheon"),
    Chungcheongnam("Chungcheongnam"),
    Chungcheongbuk("Chungcheongbuk"),
    Gyeongsangnam("Gyeongsangnam"),
    Gyeongsangbuk("Gyeongsangbuk"),
    Jeollanam("Jeollanam"),
    Jeollabuk("Jeollabuk"),
    NotSelected("Not selected");


    @Getter
    private String localString;

    Local(String local) {
        this.localString = local;
    }

    public static Local findByLocal(String local) {
        for (Local status : values()) {
            if (local.equals(status.getLocalString())) {
                return status;
            }
        } return NotSelected;
    }
}
