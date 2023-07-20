package wanderhub.server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.BoardTempDto;
import wanderhub.server.domain.member.dto.MemberDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponseDto {
    private MemberDto.GetResponse data; // 멤버 정보
    private PageResponseDto<BoardListResponseDto> boardResponseDtos;           // 작성한 게시판 목록
    private PageResponseDto<BoCommentResponseDto> boCommentResponseDtos;   // 작성한 댓글 목록
}
