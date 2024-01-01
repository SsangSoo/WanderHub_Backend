package wanderhub.server.domain.accompany;

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
import wanderhub.server.domain.accompany.controller.AccompanyController;
import wanderhub.server.domain.accompany.dto.AccompanyPostDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.accompany.dto.AccompanySearchCondition;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.mapper.AccompanyMapper;
import wanderhub.server.domain.accompany.service.AccompanyService;
import wanderhub.server.global.response.MessageResponseDto;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanderhub.server.global.utils.ApiDocumentUtils.getRequestPreProcessor;
import static wanderhub.server.global.utils.ApiDocumentUtils.getResponsePreProcessor;

@WebMvcTest(AccompanyController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class AccompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AccompanyMapper accompanyMapper;

    @MockBean
    private AccompanyService accompanyService;

    private static final String BASE_URL = "/v1/accompany";

    private static final LocalDateTime TIME = LocalDateTime.now();

    @Autowired
    private Gson gson;


    @DisplayName("동행 생성 ApiDocs")
    @WithMockUser
    @Test
    void postAccompanyTest() throws Exception {
        // given
        LocalDateTime createAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();

        Map<String, Object> postMap = new HashMap<>();  //
        postMap.put("local", "부산");
        postMap.put("maxMemberNum", 3L);
        postMap.put("accompanyStartDate", "2023-07-23");
        postMap.put("accompanyEndDate", "2023-08-02");
        postMap.put("title", "동행 생성 제목");
        postMap.put("content", "동행 생성 본문");
        postMap.put("coordinateX", 0.15);
        postMap.put("coordinateY", 0.15);
        postMap.put("placeName", "동행 장소");

        String accompanyPostToJson = gson.toJson(postMap);

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(1L)
                .nickname("작성자 닉네임")
                .local("부산")
                .maxMemberNum(3L)
                .accompanyStartDate(LocalDate.of(2023,7,23))
                .accompanyEndDate(LocalDate.of(2023,8,2))
                .title("동행 생성 제목")
                .content("동행 생성 본문")
                .recruitComplete(false)
                .coordinateX(0.15)
                .coordinateY(0.15)
                .placeName("동행 장소")
                .createdAt(createAt)
                .modifiedAt(modifiedAt)
                .build();

        List<String> memberList = List.of("작성자 닉네임");

        accompanyResponseDto.setMemberList(memberList);

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(accompanyMapper.accompanyPostDtoToAccompanyEntity(Mockito.any(AccompanyPostDto.Post.class))).willReturn(new Accompany());
        given(accompanyService.createAccompany(Mockito.any(Accompany.class), Mockito.anyString())).willReturn(accompanyResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(accompanyPostToJson)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.local").value(postMap.get("local")))
                .andExpect(jsonPath("$.data.maxMemberNum").value(postMap.get("maxMemberNum")))
                .andExpect(jsonPath("$.data.accompanyStartDate").value(postMap.get("accompanyStartDate")))
                .andExpect(jsonPath("$.data.accompanyEndDate").value(postMap.get("accompanyEndDate")))
                .andExpect(jsonPath("$.data.title").value(postMap.get("title")))
                .andExpect(jsonPath("$.data.content").value(postMap.get("content")))
                .andExpect(jsonPath("$.data.coordinateX").value(postMap.get("coordinateX")))
                .andExpect(jsonPath("$.data.coordinateY").value(postMap.get("coordinateY")))
                .andExpect(jsonPath("$.data.placeName").value(postMap.get("placeName")))
                .andDo(document("post-accompany",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("data.local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("data.currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("data.maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("data.accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("data.accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("data.recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("data.coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("data.coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("여행 장소"),
                                        fieldWithPath("data.joinMembers").type(JsonFieldType.ARRAY).description("현재 모집된 멤버(생성시 작성자만)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("동행 수정 일자")
                                )
                        )
                ));
    }

    @DisplayName("동행 수정 ApiDocs")
    @WithMockUser
    @Test
    void patchAccompanyTest() throws Exception {
        // given
        Long accompanyId = 1L;
        LocalDateTime createAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();

        Map<String, Object> patchMap = new HashMap<>();  //
        patchMap.put("local", "부산");
        patchMap.put("maxMemberNum", 6L);
        patchMap.put("accompanyStartDate", "2023-07-24");
        patchMap.put("accompanyEndDate", "2023-07-31");
        patchMap.put("title", "수정한 동행 제목");
        patchMap.put("content", "수정한 동행 생성 본문");
        patchMap.put("coordinateX", 13.39);
        patchMap.put("coordinateY", 46.55);
        patchMap.put("placeName", "수정한 동행 장소");

        String accompanyPatchToJson = gson.toJson(patchMap);

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(accompanyId)
                .nickname("작성자 닉네임")
                .local("부산")
                .maxMemberNum(6L)
                .accompanyStartDate(LocalDate.of(2023,7,24))
                .accompanyEndDate(LocalDate.of(2023,7,31))
                .title("수정한 동행 제목")
                .content("수정한 동행 생성 본문")
                .recruitComplete(false)
                .coordinateX(13.39)
                .coordinateY(46.55)
                .placeName("수정한 동행 장소")
                .createdAt(createAt)
                .modifiedAt(modifiedAt)
                .build();

        List<String> memberList = List.of("작성자 닉네임", "작성자 외 1명");

        accompanyResponseDto.setMemberList(memberList);

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(accompanyMapper.accompanyPatchDtoToAccompanyEntity(Mockito.any(AccompanyPostDto.Patch.class))).willReturn(new Accompany());
        given(accompanyService.updateAccompany(Mockito.any(Long.class), Mockito.anyString(), Mockito.any(Accompany.class))).willReturn(accompanyResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{accompany-id}", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(accompanyPatchToJson)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("local").value(patchMap.get("local")))
                .andExpect(jsonPath("maxMemberNum").value(patchMap.get("maxMemberNum")))
                .andExpect(jsonPath("accompanyStartDate").value(patchMap.get("accompanyStartDate")))
                .andExpect(jsonPath("accompanyEndDate").value(patchMap.get("accompanyEndDate")))
                .andExpect(jsonPath("title").value(patchMap.get("title")))
                .andExpect(jsonPath("content").value(patchMap.get("content")))
                .andExpect(jsonPath("coordinateX").value(patchMap.get("coordinateX")))
                .andExpect(jsonPath("coordinateY").value(patchMap.get("coordinateY")))
                .andExpect(jsonPath("placeName").value(patchMap.get("placeName")))
                .andDo(document("patch-accompany",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("accompany-id").description("동행 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소"),
                                        fieldWithPath("joinMembers").type(JsonFieldType.ARRAY).description("현재 모집된 멤버(생성시 작성자만)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("동행 수정 일자")
                                )
                        )
                ));
    }

    @DisplayName("동행 삭제 ApiDocs")
    @WithMockUser
    @Test
    void removeAccompanyTest() throws Exception {
        // given
        Long accompanyId = 1L;

        doNothing().when(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        doNothing().when(accompanyService).removeAccompany(Mockito.anyLong(), Mockito.anyString());

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL+"/{accompany-id}", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-accompany",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("accompany-id").description("동행 식별자")
                        )
                ));
    }

    @DisplayName("동행 단일 조회 ApiDocs")
    @WithMockUser
    @Test
    void getOnceAccompanyTest() throws Exception {
        // given
        Long accompanyId = 1L;
        LocalDateTime createAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(accompanyId)
                .nickname("작성자 닉네임")
                .local("부산")
                .maxMemberNum(6L)
                .accompanyStartDate(LocalDate.of(2023,7,24))
                .accompanyEndDate(LocalDate.of(2023,7,31))
                .title("수정한 동행 제목")
                .content("수정한 동행 생성 본문")
                .coordinateX(13.39)
                .coordinateY(46.55)
                .placeName("수정한 동행 장소")
                .createdAt(createAt)
                .modifiedAt(modifiedAt)
                .build();
        List<String> memberList = List.of("작성자 닉네임", " 닉네임 작성자 말고 한명 더");
        accompanyResponseDto.setMemberList(memberList);

        given(accompanyService.getAccompany(Mockito.any(Long.class))).willReturn(accompanyResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL+"/{accompany-id}", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("getOnce-accompany",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("accompany-id").description("동행 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소"),
                                        fieldWithPath("joinMembers").type(JsonFieldType.ARRAY).description("현재 모집된 멤버(생성시 작성자만)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("동행 수정 일자")
                                )
                        )
                ));
    }

    @DisplayName("동행 전체 조회 ApiDocs")
    @WithMockUser
    @Test
    void getAllAccompanyTest() throws Exception {
        LocalDateTime createAt = TIME;

        AccompanyResponseListDto accompanyResponseListDto1 =
                AccompanyResponseListDto.builder()
                        .accompanyId(1L)
                        .nickname("작성자 닉네임")
                        .local("부산")
                        .currentMemberNum(1L)
                        .maxMemberNum(5L)
                        .accompanyStartDate(LocalDate.of(2023, 7, 24))
                        .accompanyEndDate(LocalDate.of(2023, 7, 31))
                        .title("동행 작성 제목1")
                        .recruitComplete(false)
                        .createdAt(createAt)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto2 =
                AccompanyResponseListDto.builder()
                        .accompanyId(2L)
                        .nickname("작성자 닉네임")
                        .local("서울")
                        .currentMemberNum(2L)
                        .maxMemberNum(7L)
                        .accompanyStartDate(LocalDate.of(2023, 7, 23))
                        .accompanyEndDate(LocalDate.of(2023, 8, 2))
                        .title("동행 작성 제목22")
                        .recruitComplete(false)
                        .createdAt(createAt)
                        .build();

        List<AccompanyResponseListDto> list = new ArrayList<>();
        list.add(accompanyResponseListDto1);
        list.add(accompanyResponseListDto2);

        PageResponseDto pageResponse = PageResponseDto.of(list, 1L, 2L, 2L, 1L);

        given(accompanyService.findByLocalAndDate(Mockito.any(AccompanySearchCondition.class), Mockito.any(Integer.class))).willReturn(pageResponse);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-AllAccompany",
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("동행을 모아놓은 List 객체"),
                                        fieldWithPath("data.[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("data.[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("data.[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("data.[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("data.[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("data.[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("data.[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("10개 기준 총 Page 수"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 동행 게시글 수"),
                                        fieldWithPath("currentElements").type(JsonFieldType.NUMBER).description("현재 페이지의 동행 게시글 개수"),
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                )
                        )
                ));
    }

    @DisplayName("동행 전체 조회 + 검색(지역) ApiDocs")
    @WithMockUser
    @Test
    void getAllAccompanyWithSearchByLocal() throws Exception {
        LocalDateTime createAt = TIME;


        AccompanyResponseListDto accompanyResponseListDto1 =
                AccompanyResponseListDto.builder()
                        .accompanyId(1L)
                        .nickname("작성자 닉네임")
                        .local("부산")
                        .currentMemberNum(1L)
                        .maxMemberNum(5L)
                        .accompanyStartDate(LocalDate.of(2023, 7, 24))
                        .accompanyEndDate(LocalDate.of(2023, 7, 31))
                        .title("동행 작성 제목1")
                        .recruitComplete(false)
                        .createdAt(createAt)
                        .build();


        List<AccompanyResponseListDto> list = new ArrayList<>();
        list.add(accompanyResponseListDto1);

        PageResponseDto pageResponse = PageResponseDto.of(list, 1L, 1L, 1L, 1L);

        given(accompanyService.findByLocalAndDate(Mockito.any(AccompanySearchCondition.class), Mockito.any(Integer.class))).willReturn(pageResponse);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL+"?local=부산")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        // then 
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-AllAccompanyWithSearchByLocal",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("local").description("검색할 지역(한글)")),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("동행을 모아놓은 List 객체"),
                                        fieldWithPath("data.[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("data.[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("data.[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("data.[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("data.[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("data.[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("data.[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("10개 기준 총 Page 수"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 동행 게시글 수"),
                                        fieldWithPath("currentElements").type(JsonFieldType.NUMBER).description("현재 페이지의 동행 게시글 개수"),
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                )
                        )
                ));
    }


    @DisplayName("동행 전체 조회 + 검색(날짜) ApiDocs")
    @WithMockUser
    @Test
    void getAllAccompanyWithSearchByStartDate() throws Exception {
            LocalDateTime createAt = TIME;

            LocalDate startDate = LocalDate.of(2023, 8, 2);

            AccompanyResponseListDto accompanyResponseListDto1 =
                    AccompanyResponseListDto.builder()
                            .accompanyId(1L)
                            .nickname("작성자 닉네임")
                            .local("부산")
                            .currentMemberNum(1L)
                            .maxMemberNum(5L)
                            .accompanyStartDate(LocalDate.of(2023, 8, 2))
                            .accompanyEndDate(LocalDate.of(2023, 8, 23))
                            .title("동행 작성 제목1")
                            .recruitComplete(false)
                            .createdAt(createAt)
                            .build();

        AccompanyResponseListDto accompanyResponseListDto2 =
                AccompanyResponseListDto.builder()
                        .accompanyId(2L)
                        .nickname("작성자 닉네임")
                        .local("서울")
                        .currentMemberNum(1L)
                        .maxMemberNum(3L)
                        .accompanyStartDate(LocalDate.of(2023, 8, 2))
                        .accompanyEndDate(LocalDate.of(2023, 8, 8))
                        .title("동행 작성 제목1")
                        .recruitComplete(false)
                        .createdAt(createAt)
                        .build();



        List<AccompanyResponseListDto> list = new ArrayList<>();
            list.add(accompanyResponseListDto1);
            list.add(accompanyResponseListDto2);

            PageResponseDto pageResponse = PageResponseDto.of(list, 1L, 1L, 1L, 1L);

            given(accompanyService.findByLocalAndDate(Mockito.any(AccompanySearchCondition.class), Mockito.any(Integer.class))).willReturn(pageResponse);

            // when
            ResultActions actions =
                    mockMvc.perform(
                            get(BASE_URL)
                                    .param("startDate", "2023-08-02")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

            // then
            actions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andDo(document("get-AllAccompanyWithSearchByStartDate",
                            getResponsePreProcessor(),
                            requestParameters(
                                    parameterWithName("startDate").description("검색할 동행 시작 날짜")),
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.ARRAY).description("동행을 모아놓은 List 객체"),
                                            fieldWithPath("data.[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                            fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                            fieldWithPath("data.[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                            fieldWithPath("data.[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                            fieldWithPath("data.[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                            fieldWithPath("data.[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                            fieldWithPath("data.[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                            fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                            fieldWithPath("data.[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                            fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                            fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("10개 기준 총 Page 수"),
                                            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 동행 게시글 수"),
                                            fieldWithPath("currentElements").type(JsonFieldType.NUMBER).description("현재 페이지의 동행 게시글 개수"),
                                            fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                    )
                            )
                    ));

    }

    @DisplayName("동행 전체 조회 + 검색(날짜+지역) ApiDocs")
    @WithMockUser
    @Test
    void getAllAccompanyWithSearchByLocalAndStartDate() throws Exception {
        LocalDateTime createAt = TIME;

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
                        .createdAt(createAt)
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
                        .createdAt(createAt)
                        .build();

        AccompanyResponseListDto accompanyResponseListDto3 =
                AccompanyResponseListDto.builder()
                        .accompanyId(3L)
                        .nickname("다른 닉네임")
                        .local(local)
                        .currentMemberNum(5L)
                        .maxMemberNum(8L)
                        .accompanyStartDate(startDate)
                        .accompanyEndDate(LocalDate.of(2023, 8, 8))
                        .title("동행 작성 제목3")
                        .recruitComplete(false)
                        .createdAt(createAt)
                        .build();



        List<AccompanyResponseListDto> list = new ArrayList<>();
        list.add(accompanyResponseListDto1);
        list.add(accompanyResponseListDto2);
        list.add(accompanyResponseListDto3);

        PageResponseDto pageResponse = PageResponseDto.of(list, 1L, 3L, 3L, 1L);

        given(accompanyService.findByLocalAndDate(Mockito.any(AccompanySearchCondition.class), Mockito.any(Integer.class))).willReturn(pageResponse);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL)
                                .param("local", local)
                                .param("startDate", "2023-08-02")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-AllAccompanyWithSearchByLocalAndStartDate",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("local").description("검색 동행 지역(정해진 지역 외 모두 X)"),
                                parameterWithName("startDate").description("검색할 동행 시작 날짜")),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("동행을 모아놓은 List 객체"),
                                        fieldWithPath("data.[].accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("data.[].local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("data.[].currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("data.[].maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("data.[].accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("data.[].accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("data.[].recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("10개 기준 총 Page 수"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 동행 게시글 수"),
                                        fieldWithPath("currentElements").type(JsonFieldType.NUMBER).description("현재 페이지의 동행 게시글 개수"),
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                )
                        )
                ));
    }

    @DisplayName("동행 참여")
    @WithMockUser
    @Test
    void joinAccompanyTest() throws Exception {
        LocalDateTime createdAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();
        Long accompanyId = 1L;

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(accompanyId)
                .nickname("닉네임")
                .local("부산")
                .maxMemberNum(5L)
                .accompanyStartDate(LocalDate.of(2023, 7, 23))
                .accompanyEndDate(LocalDate.of(2023, 8, 2))
                .title("부산 광안리 수변공원 가실 분!")
                .content("부산 광안리 수변공원에 앉아서 회먹으면서 광안대교랑 바다보면, 기분 짱입니다.")
                .coordinateX(13.49)
                .coordinateY(36.5)
                .recruitComplete(false)
                .placeName("서울역")
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();

        List<String> memberList = List.of("닉네임", "부산분");
        accompanyResponseDto.setMemberList(memberList);


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(accompanyService.joinAccompany(Mockito.any(Long.class), Mockito.anyString())).willReturn(accompanyResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{accompany-id}/join", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-joinAccompany",
                                getRequestPreProcessor(),
                                getResponsePreProcessor(),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("accompany-id").description("동행 식별자")
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                                fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                                fieldWithPath("currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                                fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                                fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                                fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                                fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                                fieldWithPath("recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                                fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                                fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                                fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소"),
                                                fieldWithPath("joinMembers").type(JsonFieldType.ARRAY).description("현재 모집된 멤버(생성시 작성자만)"),
                                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("동행 수정 일자"))
                                )
                ));
    }

    @DisplayName("동행 나가기")
    @WithMockUser
    @Test
    void quitAccompanyTest() throws Exception {
        LocalDateTime createdAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();
        Long accompanyId = 1L;

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(accompanyId)
                .nickname("닉네임")
                .local("부산")
                .maxMemberNum(5L)
                .accompanyStartDate(LocalDate.of(2023, 7, 23))
                .accompanyEndDate(LocalDate.of(2023, 8, 2))
                .title("부산 광안리 수변공원 가실 분!")
                .content("부산 광안리 수변공원에 앉아서 회먹으면서 광안대교랑 바다보면, 기분 짱입니다.")
                .coordinateX(13.49)
                .coordinateY(36.5)
                .recruitComplete(false)
                .placeName("서울역")
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
        accompanyResponseDto.setMemberList(List.of("닉네임"));

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(accompanyService.outAccompany(Mockito.any(Long.class), Mockito.anyString())).willReturn(accompanyResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{accompany-id}/quit", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-quitAccompany",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("accompany-id").description("동행 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("accompanyId").type(JsonFieldType.NUMBER).description("동행 게시글 식별자"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("동행 게시글 작성자 닉네임"),
                                        fieldWithPath("local").type(JsonFieldType.STRING).description("동행 지역(정해진 지역 외 모두 X)"),
                                        fieldWithPath("currentMemberNum").type(JsonFieldType.NUMBER).description("현재 동행 참석인원"),
                                        fieldWithPath("maxMemberNum").type(JsonFieldType.NUMBER).description("동행 모집 인원"),
                                        fieldWithPath("accompanyStartDate").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("accompanyEndDate").type(JsonFieldType.STRING).description("동행 여행 종료일"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행 게시판 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행 게시판 본문"),
                                        fieldWithPath("recruitComplete").type(JsonFieldType.BOOLEAN).description("동행 모집 완료 여부"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("여행 장소 X 좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("여행 장소 Y 좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("여행 장소"),
                                        fieldWithPath("joinMembers").type(JsonFieldType.ARRAY).description("현재 모집된 멤버(생성시 작성자만)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("동행 생성 일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("동행 수정 일자"))
                        )
                ));
    }

    @DisplayName("동행 모집 완료")
    @WithMockUser
    @Test
    void recruitCompleteAccompanyTest() throws Exception {
        Long accompanyId = 1L;
        LocalDateTime createdAt = TIME;
        LocalDateTime modifiedAt = LocalDateTime.now();

        AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                .accompanyId(accompanyId)
                .nickname("닉네임")
                .local("부산")
                .maxMemberNum(5L)
                .accompanyStartDate(LocalDate.of(2023, 7, 23))
                .accompanyEndDate(LocalDate.of(2023, 8, 2))
                .title("부산 광안리 수변공원 가실 분!")
                .content("부산 광안리 수변공원에 앉아서 회먹으면서 광안대교랑 바다보면, 기분 짱입니다.")
                .coordinateX(13.49)
                .coordinateY(36.5)
                .recruitComplete(false)
                .placeName("서울역")
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
        accompanyResponseDto.setMemberList(List.of("닉네임"));

        MessageResponseDto messageResponseDto = new MessageResponseDto("Accompany recruitComplete Cancel!");

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(accompanyService.recruitComplete(Mockito.any(Long.class), Mockito.anyString())).willReturn(true);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{accompany-id}/recruitComplete", accompanyId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-recruitCompleteAccompany",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("accompany-id").description("동행 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("동행 모집 완료 메세지"))

                        )
                ));
    }

        @DisplayName("동행 모집 완료 취소")
        @WithMockUser
        @Test
        void cancelRecruitCompleteAccompanyTest() throws Exception {
            Long accompanyId = 1L;
            LocalDateTime createdAt = TIME;
            LocalDateTime modifiedAt = LocalDateTime.now();

            AccompanyResponseDto accompanyResponseDto = AccompanyResponseDto.builder()
                    .accompanyId(accompanyId)
                    .nickname("닉네임")
                    .local("부산")
                    .maxMemberNum(5L)
                    .accompanyStartDate(LocalDate.of(2023, 7, 23))
                    .accompanyEndDate(LocalDate.of(2023, 8, 2))
                    .title("부산 광안리 수변공원 가실 분!")
                    .content("부산 광안리 수변공원에 앉아서 회먹으면서 광안대교랑 바다보면, 기분 짱입니다.")
                    .coordinateX(13.49)
                    .coordinateY(36.5)
                    .recruitComplete(true)
                    .placeName("서울역")
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .build();
            accompanyResponseDto.setMemberList(List.of("닉네임"));

            MessageResponseDto messageResponseDto = new MessageResponseDto("Accompany recruitComplete Cancel!");

            willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
            given(accompanyService.recruitComplete(Mockito.any(Long.class), Mockito.anyString())).willReturn(false);

            // when
            ResultActions actions =
                    mockMvc.perform(
                            patch(BASE_URL + "/{accompany-id}/recruitComplete", accompanyId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .with(csrf())
                                    .headers(GenerateMockToken.getMockHeaderToken())
                    );
            // then
            actions
                    .andExpect(status().isOk())
                    .andDo(document("patch-cancleRecruitCompleteAccompany",
                            getRequestPreProcessor(),
                            getResponsePreProcessor(),
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer Token")
                            ),
                            pathParameters(
                                    parameterWithName("accompany-id").description("동행 식별자")
                            ),
                            responseFields(
                                    List.of(
                                            fieldWithPath("message").type(JsonFieldType.STRING).description("동행 모집 완료 취소 메세지"))

                            )
                    ));

    }

}
