package wanderhub.server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.community.dto.BoardDto;
import wanderhub.server.domain.community_comment.dto.BoCommentDto;
import wanderhub.server.domain.member.dto.MemberDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponseDto {
    private MemberDto.GetResponse data; // 멤버 정보
    private PageResponseDto<BoardDto.Response> boardResponseDtos;           // 작성한 게시판 목록
    private PageResponseDto<BoCommentDto.Response> boCommentResponseDtos;   // 작성한 댓글 목록
    private PageResponseDto<AccompanyResponseDto> createAccompanyDtos;      // 만든 동행 목록
    private PageResponseDto<AccompanyResponseDto> joinedAccompanyDtos;      // 참여한 동행 목록
}