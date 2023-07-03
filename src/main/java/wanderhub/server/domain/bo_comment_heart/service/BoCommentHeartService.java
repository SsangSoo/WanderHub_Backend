package wanderhub.server.domain.bo_comment_heart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;
import wanderhub.server.domain.bo_comment_heart.repository.BoCommentHeartRepository;
import wanderhub.server.domain.member.entity.Member;

import java.util.Optional;

@Service
@Transactional
public class BoCommentHeartService {

    private final BoCommentHeartRepository boCommentHeartRepository;

    public BoCommentHeartService(BoCommentHeartRepository boCommentHeartRepository) {
        this.boCommentHeartRepository = boCommentHeartRepository;
    }

    public Optional<BoCommentHeart> findByBoCommentAndMember(Long boCommentId, String email) {
        return boCommentHeartRepository.findByBoCommentAndMember(boCommentId, email);
    }

    public void removeBoCommentHeart(BoCommentHeart boCommentHeartWillRemove) {
        boCommentHeartRepository.delete(boCommentHeartWillRemove);
    }

    public void createBoCommentHeart(Member member, BoComment boComment) {
        BoCommentHeart createdBoCommentHeart = BoCommentHeart.builder()
                .member(member)
                .boComment(boComment)
                .build();
        boCommentHeartRepository.save(createdBoCommentHeart);
    }
}
