/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

/**
 * Service for Member registration operations.
 */
@Slf4j
@Service
public class MemberRegistration {

    private final MemberRepository memberRepository;

    /**
     * Creates a new MemberRegistration service.
     *
     * @param memberRepository the repository for member operations
     */
    public MemberRegistration(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @throws Exception if registration fails
     */
    @Transactional
    public void register(Member member) throws Exception {
        log.info("Registering {}", member.getName());
        try {
            memberRepository.save(member);
        } catch (DuplicateKeyException e) {
            throw e; // Re-throw to be handled by controller
        }
    }
}
