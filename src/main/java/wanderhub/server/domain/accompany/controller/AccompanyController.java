package wanderhub.server.domain.accompany.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.dto.AccompanyPostDto;
import wanderhub.server.domain.accompany.dto.AccompanySearchCondition;
import wanderhub.server.domain.accompany.service.AccompanyService;
import wanderhub.server.global.response.MessageResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accompany")
public class AccompanyController {

    private final TokenService tokenService;
    private final AccompanyService accompanyService;

    // 동행 생성
    @PostMapping
    public ResponseEntity postAccompany(HttpServletRequest request, @Validated @RequestBody AccompanyDto.Post post, Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 JWT확인
        accompanyService.createAccompany(post, principal.getName());
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 동행 생성 - 리팩터링
    @PostMapping("/refactoring")
    public ResponseEntity postAccompanyRefactoring(HttpServletRequest request, @Validated @RequestBody AccompanyPostDto accompanyPostDto, Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 JWT확인
        accompanyService.createAccompanyRefactoring(accompanyPostDto, principal.getName());
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 동행 수정
    @PatchMapping("/{accompany-id}")
    public ResponseEntity patchAccompany(HttpServletRequest request,
                                         @PathVariable("accompany-id")Long accompanyId,
                                         @Validated @RequestBody AccompanyPostDto accompanyPatchDto,
                                         Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token 확인
        return ResponseEntity.ok(accompanyService.updateAccompany(accompanyId, principal.getName(), accompanyPatchDto));
    }


    // 동행 삭제
    @DeleteMapping("/{accompany-id}")
    public ResponseEntity deleteAccompany(HttpServletRequest request,
                                          @PathVariable("accompany-id")Long accompanyId,
                                          Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
        accompanyService.removeAccompany(accompanyId, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 동행 단일 조회
    @GetMapping("/{accompany-id}")
    public ResponseEntity getAccompany(@PathVariable("accompany-id")Long accompanyId) {
        return ResponseEntity.ok(accompanyService.getAccompany(accompanyId));
    }


    // 동행 전체 조회(날짜, 지역)
    @GetMapping
    public ResponseEntity searchLocalAndDate(AccompanySearchCondition accompanySearchCondition,
                                             @RequestParam(name = "page", defaultValue = "1") Integer page) throws ParseException {
        return ResponseEntity.ok(accompanyService.findByLocalAndDate(accompanySearchCondition, page));
    }

    // 동행 참여 > Patch
    @PatchMapping("/{accompany-id}/join")
    public ResponseEntity joinAccompany(HttpServletRequest request,
                                        @PathVariable("accompany-id")Long accompanyId,
                                        Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
        return ResponseEntity.ok(accompanyService.joinAccompany(accompanyId, principal.getName()));
    }


    // 동행 나가기 > Patch
    @PatchMapping("/{accompany-id}/quit")
    public ResponseEntity outAccompany(HttpServletRequest request,
                                        @PathVariable("accompany-id")Long accompanyId,
                                        Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
        return ResponseEntity.ok(accompanyService.outAccompany(accompanyId, principal.getName()));
    }

    // 동행 모집 완료
    @PatchMapping("/{accompany-id}/recruitComplete")
    public ResponseEntity recruitCompleteAccompany(HttpServletRequest request,
                                                   @PathVariable("accompany-id")Long accompanyId,
                                                   Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
        if(accompanyService.recruitComplete(accompanyId, principal.getName())) {
            return ResponseEntity.ok(new MessageResponseDto("Accompany recruitComplete!"));
        }
        return ResponseEntity.ok(new MessageResponseDto("Accompany recruitComplete Cancel!"));
    }
}