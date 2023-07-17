package wanderhub.server.domain.mytrip_plan_detail.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailDto;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.domain.mytrip_plan_detail.mapper.MyTripPlanDetailMapper;
import wanderhub.server.domain.mytrip_plan_detail.service.MyTripPlanDetailService;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/v1/mytrip/{myTripPlan-id}/details")
public class MyTripPlanDetailController {

    private final TokenService tokenService;
    private final MyTripPlanDetailService myTripPlanDetailService;
    private final MyTripPlanDetailMapper myTripPlanDetailMapper;

    public MyTripPlanDetailController(TokenService tokenService, MyTripPlanDetailService myTripPlanDetailService, MyTripPlanDetailMapper myTripPlanDetailMapper) {
        this.tokenService = tokenService;
        this.myTripPlanDetailService = myTripPlanDetailService;
        this.myTripPlanDetailMapper = myTripPlanDetailMapper;
    }

    // 일정 디테일 생성
    @PostMapping
    public ResponseEntity postMyTripPlanDetail(HttpServletRequest request,
                                               @PathVariable("myTripPlan-id")Long myTripPlanId,
                                               @Validated @RequestBody MyTripPlanDetailDto.Post postTripPlanDetail,
                                               Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 체크
        MyTripPlanDetail myTripPlanDetail = myTripPlanDetailMapper.myTripPlanDetailPostDtoToEntity(postTripPlanDetail);
        return new ResponseEntity(new SingleResponse<>(myTripPlanDetailService.createTripPlanDetail(principal.getName(), myTripPlanId, myTripPlanDetail)), HttpStatus.CREATED);
    }
}
