package wanderhub.server.domain.member.dto;

import lombok.*;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany_member.dto.AccompanyMemberResponseDto;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.community.dto.BoardDto;
import wanderhub.server.domain.community.entity.Board;
import wanderhub.server.domain.community_comment.dto.BoCommentDto;
import wanderhub.server.domain.community_comment.entity.BoComment;
import wanderhub.server.domain.member.entity.MemberStatus;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private String name;                // 이름 / null 허용
        private String nickName;            // 닉네임 / null, 공백, 빈문자 X
        @Lob
        private String imgUrl;              // 이미지 Url  / null 허용
        private String local;               // 지역 / null 허용


    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String name;
        private String email;
        private String nickName;
        @Lob
        private String imgUrl;
        private String local;
        private MemberStatus memberStatus;
        private boolean newbie;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    // 마이페이지 조회시
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetResponse {
        private String name;
        private String email;
        private String nickName;
        @Lob
        private String imgUrl;
        private String local;
        private MemberStatus memberStatus;
        private boolean newbie;
//        @Setter  // 내가 참여한 동행
//        private List<AccompanyMemberResponseDto> accompanyMembers;
//        @Setter
//        private List<BoardDto.Response> boardList;
//        @Setter
//        private List<BoCommentDto.Response> boCommentList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }


}
