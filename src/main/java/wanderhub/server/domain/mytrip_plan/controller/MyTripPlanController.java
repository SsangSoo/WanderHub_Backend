package wanderhub.server.domain.mytrip_plan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.domain.mytrip_plan.mapper.MyTripMapper;
import wanderhub.server.domain.mytrip_plan.service.MyTripPlanService;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/v1/mytrip")
public class MyTripPlanController {

    private final TokenService tokenService;
    private final MyTripPlanService myTripPlanService;
    private final MyTripMapper myTripMapper;

    public MyTripPlanController(TokenService tokenService, MyTripPlanService myTripPlanService, MyTripMapper myTripMapper) {
        this.tokenService = tokenService;
        this.myTripPlanService = myTripPlanService;
        this.myTripMapper = myTripMapper;
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity postMyTrip(HttpServletRequest request,
                                     Principal principal,
                                     @Validated @RequestBody MyTripPlanDto.Post post) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 토큰 확인
        MyTripPlan postMyTripPlan = myTripMapper.myTripPlanPostDtoToMyTripPlanEntity(post);
        return new ResponseEntity(new SingleResponse<>(myTripPlanService.createTripPlan(principal.getName(), postMyTripPlan)), HttpStatus.CREATED);
    }

    // 일정 업데이트
    @PatchMapping("/{myTripPlan-id}")
    public ResponseEntity patchMyTrip(HttpServletRequest request,
                                     Principal principal,
                                     @PathVariable("myTripPlan-id")Long myTripPlanId,
                                     @Validated @RequestBody MyTripPlanDto.Patch patch) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 토큰 확인
        MyTripPlan patchMyTripPlan = myTripMapper.myTripPlanPatchDtoToMyTripPlanEntity(patch);
        return ResponseEntity.ok(myTripPlanService.updateMyTripPlan(principal.getName(), myTripPlanId, patchMyTripPlan));
    }

    // 일정 삭제
    @DeleteMapping("/{myTripPlan-id}")
    public ResponseEntity patchMyTrip(HttpServletRequest request,
                                      Principal principal,
                                      @PathVariable("myTripPlan-id")Long myTripPlanId) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 토큰 확인
        myTripPlanService.removeMyTripPlan(principal.getName(), myTripPlanId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}