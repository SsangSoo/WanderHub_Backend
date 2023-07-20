package wanderhub.server.domain.member;

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
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.member.controller.MemberController;
import wanderhub.server.domain.member.dto.MemberDto;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.entity.MemberStatus;
import wanderhub.server.domain.member.mapper.MemberMapper;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanderhub.server.global.utils.ApiDocumentUtils.getRequestPreProcessor;
import static wanderhub.server.global.utils.ApiDocumentUtils.getResponsePreProcessor;

@WebMvcTest(MemberController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberMapper memberMapper;
    @MockBean
    private TokenService tokenService;
    private static final LocalDateTime TIME = LocalDateTime.now();
    private static final String BASE_URL = "/v1/members";

    @DisplayName("회원 정보 수정")
    @WithMockUser
    @Test
    void updateMemberTest() throws Exception {

        // given
        MemberDto.Patch patchMember = MemberDto.Patch.builder()
                .name("이름")
                .nickName("닉네임(이후 변경 X)")
                .imgUrl("프로필 이미지")
                .local("지역(지정된 지역 외 X)")
                .build();

        String patchMemberToJson = gson.toJson(patchMember);

        MemberDto.Response updatedMember = MemberDto.Response.builder()
                .name("이름")
                .email("이메일")
                .nickName("닉네임(이후 변경 X)")
                .imgUrl("프로필 이미지")
                .local("지역(지정된 지역 외 X)")
                .memberStatus(MemberStatus.ACTIVE)
                .newbie(false)
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.findMember(Mockito.anyString())).willReturn(new Member());
        given(memberMapper.memberPatchDtoToMemberEntity(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class), Mockito.any(Member.class))).willReturn(new Member());
        given(memberMapper.memberEntityToMemberResponseDto(Mockito.any(Member.class))).willReturn(updatedMember);


        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                                .content(patchMemberToJson)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(patchMember.getName()))
                .andExpect(jsonPath("$.data.nickName").value(patchMember.getNickName()))
                .andExpect(jsonPath("$.data.imgUrl").value(patchMember.getImgUrl()))
                .andExpect(jsonPath("$.data.local").value(patchMember.getLocal()))
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임(이후 변경 X)"),
                                        fieldWithPath("imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("회원 지역")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("회원 정보 수정 결과 데이터"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("회원 닉네임(이후 변경 불가)"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.local").type(JsonFieldType.STRING).description("회원 거주 지역(선택)"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("현재 회원 활동상태"),
                                        fieldWithPath("data.newbie").type(JsonFieldType.BOOLEAN).description("신규가입자 여부"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 정보 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 일자")
                                )
                        )
                ));
    }


    @DisplayName("회원 정보 조회")
    @WithMockUser
    @Test
    void getMemberTest() throws Exception {
        // given
        MemberDto.GetResponse getMemberResponse = MemberDto.GetResponse.builder()
                .name("회원 이름")
                .email("회원 이메일")
                .nickName("회원 닉네임")
                .imgUrl("회원 프로필 이미지 URL")
                .local("회원 지역")
                .memberStatus(MemberStatus.ACTIVE)
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getMember(Mockito.anyString())).willReturn(new Member());
        given(memberMapper.getMemberEntityToMemberResponseDto(Mockito.any(Member.class))).willReturn(getMemberResponse);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getMember",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("회원 정보 수정 결과 데이터"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("회원 닉네임(이후 변경 불가)"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.local").type(JsonFieldType.STRING).description("회원 거주 지역(선택)"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("현재 회원 활동상태"),
                                        fieldWithPath("data.newbie").type(JsonFieldType.BOOLEAN).description("신규가입자 여부"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 정보 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 일자")
                                )
                        )
                ));
    }


    @DisplayName("회원 탈퇴(휴면 상태)")
    @WithMockUser
    @Test
    void quitMemberTest() throws Exception {
        // given
        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(memberService).quitMember(Mockito.anyString());

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/quit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("member-quit",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("휴면계정 전환 메세지(회원이 휴면계정으로 변경되었습니다.)")
                        )
                ));

    }


    @DisplayName("내가 만든 게시판")
    @WithMockUser
    @Test
    void getWriteBoardListTest() throws Exception {
        // given
        BoardListResponseDto boardListResponseDto1 = BoardListResponseDto.builder()
                .boardId(1L)
                .nickName("회원 닉네임")
                .title("게시판 제목1")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(0)
                .likePoint(0)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto2 = BoardListResponseDto.builder()
                .boardId(2L)
                .nickName("회원 닉네임")
                .title("게시판 제목2")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(2L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto3 = BoardListResponseDto.builder()
                .boardId(3L)
                .nickName("회원 닉네임")
                .title("게시판 제목3")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(12L)
                .likePoint(2L)
                .createdAt(TIME)
                .build();

        List<BoardListResponseDto> boardListResponseDtoList = List.of(
                boardListResponseDto1,
                boardListResponseDto2,
                boardListResponseDto3
        );

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getWriteBoardList(Mockito.anyString())).willReturn(boardListResponseDtoList);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/board/myWriteBoard")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getWriteBoardList",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 식별자"),
                                        fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("회원이 작성한 게시판 제목"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("회원이 작성한 게시판 설정 지역"),
                                        fieldWithPath("[].viewPoint").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 조회수"),
                                        fieldWithPath("[].likePoint").type(JsonFieldType.NUMBER).description("회원이 작성한 좋아요 수"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("회원이 작성한 게시판 생성일자")
                                )
                        )
                ));
    }

    @DisplayName("내가 좋아요 한 게시판")
    @WithMockUser
    @Test
    void getWriteBoardListWithHeartTest() throws Exception {

        // given
        BoardListResponseDto boardListResponseDto1 = BoardListResponseDto.builder()
                .boardId(1L)
                .nickName("작성자 A 닉네임")
                .title("게시판 제목1")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(4L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto2 = BoardListResponseDto.builder()
                .boardId(2L)
                .nickName("작성자 B 닉네임")
                .title("게시판 제목2")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(2L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto3 = BoardListResponseDto.builder()
                .boardId(3L)
                .nickName("작성자 C 닉네임")
                .title("게시판 제목3")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(12L)
                .likePoint(2L)
                .createdAt(TIME)
                .build();

        List<BoardListResponseDto> boardListResponseDtoList = List.of(
                boardListResponseDto1,
                boardListResponseDto2,
                boardListResponseDto3
        );


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getWriteBoardListWithHeart(Mockito.anyString())).willReturn(boardListResponseDtoList);


        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/board/myHeart")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getWriteBoardListWithHeart",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 식별자"),
                                        fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("회원이 작성한 게시판 제목"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("회원이 작성한 게시판 설정 지역"),
                                        fieldWithPath("[].viewPoint").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 조회수"),
                                        fieldWithPath("[].likePoint").type(JsonFieldType.NUMBER).description("회원이 작성한 좋아요 수"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("회원이 작성한 게시판 생성일자")
                                )
                        )
                ));
    }

    @DisplayName("내가 댓글 달은 게시판")
    @WithMockUser
    @Test
    void getBoardWithWriteMyBoardCommentTest() throws Exception {
        // given
        // given
        BoardListResponseDto boardListResponseDto1 = BoardListResponseDto.builder()
                .boardId(1L)
                .nickName("작성자 A 닉네임")
                .title("게시판 제목1")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(4L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto2 = BoardListResponseDto.builder()
                .boardId(2L)
                .nickName("작성자 B 닉네임")
                .title("게시판 제목2")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(2L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto3 = BoardListResponseDto.builder()
                .boardId(3L)
                .nickName("작성자 C 닉네임")
                .title("게시판 제목3")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(12L)
                .likePoint(2L)
                .createdAt(TIME)
                .build();

        List<BoardListResponseDto> boardListResponseDtoList = List.of(
                boardListResponseDto1,
                boardListResponseDto2,
                boardListResponseDto3
        );

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getBoardWithWriteMyBoardComment(Mockito.anyString())).willReturn(boardListResponseDtoList);


        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/board/with-myComment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getBoardWithWriteMyBoardComment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 식별자"),
                                        fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("회원이 작성한 게시판 제목"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("회원이 작성한 게시판 설정 지역"),
                                        fieldWithPath("[].viewPoint").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 조회수"),
                                        fieldWithPath("[].likePoint").type(JsonFieldType.NUMBER).description("회원이 작성한 좋아요 수"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("회원이 작성한 게시판 생성일자")
                                )
                        )
                ));
    }


    @DisplayName("내가 좋아요 한 댓글이 있는 게시판")
    @WithMockUser
    @Test
    void getBoardWithWriteHeartBoardCommentTest() throws Exception {
        // given
        // given
        BoardListResponseDto boardListResponseDto1 = BoardListResponseDto.builder()
                .boardId(1L)
                .nickName("작성자 A 닉네임")
                .title("게시판 제목1")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(4L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto2 = BoardListResponseDto.builder()
                .boardId(2L)
                .nickName("작성자 B 닉네임")
                .title("게시판 제목2")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(2L)
                .likePoint(1L)
                .createdAt(TIME)
                .build();
        BoardListResponseDto boardListResponseDto3 = BoardListResponseDto.builder()
                .boardId(3L)
                .nickName("작성자 C 닉네임")
                .title("게시판 제목3")
                .local("게시판 지역 카테고리 지역")
                .viewPoint(12L)
                .likePoint(2L)
                .createdAt(TIME)
                .build();

        List<BoardListResponseDto> boardListResponseDtoList = List.of(
                boardListResponseDto1,
                boardListResponseDto2,
                boardListResponseDto3
        );

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getBoardWithWriteHeartBoardComment(Mockito.anyString())).willReturn(boardListResponseDtoList);


        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/board/with-myCommentAndHeart")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getBoardWithBoardCommentWithMyHeart",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].boardId").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 식별자"),
                                        fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("회원이 작성한 게시판 제목"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("회원이 작성한 게시판 설정 지역"),
                                        fieldWithPath("[].viewPoint").type(JsonFieldType.NUMBER).description("회원이 작성한 게시판 조회수"),
                                        fieldWithPath("[].likePoint").type(JsonFieldType.NUMBER).description("회원이 작성한 좋아요 수"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("회원이 작성한 게시판 생성일자")
                                )
                        )
                ));
    }
    @DisplayName("내가 만등 동행")
    @WithMockUser
    @Test
    void getWriteAccompanyListTest() throws Exception {
        // given
        String local = "부산";
        LocalDate startDate = LocalDate.of(2023, 8, 2);

        AccompanyResponseListDto accompanyResponseListDto1 =
                AccompanyResponseListDto.builder()
                        .accompanyId(1L)
                        .nickname("작성자 닉네임")
                        .local(local)
                        .currentMemberNum(4L)
                        .maxMemberNum(5L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 23))
                        .title("동행 작성 제목1")
                        .recruitComplete(false)
                        .createdAt(TIME)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto2 =
                AccompanyResponseListDto.builder()
                        .accompanyId(2L)
                        .nickname("다른 닉")
                        .local(local)
                        .currentMemberNum(3L)
                        .maxMemberNum(3L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 8))
                        .title("동행 작성 제목2")
                        .recruitComplete(true)
                        .createdAt(TIME)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto3 =
                AccompanyResponseListDto.builder()
                        .accompanyId(3L)
                        .nickname("다른 닉네임")
                        .local(local)
                        .currentMemberNum(5L)
                        .maxMemberNum(8L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 16))
                        .title("동행 작성 제목3")
                        .recruitComplete(false)
                        .createdAt(TIME)
                        .build();


        List<AccompanyResponseListDto> accompanyResponseListDtoList = List.of(
                accompanyResponseListDto1,
                accompanyResponseListDto2,
                accompanyResponseListDto3
        );

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getWriteAccompanList(Mockito.anyString())).willReturn(accompanyResponseListDtoList);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/accompany/myWrite")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("getWriteAccompanyList",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자")
                                )
                        )
                ));
    }

    @DisplayName("내가 참여 중인 동행")
    @WithMockUser
    @Test
    void getWriteAccompanyJoinedTest() throws Exception {
        // given
        String local = "부산";
        LocalDate startDate = LocalDate.of(2023, 8, 2);

        AccompanyResponseListDto accompanyResponseListDto1 =
                AccompanyResponseListDto.builder()
                        .accompanyId(1L)
                        .nickname("작성자 닉네임")
                        .local(local)
                        .currentMemberNum(4L)
                        .maxMemberNum(5L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 23))
                        .title("동행 작성 제목1")
                        .recruitComplete(false)
                        .createdAt(TIME)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto2 =
                AccompanyResponseListDto.builder()
                        .accompanyId(2L)
                        .nickname("다른 닉")
                        .local(local)
                        .currentMemberNum(3L)
                        .maxMemberNum(3L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 8))
                        .title("동행 작성 제목2")
                        .recruitComplete(true)
                        .createdAt(TIME)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto3 =
                AccompanyResponseListDto.builder()
                        .accompanyId(3L)
                        .nickname("다른 닉네임")
                        .local(local)
                        .currentMemberNum(5L)
                        .maxMemberNum(8L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 16))
                        .title("동행 작성 제목3")
                        .recruitComplete(false)
                        .createdAt(TIME)
                        .build();


        List<AccompanyResponseListDto> accompanyResponseListDtoList = List.of(
                accompanyResponseListDto1,
                accompanyResponseListDto2,
                accompanyResponseListDto3
        );

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(memberService.getWriteAccompanyJoined(Mockito.anyString())).willReturn(accompanyResponseListDtoList);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/accompany/JoinedMe")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getWriteAccompanyJoined",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자")
                                )
                        )
                ));
    }
}
