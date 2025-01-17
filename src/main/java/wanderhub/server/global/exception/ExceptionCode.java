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
    BOARD_COMMENT_WRITER_DIFFERENT(404, "Writer Different"),

    ACCOMPANY_NOT_FOUND(404, "Accompany not found"),
    ACCOMPANY_WRITER_DIFFERENT(404, "Writer Different"),

    ACCOMPANY_JOIN_MAX(404, "Join Member Max"),
    ACCOMPANY_JOIN_ALREADY_JOINED(404, "You are Already joined this Accompany"),
    ACCOMPANY_WRITER(404,"You are Writer"),
    ACCOMPANY_CANNOT_QUIT(404, "Not Joined Member"),

    TOKEN_WITHOUT(401, "Token is without"), // 토큰 없음
    TOKEN_INVALID(401, "Token is invalid"), // 유효한 인증이 아닌 경우 401
    TOKEN_EXPIRED(401, "Token is expired"),

    REFRESH_TOKEN_INVALID(401, "RefreshToken is Invalid"),
    REFRESH_TOKEN_WITHOUT(401, "RefreshToken is Without"),
    LOGOUT_TOKEN(401, "LogOuted Token!!"),

    TRIP_PLAN_NOT_FOUND(404, "TripPlan Not Found"),
    TRIP_PLAN_DIFFERENT_WRITER(404, "Writer is Different"),

    TRIP_PLAN_DETAIL_NOT_FOUND(404, "TripPlanDetail Not Found"),

    DATE_INVALID(404, "Check StartDate & EndDate"),
    TIME_INVALID(404, "Check StartTime & EndTime");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}