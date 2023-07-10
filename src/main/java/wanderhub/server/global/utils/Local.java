package wanderhub.server.global.utils;

import lombok.Getter;

public enum Local {
    서울("서울"),
    제주도("제주도"),
    경기도("경기도"),
    강원도("강원도"),
    부산("부산"),
    울산("울산"),
    포항("포항"),
    대구("대구"),
    대전("대전"),
    광주("광주"),
    세종("세종"),
    인천("인천"),
    충청남도("충청남도"),
    충청북도("충청북도"),
    경상남도("경상남도"),
    경상북도("경상북도"),
    전라남도("전라남도"),
    전라북도("전라북도"),
    X("선택 없음");


    @Getter
    private String localString;

    Local(String local) {
        this.localString = local;
    }

    public static Local findByLocal(String local) {
        if(local == null) return X;
        for (Local status : values()) {
            if (local.equals(status.getLocalString())) {
                return status;
            }
        } return X;
    }
}
