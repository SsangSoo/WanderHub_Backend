package wanderhub.server.domain.mytrip_plan_detail;

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
import wanderhub.server.domain.mytrip_plan_detail.controller.MyTripPlanDetailController;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailDto;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailResponseDto;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.domain.mytrip_plan_detail.mapper.MyTripPlanDetailMapper;
import wanderhub.server.domain.mytrip_plan_detail.service.MyTripPlanDetailService;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

@WebMvcTest(MyTripPlanDetailController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
public class MyTripPlanDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MyTripPlanDetailService myTripPlanDetailService;

    @MockBean
    private MyTripPlanDetailMapper myTripPlanDetailMapper;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/v1/mytrip/{myTripPlan-id}/details";

    private static final LocalDateTime TIME = LocalDateTime.now();


    @DisplayName("개인 일정 세부 생성")
    @WithMockUser
    @Test
    void postMyTripPlanDetailTest() throws Exception {

        // given
        Long myTripPlanId = 1L;

        Map<String, Object> myTripPlanDetailPost = new HashMap<>();
        myTripPlanDetailPost.put("subTitle", "개인 일정 세부 제목");
        myTripPlanDetailPost.put("content", "개인 일정 세부 본문");
        myTripPlanDetailPost.put("coordinateX", 0.39);
        myTripPlanDetailPost.put("coordinateY", 36.5);
        myTripPlanDetailPost.put("placeName", "쿠우쿠우");
        myTripPlanDetailPost.put("whenDate", "2023-08-02");
        myTripPlanDetailPost.put("timeStart", "13:00");
        myTripPlanDetailPost.put("timeEnd", "15:30");

        String myTripPlanDetailPostToJson = gson.toJson(myTripPlanDetailPost);

        MyTripPlanDetailResponseDto myTripPlanDetailResponseDto = MyTripPlanDetailResponseDto.builder()
                .myTripPlanDetailId(1L)
                .subTitle("개인 일정 세부 제목")
                .content("개인 일정 세부 본문")
                .coordinateX(0.39)
                .coordinateY(36.5)
                .placeName("쿠우쿠우")
                .whenDate(LocalDate.of(2023, 8, 2))
                .timeStart(LocalTime.of(13, 00))
                .timeEnd(LocalTime.of(15, 30))
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripPlanDetailMapper.myTripPlanDetailPostDtoToEntity(Mockito.any(MyTripPlanDetailDto.Post.class))).willReturn(new MyTripPlanDetail());
        given(myTripPlanDetailService.createTripPlanDetail(Mockito.anyString(), Mockito.any(Long.class), Mockito.any(MyTripPlanDetail.class))).willReturn(myTripPlanDetailResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL,myTripPlanId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .content(myTripPlanDetailPostToJson)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subTitle").value(myTripPlanDetailPost.get("subTitle")))
                .andExpect(jsonPath("$.data.content").value(myTripPlanDetailPost.get("content")))
                .andExpect(jsonPath("$.data.coordinateX").value(myTripPlanDetailPost.get("coordinateX")))
                .andExpect(jsonPath("$.data.coordinateY").value(myTripPlanDetailPost.get("coordinateY")))
                .andExpect(jsonPath("$.data.placeName").value(myTripPlanDetailPost.get("placeName")))
                .andExpect(jsonPath("$.data.whenDate").value(myTripPlanDetailPost.get("whenDate")))
                .andExpect(jsonPath("$.data.timeStart").value(myTripPlanDetailPost.get("timeStart")))
                .andExpect(jsonPath("$.data.timeEnd").value(myTripPlanDetailPost.get("timeEnd")))
                .andDo(document("post-myTripPlanDetail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 일정 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("subTitle").type(JsonFieldType.STRING).description("개인 일정 세부 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("개인 일정 세부 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 X좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 Y좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("개인 일정 세부 장소명"),
                                        fieldWithPath("whenDate").type(JsonFieldType.STRING).description("개인 일정 세부 날짜"),
                                        fieldWithPath("timeStart").type(JsonFieldType.STRING).description("개인 일정 세부 계획 시작 시간"),
                                        fieldWithPath("timeEnd").type(JsonFieldType.STRING).description("개인 일정 세부 계획 종료 시간"))
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.myTripPlanDetailId").type(JsonFieldType.NUMBER).description("개인 일정 세부 식별자"),
                                        fieldWithPath("data.subTitle").type(JsonFieldType.STRING).description("개인 일정 세부 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("개인 일정 세부 본문"),
                                        fieldWithPath("data.coordinateX").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 X좌표 "),
                                        fieldWithPath("data.coordinateY").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 Y좌표\""),
                                        fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("개인 일정 세부 장소명"),
                                        fieldWithPath("data.whenDate").type(JsonFieldType.STRING).description("개인 일정 세부 날짜"),
                                        fieldWithPath("data.timeStart").type(JsonFieldType.STRING).description("개인 일정 세부 계획 시작 시간"),
                                        fieldWithPath("data.timeEnd").type(JsonFieldType.STRING).description("개인 일정 세부 계획 종료 시간"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("개인 일정 세부 생성일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("개인 일정 세부 수정일자")
                                )
                        )
                ));
    }


    @DisplayName("개인 일정 세부 수정")
    @WithMockUser
    @Test
    void patchMyTripPlanDetailTest() throws Exception {
        // given
        Long myTripPlanId = 1L;
        Long myTripPlanDetailId = 1L;

        Map<String, Object> myTripPlanDetailPatch = new HashMap<>();
        myTripPlanDetailPatch.put("subTitle", "수정된 개인 일정 세부 제목");
        myTripPlanDetailPatch.put("content", "수정된 개인 일정 세부 본문");
        myTripPlanDetailPatch.put("coordinateX", 0.39);
        myTripPlanDetailPatch.put("coordinateY", 36.5);
        myTripPlanDetailPatch.put("placeName", "수정된 장소");
        myTripPlanDetailPatch.put("whenDate", "2023-08-02");
        myTripPlanDetailPatch.put("timeStart", "13:00");
        myTripPlanDetailPatch.put("timeEnd", "15:30");

        String myTripPlanDetailPatchToJson = gson.toJson(myTripPlanDetailPatch);

        MyTripPlanDetailResponseDto myTripPlanDetailResponseDto = MyTripPlanDetailResponseDto.builder()
                .myTripPlanDetailId(1L)
                .subTitle("수정된 개인 일정 세부 제목")
                .content("수정된 개인 일정 세부 본문")
                .coordinateX(0.39)
                .coordinateY(36.5)
                .placeName("수정된 장소")
                .whenDate(LocalDate.of(2023, 8, 2))
                .timeStart(LocalTime.of(13, 00))
                .timeEnd(LocalTime.of(15, 30))
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripPlanDetailMapper.myTripPlanDetailPatchDtoToEntity(Mockito.any(MyTripPlanDetailDto.Patch.class))).willReturn(new MyTripPlanDetail());
        given(myTripPlanDetailService.updateTripPlanDetail(Mockito.anyString(), Mockito.any(Long.class), Mockito.any(Long.class), Mockito.any(MyTripPlanDetail.class))).willReturn(myTripPlanDetailResponseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{myTripPlanDetail-id}",myTripPlanId, myTripPlanDetailId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .content(myTripPlanDetailPatchToJson)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("subTitle").value(myTripPlanDetailPatch.get("subTitle")))
                .andExpect(jsonPath("content").value(myTripPlanDetailPatch.get("content")))
                .andExpect(jsonPath("coordinateX").value(myTripPlanDetailPatch.get("coordinateX")))
                .andExpect(jsonPath("coordinateY").value(myTripPlanDetailPatch.get("coordinateY")))
                .andExpect(jsonPath("placeName").value(myTripPlanDetailPatch.get("placeName")))
                .andExpect(jsonPath("whenDate").value(myTripPlanDetailPatch.get("whenDate")))
                .andExpect(jsonPath("timeStart").value(myTripPlanDetailPatch.get("timeStart")))
                .andExpect(jsonPath("timeEnd").value(myTripPlanDetailPatch.get("timeEnd")))
                .andDo(document("patch-myTripPlanDetail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 일정 식별자"),
                                parameterWithName("myTripPlanDetail-id").description("개인 일정 세부 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("subTitle").type(JsonFieldType.STRING).description("개인 일정 세부 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("개인 일정 세부 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 X좌표"),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("개인 일정 세부 장소 Y좌표"),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("개인 일정 세부 장소명"),
                                        fieldWithPath("whenDate").type(JsonFieldType.STRING).description("개인 일정 세부 날짜"),
                                        fieldWithPath("timeStart").type(JsonFieldType.STRING).description("개인 일정 세부 계획 시작 시간"),
                                        fieldWithPath("timeEnd").type(JsonFieldType.STRING).description("개인 일정 세부 계획 종료 시간"))
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("myTripPlanDetailId").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 식별자"),
                                        fieldWithPath("subTitle").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 장소 X좌표 "),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 장소 Y좌표\""),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 장소명"),
                                        fieldWithPath("whenDate").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 날짜"),
                                        fieldWithPath("timeStart").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 계획 시작 시간"),
                                        fieldWithPath("timeEnd").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 계획 종료 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 수정일자")
                                )
                        )
                ));
    }

    @DisplayName("개인 일정 세부 삭제")
    @WithMockUser
    @Test
    void deleteMyTripPlanDetailTest() throws Exception {
        // given
        Long myTripPlanId = 1L;
        Long myTripPlanDetailId = 1L;


        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(myTripPlanDetailService).removeMyTripPlanDetail(Mockito.anyString(), Mockito.any(Long.class), Mockito.any(Long.class));

        // when
        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL + "/{myTripPlanDetail-id}", myTripPlanId, myTripPlanDetailId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );


        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-myTripPlanDetail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 일정 식별자"),
                                parameterWithName("myTripPlanDetail-id").description("개인 일정 세부 식별자")
                        )
                ));
    }

    @DisplayName("개인 일정 세부 조회")
    @WithMockUser
    @Test
    void getOnceMyTripPlanDetailTest() throws Exception {
        // given
        Long myTripPlanId = 1L;
        Long myTripPlanDetailId = 1L;


        MyTripPlanDetailResponseDto myTripPlanDetailResponseDto = MyTripPlanDetailResponseDto.builder()
                .myTripPlanDetailId(myTripPlanDetailId)
                .subTitle("개인 일정 세부 제목")
                .content("개인 일정 세부 본문")
                .coordinateX(0.35)
                .coordinateY(36.54)
                .placeName("개인 일정 세부 장소명")
                .whenDate(LocalDate.of(2023, 7, 23))
                .timeStart(LocalTime.of(13, 00))
                .timeEnd(LocalTime.of(15, 30))
                .createdAt(TIME)
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        given(myTripPlanDetailService.getOnceMyTripPlanDetail(Mockito.anyString(), Mockito.any(Long.class), Mockito.any(Long.class))).willReturn(myTripPlanDetailResponseDto);


        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/{myTripPlanDetail-id}", myTripPlanId, myTripPlanDetailId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-OnceMyTripPlanDetail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("myTripPlan-id").description("개인 일정 식별자"),
                                parameterWithName("myTripPlanDetail-id").description("개인 일정 세부 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("myTripPlanDetailId").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 식별자"),
                                        fieldWithPath("subTitle").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 본문"),
                                        fieldWithPath("coordinateX").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 장소 X좌표 "),
                                        fieldWithPath("coordinateY").type(JsonFieldType.NUMBER).description("수정된 개인 일정 세부 장소 Y좌표\""),
                                        fieldWithPath("placeName").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 장소명"),
                                        fieldWithPath("whenDate").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 날짜"),
                                        fieldWithPath("timeStart").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 계획 시작 시간"),
                                        fieldWithPath("timeEnd").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 계획 종료 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 생성일자"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정된 개인 일정 세부 수정일자")
                                )
                        )
                ));
    }
}
