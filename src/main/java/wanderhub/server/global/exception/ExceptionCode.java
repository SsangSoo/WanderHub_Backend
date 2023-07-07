package wanderhub.server.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    LOCAL_ILLEGAL_ARGUMENT_LOCAL(404, "Local is ILLEGAL_ARGUMENT"),

    NICKNAME_REQUIRED(400,"Nickname is required"),
    NICKNAME_NOT_UPDATE(400, "Nickname is never update"),
    NICKNAME_DUPLICATED(404, "Nickname Duplicated"),

    MEMBER_ALREADY_HUMAN(400, "Member is already Human"),
    MEMBER_NOT_FOUND(404, "Member is not found"),
    MEMBER_NOT_ACTIVE(404,"Member is not ACTIVE"),
    MEMBER_EXISTS(409, "Member exists"),

    BOARD_NOT_FOUND(404, "Board is not found"),
    BOARD_WRITER_DIFFERENT(404, "Writer Different"),

    BOARD_COMMENT_NOT_FOUND(404, "BoardComment is not found"),
    BOARD_COMMNET_WRITER_DIFFERENT(404, "Writer Different"),

    ACCOMPANY_NOT_FOUND(404, "Accompany not found"),
    ACCOMPANY_WRITER_DIFFERENT(404, "Writer Different"),

    ACCOMPANY_JOIN_MAX_NUM_OVER(404, "Max num over"),
    ACCOMPANY_JOIN_ALREADY_JOINED(404, "Already joined"),
    ACCOMPANY_JOIN_CANNOT_QUIT(404, "Cannot quit as you made"),
    ACCOMPANY_JOIN_NOT_A_MEMBER(404, "Cannot quit as not a member"),

    TOKEN_WITHOUT(401, "Token is without"), // 토큰 없음
    TOKEN_INVALID(401, "Token is invalid"), // 유효한 인증이 아닌 경우 401
    TOKEN_EXPIRED(401, "Token is expired"),

    REFRESH_TOKEN_INVALID(401, "RefreshToken is Invalid"),
    REFRESH_TOKNE_WITHOUT(401, "RefreshToken is Without"),
    LOGOUT_TOKEN(401, "LogOuted Token!!");


    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
