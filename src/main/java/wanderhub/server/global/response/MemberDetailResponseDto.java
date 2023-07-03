package wanderhub.server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.board.dto.BoardDto;
import wanderhub.server.domain.member.dto.MemberDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponseDto {
    private MemberDto.GetResponse data; // 멤버 정보
    private PageResponseDto<BoardDto.Response> boardResponseDtos;           // 작성한 게시판 목록
    private PageResponseDto<BoCommentDto.Response> boCommentResponseDtos;   // 작성한 댓글 목록
}
