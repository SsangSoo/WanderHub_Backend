//package wanderhub.server.global.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import wanderhub.server.domain.member.entity.Member;
//import wanderhub.server.domain.member.service.MemberService;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//@Slf4j
//public class TestConfiguratioin {
//
//    @Autowired
//    MemberService memberService;
//
//    @PostConstruct
//    void membercreateee() {
//        Member member = Member.builder()
//                .Id(1L)
//                .email("gg@Gmail.com")
//                .nickName("내가 김하은인다.")
//                .name("어쩔")
//                .build();
//
//        log.info("member ok = {}", member);
//        memberService.createMember(member);
//        log.info("member Create!! ok!! = {}", member);
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//        log.info("member의 ID는?????????? = {} ", member.getId());
//
//    }
//
//
//}
