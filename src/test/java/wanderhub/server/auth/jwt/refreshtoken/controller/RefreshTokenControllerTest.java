package wanderhub.server.auth.jwt.refreshtoken.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.global.utils.GenerateMockToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanderhub.server.global.utils.ApiDocumentUtils.getRequestPreProcessor;
import static wanderhub.server.global.utils.ApiDocumentUtils.getResponsePreProcessor;

@WebMvcTest(RefreshTokenController.class) // 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해 주는 설정
@AutoConfigureRestDocs
class RefreshTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @Autowired
    private Gson gson;

    private final static String BASE_URL = "/v1/auth";

    private final static LocalDateTime TIME = LocalDateTime.now();

    @DisplayName("액새스 토큰 재발급")
    @WithMockUser
    @Test
    void getAccessTokenTest() throws Exception {
        // given
        String accessToken = "Bearer accessToekn1234567890accessToekn1234567890accessToekn1234567890accessToekn123456789";

        given(request.getHeader("email")).willReturn("user@Email.com");
        given(request.getHeader("Authorization")).willReturn(GenerateMockToken.createMockToken());
        given(tokenService.reissueAccessToken(Mockito.anyString(), Mockito.anyString())).willReturn(accessToken);
        willDoNothing().given(response).setHeader(Mockito.anyString(), Mockito.anyString());


        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL + "/accessToken")
                                .headers(GenerateMockToken.getMockHeaderRefreshToken())
                                .headers(GenerateMockToken.getEmail())
                                .with(csrf())
                );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("accessToken-reIssue",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("리프레시 토큰"),
                                headerWithName("email").description("사용자 이메일")
                        ),
                        responseHeaders(
                                headerWithName("Access-Token").description("액세스 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("Reissue AccessToken(액세스 토큰 재발급 성공 메세지")
                                )
                        )
                ));
    }


    @DisplayName("로그 아웃")
    @WithMockUser
    @Test
    void memberLogoutTest() throws Exception {
        // given

        willDoNothing().given(tokenService).verificationLogOutToken(Mockito.any(HttpServletRequest.class));
        willDoNothing().given(tokenService).logoutService(Mockito.anyString(), Mockito.anyString());


        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL + "/logout")
                                .headers(GenerateMockToken.getMockHeaderToken())
                                .with(csrf())
                );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("logOut",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("Logout Success!(로그 아웃 성공 메세지")
                                )
                        )
                ));
    }


}