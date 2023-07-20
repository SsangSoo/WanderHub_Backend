package wanderhub.server.domain.board;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.board.controller.BoardController;
import wanderhub.server.domain.board.dto.BoardDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.BoardResponseDto;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.board.mapper.BoardMapper;
import wanderhub.server.domain.board.service.BoardService;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanderhub.server.global.utils.ApiDocumentUtils.getRequestPreProcessor;
import static wanderhub.server.global.utils.ApiDocumentUtils.getResponsePreProcessor;

@WebMvcTest(BoardController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private BoardMapper boardMapper;

    @MockBean
    private BoardService boardService;

    private static final String BASE_URL = "/v1/board";

    private static final LocalDateTime TIME = LocalDateTime.now();

    @Autowired
    private Gson gson;

    @DisplayName("게시판 작성")
    @WithMockUser
    @Test
    void boardCreateTest() throws Exception {
        Long boardId = 1L;
        LocalDateTime createdAt = TIME;
        LocalDateTime modifiedAt = createdAt;

        // given
        BoardDto.Post boarPost = BoardDto.Post.builder()
                .title("게시판 제목")
                .content("게시판 본문")
                .local("게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                .build();

        String boardPostToJson = gson.toJson(boarPost);

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .boardId(boardId)
                .nickName("작성자")
                .title("게시판 제목")
                .content("게시판 본문")
                .local("게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                .viewPoint(0L)
                .likePoint(0L)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boardMapper.boardPostDtoToBoardEntity(Mockito.any(BoardDto.Post.class))).willReturn(new Board());
        given(boardService.createBoard(Mockito.any(Board.class), Mockito.anyString())).willReturn(boardResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(boardPostToJson)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(boarPost.getTitle()))
                .andExpect(jsonPath("$.data.content").value(boarPost.getContent()))
                .andExpect(jsonPath("$.data.local").value(boarPost.getLocal()))
                .andDo(document("post-board",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시판 본문"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                                        )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("게시판 작성자 닉네임"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시판 본문"),
                                        fieldWithPath("data.local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("data.viewPoint").type(JsonFieldType.NUMBER).description("게시판 조회수"),
                                        fieldWithPath("data.likePoint").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("data.boComments").type(JsonFieldType.NULL).description("게시판 댓글"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시판 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시판 수정 일자")
                                )
                        )
                ));

    }


    @DisplayName("게시판 수정")
    @WithMockUser
    @Test
    void boardPatchTest() throws Exception {
        Long boardId = 1L;
        LocalDateTime createdAt = TIME;
        LocalDateTime modifiedAt = createdAt;

        // given
        BoardDto.Patch boardPatch = BoardDto.Patch.builder()
                .title("수정된 게시판 제목")
                .content("수정된 게시판 본문")
                .local("수정된 게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                .build();

        String boardPatchToJson = gson.toJson(boardPatch);

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .boardId(boardId)
                .nickName("작성자")
                .title("수정된 게시판 제목")
                .content("수정된 게시판 본문")
                .local("수정된 게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                .viewPoint(3L)
                .likePoint(1L)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boardMapper.boardPatchDtoToBoardEntity(Mockito.any(BoardDto.Patch.class))).willReturn(new Board());
        given(boardService.updateBoard(Mockito.any(Long.class), Mockito.any(Board.class),Mockito.anyString())).willReturn(boardResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{board-id}",boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(boardPatchToJson)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(boardPatch.getTitle()))
                .andExpect(jsonPath("content").value(boardPatch.getContent()))
                .andExpect(jsonPath("local").value(boardPatch.getLocal()))
                .andDo(document("patch-board",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 게시판 본문"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("수정할 게시판 지역 카테고리 지역(정해진 지역 외 모두 X)")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 작성자 닉네임"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시판 본문"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("viewPoint").type(JsonFieldType.NUMBER).description("게시판 조회수"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("boComments").type(JsonFieldType.NULL).description("게시판 댓글"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 수정 일자")
                                )
                        )
                ));

    }

    @DisplayName("게시판 삭제")
    @WithMockUser
    @Test
    void boardDeleteTest() throws Exception {
        Long boardId = 1L;

        // given
        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(boardService).removeBoard(Mockito.any(Long.class), Mockito.anyString());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL+"/{board-id}",boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-board",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자")
                        )
                ));
    }

    @DisplayName("게시판 전체 조회")
    @WithMockUser
    @Test
    void getAllBoardListTest() throws Exception {
        // given
        BoardListResponseDto boardListResponseDto1 =
                BoardListResponseDto.builder()
                        .boardId(1L)
                        .nickName("작성자")
                        .title("게시판 1 제목")
                        .local("부산")
                        .viewPoint(8L)
                        .likePoint(2L)
                        .createdAt(TIME)
                        .build();

        BoardListResponseDto boardListResponseDto2 =
                BoardListResponseDto.builder()
                        .boardId(2L)
                        .nickName("서울물")
                        .title("게시판 2 제목")
                        .local("서울")
                        .viewPoint(3L)
                        .likePoint(0)
                        .createdAt(LocalDateTime.now())
                        .build();


        BoardListResponseDto boardListResponseDto3 =
                BoardListResponseDto.builder()
                        .boardId(3L)
                        .nickName("삼다수")
                        .title("게시판 3 제목")
                        .local("제주")
                        .viewPoint(3L)
                        .likePoint(0)
                        .createdAt(LocalDateTime.now())
                        .build();

        List<BoardListResponseDto> boardListResponseDtoList = List.of(boardListResponseDto1, boardListResponseDto2, boardListResponseDto3);

        PageResponseDto<BoardListResponseDto> boardListPageResponseResponseDto = PageResponseDto.of(boardListResponseDtoList, 1L, 3L, 3L, 1L);

        given(boardService.findAllBoards(Mockito.any(Integer.class))).willReturn(boardListPageResponseResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "?page="+1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-AllBoard",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시판 목록"),
                                        fieldWithPath("data.[].boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("data.[].nickName").type(JsonFieldType.STRING).description("게시판 작성자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("data.[].local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역"),
                                        fieldWithPath("data.[].viewPoint").type(JsonFieldType.NUMBER).description("게시판 조회수"),
                                        fieldWithPath("data.[].likePoint").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("게시판 생성일자"),
                                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 게시판 개수"),
                                        fieldWithPath("currentElements").type(JsonFieldType.NUMBER).description("현재 페이지 내 게시판 개수"),
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                )
                        )
                ));
        }

    @DisplayName("게시판 단일 조회")
    @WithMockUser
    @Test
    void getOnceBoardTest() throws Exception {
        Long boardId = 1L;

        BoardResponseDto boardResponseDto =
                BoardResponseDto.builder()
                .boardId(boardId)
                .nickName("작성자")
                .title("게시판 제목")
                .content("게시판 본문")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(11L)
                .likePoint(5L)
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();


        BoCommentResponseDto boCommentResponseDto1 =
                BoCommentResponseDto.builder()
                        .boCommentId(1L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(5L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto2 =
                BoCommentResponseDto.builder()
                        .boCommentId(2L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(0)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto3 =
                BoCommentResponseDto.builder()
                        .boCommentId(3L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(1L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto4 =
                BoCommentResponseDto.builder()
                        .boCommentId(4L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(5000L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();

        List<BoCommentResponseDto> boCommentResponseDtoList = List.of(boCommentResponseDto1, boCommentResponseDto2, boCommentResponseDto3, boCommentResponseDto4);

        boardResponseDto.setBoComments(boCommentResponseDtoList);

        given(boardService.getBoard(boardId)).willReturn(boardResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL+"/{board-id}",boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-OnceBoard",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 작성자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시판 본문"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역"),
                                        fieldWithPath("viewPoint").type(JsonFieldType.NUMBER).description("게시판 조회 수"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("boComments").type(JsonFieldType.ARRAY).description("게시판 댓글"),
                                        fieldWithPath("boComments.[].boCommentId").type(JsonFieldType.NUMBER).description("게시판 댓글 식별자"),
                                        fieldWithPath("boComments.[].boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("boComments.[].nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("boComments.[].content").type(JsonFieldType.STRING).description("게시판 댓글 본문"),
                                        fieldWithPath("boComments.[].likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 수"),
                                        fieldWithPath("boComments.[].createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("boComments.[].modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 수정 일자")
                                )
                        )
                ));
    }


    @DisplayName("게시판 좋아요")
    @WithMockUser
    @Test
    void heartBoardTest() throws Exception {
        Long boardId = 1L;

        BoardResponseDto boardResponseDto =
                BoardResponseDto.builder()
                        .boardId(boardId)
                        .nickName("작성자")
                        .title("게시판 제목")
                        .content("게시판 본문")
                        .local("게시판 지역 카테고리 지역")
                        .viewPoint(11L)
                        .likePoint(1L)
                        .createdAt(TIME)
                        .modifiedAt(LocalDateTime.now())
                        .build();


        BoCommentResponseDto boCommentResponseDto1 =
                BoCommentResponseDto.builder()
                        .boCommentId(1L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(5L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto2 =
                BoCommentResponseDto.builder()
                        .boCommentId(2L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(0)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto3 =
                BoCommentResponseDto.builder()
                        .boCommentId(3L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(1L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
        BoCommentResponseDto boCommentResponseDto4 =
                BoCommentResponseDto.builder()
                        .boCommentId(4L)
                        .boardId(boardId)
                        .nickName("닉네임")
                        .content("댓글 내용")
                        .likePoint(5000L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();

        List<BoCommentResponseDto> boCommentResponseDtoList = List.of(boCommentResponseDto1, boCommentResponseDto2, boCommentResponseDto3, boCommentResponseDto4);

        boardResponseDto.setBoComments(boCommentResponseDtoList);

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boardService.likeBoard(Mockito.any(Long.class), Mockito.anyString())).willReturn(boardResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{board-id}/heart", boardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
                actions
                    .andExpect(status().isOk())
                    .andDo(document("heart-board",
                            getRequestPreProcessor(),
                            getResponsePreProcessor(),
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer Token")
                            ),
                            pathParameters(
                                    parameterWithName("board-id").description("게시판 식별자")
                            ),
                            responseFields(
                                    List.of(
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 작성자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시판 본문"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("게시판 지역 카테고리 지역"),
                                        fieldWithPath("viewPoint").type(JsonFieldType.NUMBER).description("게시판 조회 수"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("boComments").type(JsonFieldType.ARRAY).description("게시판 댓글"),
                                        fieldWithPath("boComments.[].boCommentId").type(JsonFieldType.NUMBER).description("게시판 댓글 식별자"),
                                        fieldWithPath("boComments.[].boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("boComments.[].nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("boComments.[].content").type(JsonFieldType.STRING).description("게시판 댓글 본문"),
                                        fieldWithPath("boComments.[].likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 수"),
                                        fieldWithPath("boComments.[].createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("boComments.[].modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 수정 일자")
                                    )
                            )
                    ));
        }

}
