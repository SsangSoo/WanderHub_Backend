package wanderhub.server.domain.mytrip_plan;

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
import wanderhub.server.domain.mytrip_plan.controller.MyTripPlanController;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanListResponseDto;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanResponseDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.domain.mytrip_plan.mapper.MyTripMapper;
import wanderhub.server.domain.mytrip_plan.service.MyTripPlanService;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailListResponseDto;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

@WebMvcTest(MyTripPlanController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class MyTripPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MyTripPlanService myTripPlanService;

    @MockBean
    private MyTripMapper myTripMapper;

    private final static String BASE_URL = "/v1/mytrip";

    private final static LocalDateTime TIME = LocalDateTime.now();



    @DisplayName("개인 여행 일정 생성")
    @WithMockUser
    @Test
    void myTripPlanCreateTest() throws Exception {
        // given
        Map<String, String> myTripPlanPost = new HashMap<>();
        myTripPlanPost.put("title", "여행 계획 제목");
        myTripPlanPost.put("tripStartDate", "2023-10-02");
        myTripPlanPost.put("tripEndDate", "2023-10-02");

        String myTripPlanPostToJson = gson.toJson(myTripPlanPost);

        MyTripPlanResponseDto myTripPlanResponseDto = MyTripPlanResponseDto.builder()
                .myTripPlanId(1L)
                .title("여행 계획 제목")
                .tripStartDate(LocalDate.of(2023, 10, 2))
                .tripEndDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripMapper.myTripPlanPostDtoToMyTripPlanEntity(Mockito.any(MyTripPlanDto.Post.class))).willReturn(new MyTripPlan());
        given(myTripPlanService.createTripPlan(Mockito.anyString(), Mockito.any(MyTripPlan.class))).willReturn(myTripPlanResponseDto);


        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                                .content(myTripPlanPostToJson)
                );

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(myTripPlanPost.get("title")))
                .andExpect(jsonPath("$.data.tripStartDate").value(myTripPlanPost.get("tripStartDate")))
                .andExpect(jsonPath("$.data.tripEndDate").value(myTripPlanPost.get("tripEndDate")))
                .andDo(document("post-myTrip",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("개인 여행 제목"),
                                        fieldWithPath("tripStartDate").type(JsonFieldType.STRING).description("개인 여행 시작 날짜"),
                                        fieldWithPath("tripEndDate").type(JsonFieldType.STRING).description("개인 여행 종료 날짜")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.myTripPlanId").type(JsonFieldType.NUMBER).description("개인 여행 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("개인 여행 제목"),
                                        fieldWithPath("data.tripStartDate").type(JsonFieldType.STRING).description("개인 여행 시작 날짜"),
                                        fieldWithPath("data.tripEndDate").type(JsonFieldType.STRING).description("개인 여행 종료 날짜"),
                                        fieldWithPath("data.myTripPlanDetailResponseDtoList").type(JsonFieldType.ARRAY).description("여행 정보 디테일(새로 만들어서 아무것도 없음)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("동행 여행 종료일")
                                )
                        )
                ));

    }

    @DisplayName("개인 여행 일정 수정")
    @WithMockUser
    @Test
    void updateMyTripPlanTest() throws Exception {
        // given
        Long myTripPlanId = 1L;
        
        Map<String, String> myTripPlanPatch = new HashMap<>();
        myTripPlanPatch.put("title", "수정된 여행 계획 제목");
        myTripPlanPatch.put("tripStartDate", "2023-10-02");
        myTripPlanPatch.put("tripEndDate", "2023-10-02");

        String myTripPlanPatchToJson = gson.toJson(myTripPlanPatch);

        MyTripPlanResponseDto myTripPlanResponseDto = MyTripPlanResponseDto.builder()
                .myTripPlanId(1L)
                .title("수정된 여행 계획 제목")
                .tripStartDate(LocalDate.of(2023, 10, 2))
                .tripEndDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto1 = MyTripPlanDetailListResponseDto.builder()
                .myTripPlanDetailId(1L)
                .subTitle("개인 일정 세부 제목")
                .placeName("개인 일정 세부 장소")
                .whenDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .build();
        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto2 = MyTripPlanDetailListResponseDto.builder()
                .myTripPlanDetailId(2L)
                .subTitle("개인 일정 세부 제목2")
                .placeName("개인 일정 세부 장소2")
                .whenDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .build();
        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto3 = MyTripPlanDetailListResponseDto.builder()
                .myTripPlanDetailId(3L)
                .subTitle("개인 일정 세부 제목3")
                .placeName("개인 일정 세부 장소3")
                .whenDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .build();

        List<MyTripPlanDetailListResponseDto> myTripPlanDetailListResponseDtoList = List.of(
                myTripPlanDetailListResponseDto1,
                myTripPlanDetailListResponseDto2,
                myTripPlanDetailListResponseDto3
        );

        myTripPlanResponseDto.setMyTripPlanDetailResponseDtoList(myTripPlanDetailListResponseDtoList);


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripMapper.myTripPlanPatchDtoToMyTripPlanEntity(Mockito.any(MyTripPlanDto.Patch.class))).willReturn(new MyTripPlan());
        given(myTripPlanService.updateMyTripPlan(Mockito.anyString(), Mockito.any(Long.class), Mockito.any(MyTripPlan.class))).willReturn(myTripPlanResponseDto);


        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{myTripPlan-id}",myTripPlanId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                                .content(myTripPlanPatchToJson)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(myTripPlanPatch.get("title")))
                .andExpect(jsonPath("tripStartDate").value(myTripPlanPatch.get("tripStartDate")))
                .andExpect(jsonPath("tripEndDate").value(myTripPlanPatch.get("tripEndDate")))
                .andDo(document("patch-myTrip",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("여행 계획 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("수정한 개인 여행 제목"),
                                        fieldWithPath("tripStartDate").type(JsonFieldType.STRING).description("수정한 개인 여행 시작 날짜"),
                                        fieldWithPath("tripEndDate").type(JsonFieldType.STRING).description("수정한 개인 여행 종료 날짜")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("myTripPlanId").type(JsonFieldType.NUMBER).description("수정한 개인 여행 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("수정한 개인 여행 제목"),
                                        fieldWithPath("tripStartDate").type(JsonFieldType.STRING).description("수정한 개인 여행 시작 날짜"),
                                        fieldWithPath("tripEndDate").type(JsonFieldType.STRING).description("수정한 개인 여행 종료 날짜"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList").type(JsonFieldType.ARRAY).description("여행 정보 디테일 목록"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].myTripPlanDetailId").type(JsonFieldType.NUMBER).description("여행 정보 디테일 목록 식별자"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].subTitle").type(JsonFieldType.STRING).description("여행 정보 디테일 제목(부제목)"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].placeName").type(JsonFieldType.STRING).description("여행 정보 디테일 장소"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].whenDate").type(JsonFieldType.STRING).description("여행 정보 디테일 계획 일"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].createdAt").type(JsonFieldType.STRING).description("여행 정보 디테일 생성일자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("동행 여행 시작일"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("동행 여행 종료일")
                                )
                        )
                ));
    }

    @DisplayName("개인 여행 일정 삭제")
    @WithMockUser
    @Test
    void deleteMyTripPlanTest() throws Exception {
        // given
        Long myTripPlanId = 1L;

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(myTripPlanService).removeMyTripPlan(Mockito.anyString(), Mockito.any(Long.class));


        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL + "/{myTripPlan-id}", myTripPlanId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document("delete-myTrip",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 일정 식별자")
                        )
                ));
    }


    @DisplayName("개인 여행 일정 전체 조회 (자기 것만)")
    @WithMockUser
    @Test
    void getAllMyPlanTripTest() throws Exception {
        MyTripPlanListResponseDto myTripPlanListResponseDto1 = MyTripPlanListResponseDto.builder()
                .myTripPlanId(1L)
                .title("개인 일정 조회")
                .tripStartDate(LocalDate.of(2023, 7, 23))
                .tripEndDate(LocalDate.of(2023, 8, 2))
                .createdAt(TIME)
                .build();

        MyTripPlanListResponseDto myTripPlanListResponseDto2 = MyTripPlanListResponseDto.builder()
                .myTripPlanId(2L)
                .title("개인 일정 조회2")
                .tripStartDate(LocalDate.of(2023, 8, 23))
                .tripEndDate(LocalDate.of(2023, 9, 2))
                .createdAt(TIME)
                .build();


        MyTripPlanListResponseDto myTripPlanListResponseDto3 = MyTripPlanListResponseDto.builder()
                .myTripPlanId(3L)
                .title("개인 일정 조회3")
                .tripStartDate(LocalDate.of(2023, 9, 23))
                .tripEndDate(LocalDate.of(2023, 10, 2))
                .createdAt(TIME)
                .build();


        List<MyTripPlanListResponseDto> myTripPlanListResponseDtoList = List.of(
                myTripPlanListResponseDto1,
                myTripPlanListResponseDto2,
                myTripPlanListResponseDto3
        );


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripPlanService.getAllMyTripPlan(Mockito.anyString())).willReturn(myTripPlanListResponseDtoList);

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
        actions.andExpect(status().isOk())
                .andDo(document("get-allMyTrip",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].myTripPlanId").type(JsonFieldType.NUMBER).description("개인 여행 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("개인 여행 제목"),
                                        fieldWithPath("[].tripStartDate").type(JsonFieldType.STRING).description("개인 여행 시작 날짜"),
                                        fieldWithPath("[].tripEndDate").type(JsonFieldType.STRING).description("개인 여행 종료 날짜"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("개인 여행 생성일자")
                                )
                        )
                ));
    }

    @DisplayName("개인 여행 일정 일정 단일 조회 (자기 것만 조회가능)")
    @WithMockUser
    @Test
    void getOnceMyTripTest() throws Exception {
        // given
        Long myTripPlanId = 1L;

        MyTripPlanResponseDto myTripPlanResponseDto =
                MyTripPlanResponseDto.builder()
                        .myTripPlanId(myTripPlanId)
                        .title("개인 여행 일정 제목")
                        .tripStartDate(LocalDate.of(2023, 7, 24))
                        .tripEndDate(LocalDate.of(2023, 8, 2))
                        .createdAt(TIME)
                        .modifiedAt(LocalDateTime.now())
                        .build();

        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto1 =
                MyTripPlanDetailListResponseDto.builder()
                        .myTripPlanDetailId(1L)
                        .subTitle("개인 여행 일정 세부 제목1")
                        .placeName("개인 여행 일정 세부 여행 장소1")
                        .whenDate(LocalDate.of(2023, 9, 23))
                        .createdAt(LocalDateTime.now())
                        .build();
        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto2 =
                MyTripPlanDetailListResponseDto.builder()
                        .myTripPlanDetailId(2L)
                        .subTitle("개인 여행 일정 세부 제목2")
                        .placeName("개인 여행 일정 세부 여행 장소2")
                        .whenDate(LocalDate.of(2023, 9, 23))
                        .createdAt(LocalDateTime.now())
                        .build();
        MyTripPlanDetailListResponseDto myTripPlanDetailListResponseDto3 =
                MyTripPlanDetailListResponseDto.builder()
                        .myTripPlanDetailId(3L)
                        .subTitle("개인 여행 일정 세부 제목3")
                        .placeName("개인 여행 일정 세부 여행 장소3")
                        .whenDate(LocalDate.of(2023, 9, 23))
                        .createdAt(LocalDateTime.now())
                        .build();

        List<MyTripPlanDetailListResponseDto> myTripPlanDetailListResponseDtoList =
                List.of(
                        myTripPlanDetailListResponseDto1,
                        myTripPlanDetailListResponseDto2,
                        myTripPlanDetailListResponseDto3
                );
        myTripPlanResponseDto.setMyTripPlanDetailResponseDtoList(myTripPlanDetailListResponseDtoList);

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripPlanService.getMyTripPlan(Mockito.anyString(), Mockito.any(Long.class))).willReturn(myTripPlanResponseDto);


        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/{myTripPlan-id}", myTripPlanId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // than
        actions.andExpect(status().isOk())
                .andDo(document("get-allMyTrip",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 여행 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("myTripPlanId").type(JsonFieldType.NUMBER).description("개인 여행 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("개인 여행 제목"),
                                        fieldWithPath("tripStartDate").type(JsonFieldType.STRING).description("개인 여행 시작 날짜"),
                                        fieldWithPath("tripEndDate").type(JsonFieldType.STRING).description("개인 여행 종료 날짜"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList").type(JsonFieldType.ARRAY).description("개인 여행 세부 목록"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].myTripPlanDetailId").type(JsonFieldType.NUMBER).description("개인 여행 세부 식별자"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].subTitle").type(JsonFieldType.STRING).description("개인 여행 세부 제목"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].placeName").type(JsonFieldType.STRING).description("개인 여행 세부 여행 장소"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].whenDate").type(JsonFieldType.STRING).description("개인 여행 세부 날짜"),
                                        fieldWithPath("myTripPlanDetailResponseDtoList.[].createdAt").type(JsonFieldType.STRING).description("개인 여행 세부 생성일자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("개인 여행 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("개인 여행 수정일자")
                                )
                        )
                ));
    }

}
