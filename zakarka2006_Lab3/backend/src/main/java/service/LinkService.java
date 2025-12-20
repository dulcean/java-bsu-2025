package com.example.linkshortener.service;

import com.example.linkshortener.model.Link;
import com.example.linkshortener.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final LinkRepository linkRepository;


    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }


    @Transactional
    public Link createShortLink(String originalUrl) {
        String code;
        do {
            code = generateCode();
        } while (linkRepository.existsByCode(code));

        Link link = new Link();
        link.setCode(code);
        link.setOriginalUrl(originalUrl);
        
        return linkRepository.save(link);
    }


    @Transactional
    public Optional<Link> getAndTrackClick(String code) {
        Optional<Link> linkOpt = linkRepository.findByCode(code);
        linkOpt.ifPresent(link -> {
            link.setClickCount(link.getClickCount() + 1);
            linkRepository.save(link);
        });
        return linkOpt;
    }


    public Optional<Link> getByCode(String code) {
        return linkRepository.findByCode(code);
    }


    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }


    @Transactional
    public boolean deleteByCode(String code) {
        Optional<Link> link = linkRepository.findByCode(code);
        if (link.isPresent()) {
            linkRepository.delete(link.get());
            return true;
        }
        return false;
    }
}
