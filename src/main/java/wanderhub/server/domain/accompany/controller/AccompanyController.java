package wanderhub.server.domain.accompany.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.mapper.AccompanyMapper;
import wanderhub.server.domain.accompany.service.AccompanyService;
import wanderhub.server.global.response.PageInfo;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/accompany")
public class AccompanyController {

    private final TokenService tokenService;
    private final AccompanyMapper accompanyMapper;
    private final AccompanyService accompanyService;

    public AccompanyController(TokenService tokenService, AccompanyMapper accompanyMapper, AccompanyService accompanyService) {
        this.tokenService = tokenService;
        this.accompanyMapper = accompanyMapper;
        this.accompanyService = accompanyService;
    }

    // 동행 생성
    @PostMapping
    public ResponseEntity postAccompany(HttpServletRequest request, @Validated @RequestBody AccompanyDto.Post post, Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token확인
        Accompany accompanyEntityFromPostDto = accompanyMapper.accompanyPostDtoToAccompanyEntity(post);   // Mppaer로 Accompany Entity 생성
        Accompany createdAccompany = accompanyService.createAccompany(accompanyEntityFromPostDto, principal.getName());
        AccompanyDto.Response accompanyResponse = accompanyMapper.accompanyEntityToAccompanyResponseDto(createdAccompany);
        return new ResponseEntity(new SingleResponse<>(accompanyResponse), HttpStatus.CREATED);
    }


    // 동행 수정
    @PatchMapping("/{accompany-id}")
    public ResponseEntity patchAccompany(HttpServletRequest request,
                                         @PathVariable("accompany-id")Long accompanyId,
                                         @Validated @RequestBody AccompanyDto.Patch patch,
                                         Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token 확인
        Accompany accompanyEntityFromPatchDto = accompanyMapper.accompanyPatchDtoToAccompanyEntity(patch);
        Accompany patchedAccompany = accompanyService.updateAccompany(accompanyId, principal.getName(), accompanyEntityFromPatchDto);
        AccompanyDto.Response accompanyResponse = accompanyMapper.accompanyEntityToAccompanyResponseDto(patchedAccompany);
        return ResponseEntity.ok(accompanyResponse);
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
        Accompany getAccompany = accompanyService.getAccompany(accompanyId);
        AccompanyDto.Response accompanyEntityToResponse = accompanyMapper.accompanyEntityToAccompanyResponseDto(getAccompany);
        return ResponseEntity.ok(accompanyEntityToResponse);
    }

    // 동행 목록(페이지 네이션)
    @GetMapping
    public ResponseEntity getAccompanies(@PageableDefault Pageable pageable){
        Page<Accompany> accompanyEntityListPage = accompanyService.findAccompanies(pageable);
        return  new ResponseEntity<>(
                new PageResponseDto<>(accompanyMapper.accompanyEntityListToResponseList(accompanyEntityListPage.getContent()),
                        new PageInfo(accompanyEntityListPage.getPageable(), accompanyEntityListPage.getTotalElements())), HttpStatus.OK);
    }


    // 동행 참여 > Patch
//    @PatchMapping("/{accompany-id}/join")
//    public ResponseEntity joinAccompany(HttpServletRequest request,
//                                        @PathVariable("accompany-id")Long accompanyId,
//                                        Principal principal) {
//        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
//
//    }
//
//
//    // 동행 나가기 > Patch
//    @PatchMapping("/{accompany-id}/out")
//    public ResponseEntity joinAccompany(HttpServletRequest request,
//                                        @PathVariable("accompany-id")Long accompanyId,
//                                        Principal principal) {
//        tokenService.verificationLogOutToken(request); // 블랙리스트 Token 확인
//
//    }



}