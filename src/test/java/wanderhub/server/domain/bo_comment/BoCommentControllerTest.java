package wanderhub.server.domain.bo_comment;

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
import wanderhub.server.domain.bo_comment.controller.BoCommentController;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.bo_comment.mapper.BoardCommentMapper;
import wanderhub.server.domain.bo_comment.service.BoCommentService;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanderhub.server.global.utils.ApiDocumentUtils.getRequestPreProcessor;
import static wanderhub.server.global.utils.ApiDocumentUtils.getResponsePreProcessor;

@WebMvcTest(BoCommentController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class BoCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardCommentMapper boardCommentMapper;

    @MockBean
    private BoCommentService boCommentService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    Gson gson;


    private final static String BASE_URL = "/v1/board/{board-id}/comment";

    private final static LocalDateTime TIME = LocalDateTime.now();

    @DisplayName("게시판 댓글 생성")
    @WithMockUser
    @Test
    void boCommentCreateTest() throws Exception {

        // given
        Long boardId = 1L;

        BoCommentDto.PostAndPatch boCommentPost =
                BoCommentDto.PostAndPatch.builder()
                        .content("게시판 댓글 본문")
                        .build();

        String boCommentPostToJson = gson.toJson(boCommentPost);

        BoCommentResponseDto createdBoComment = BoCommentResponseDto.builder()
                .boCommentId(1L)
                .boardId(1L)
                .nickName("게시판 댓글 작성자")
                .content("게시판 댓글 본문")
                .likePoint(0L)
                .createdAt(TIME)
                .modifiedAt(TIME)
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boardCommentMapper.boCommentPostAndPatchDtoToBoCommentEntity(Mockito.any(BoCommentDto.PostAndPatch.class))).willReturn(new BoComment());
        given(boCommentService.createComment(Mockito.any(Long.class), Mockito.any(BoComment.class), Mockito.anyString())).willReturn(createdBoComment);


        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL,boardId)

                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                                .content(boCommentPostToJson)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value(boCommentPost.getContent()))
                .andDo(document("post-board-comment",
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
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 본문")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.boCommentId").type(JsonFieldType.NUMBER).description("게시판 댓글 식별자"),
                                        fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시판 댓글 본문"),
                                        fieldWithPath("data.likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 수"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자")
                                )
                        )
                ));
    }

    @DisplayName("게시판 댓글 수정")
    @WithMockUser
    @Test
    void updateBoardCommentTest() throws Exception {
        // given
        Long boardId = 1L;

        Long commentId = 1L;

        BoCommentDto.PostAndPatch boCommentPatch =
                BoCommentDto.PostAndPatch.builder()
                        .content("수정한 게시판 댓글 본문")
                        .build();

        String boCommentPatchToJson = gson.toJson(boCommentPatch);

        BoCommentResponseDto updateBoCommentResponseDto = BoCommentResponseDto.builder()
                .boCommentId(1L)
                .boardId(1L)
                .nickName("게시판 댓글 작성자")
                .content("수정한 게시판 댓글 본문")
                .likePoint(0L)
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boardCommentMapper.boCommentPostAndPatchDtoToBoCommentEntity(Mockito.any(BoCommentDto.PostAndPatch.class))).willReturn(new BoComment());
        given(boCommentService.updateComment(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.any(BoComment.class), Mockito.anyString())).willReturn(updateBoCommentResponseDto);


        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{comment-id}", boardId, commentId)

                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                                .content(boCommentPatchToJson)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value(boCommentPatch.getContent()))
                .andDo(document("patch-board-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자"),
                                parameterWithName("comment-id").description("게시판 댓글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 본문")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boCommentId").type(JsonFieldType.NUMBER).description("수정한 게시판 댓글 식별자"),
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 게시판 댓글 본문"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 수"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자")
                                )
                        )
                ));
    }

    @DisplayName("게시판 댓글 삭제")
    @WithMockUser
    @Test
    void deleteBoardCommentTest() throws Exception {
        // given
        Long boardId = 1L;
        Long commentId = 1L;

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(boCommentService).removeBoComment(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.anyString());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL + "/{comment-id}", boardId, commentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-board-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자"),
                                parameterWithName("comment-id").description("게시판 댓글 식별자")
                        )
                ));
    }

    @DisplayName("게시판 댓글 좋아요")
    @WithMockUser
    @Test
    void heartBoardCommentTest() throws Exception {
        // given
        Long boardId = 1L;

        Long commentId = 1L;

        BoCommentResponseDto likedBoardCommentResponseDto = BoCommentResponseDto.builder()
                .boCommentId(commentId)
                .boardId(1L)
                .nickName("게시판 댓글 작성자")
                .content("게시판 댓글 본문")
                .likePoint(1L)
                .createdAt(TIME)
                .modifiedAt(TIME)
                .build();


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boCommentService.likeBoCommnet(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.anyString())).willReturn(likedBoardCommentResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{comment-id}/heart", boardId, commentId)

                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("heart-board-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자"),
                                parameterWithName("comment-id").description("게시판 댓글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boCommentId").type(JsonFieldType.NUMBER).description("수정한 게시판 댓글 식별자"),
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 게시판 댓글 본문"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 수"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자")
                                )
                        )
                ));

    }


    @DisplayName("게시판 댓글 좋아요 취소")
    @WithMockUser
    @Test
    void cancelHeartBoardCommentTest() throws Exception {
        // given
        Long boardId = 1L;
        Long commentId = 1L;

        BoCommentResponseDto likedBoardCommentResponseDto = BoCommentResponseDto.builder()
                .boCommentId(commentId)
                .boardId(1L)
                .nickName("게시판 댓글 작성자")
                .content("게시판 댓글 본문")
                .likePoint(0)
                .createdAt(TIME)
                .modifiedAt(TIME)
                .build();


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(boCommentService.likeBoCommnet(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.anyString())).willReturn(likedBoardCommentResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{comment-id}/heart",boardId , commentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("heart-cancel-board-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("board-id").description("게시판 식별자"),
                                parameterWithName("comment-id").description("게시판 댓글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boCommentId").type(JsonFieldType.NUMBER).description("수정한 게시판 댓글 식별자"),
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("게시판 댓글 작성자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 게시판 댓글 본문"),
                                        fieldWithPath("likePoint").type(JsonFieldType.NUMBER).description("게시판 댓글 좋아요 취소 후 개수"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시판 댓글 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("게시판 댓글 수정일자")
                                )
                        )
                ));
    }

}
