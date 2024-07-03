package com.writeitup.wiu_post_service.service.impl;

import com.writeitup.wiu_post_service.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl {

    private final TagRepository tagRepository;
}
