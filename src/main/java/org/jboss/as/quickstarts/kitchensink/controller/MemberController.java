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
package org.jboss.as.quickstarts.kitchensink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;

@Controller
public class MemberController {

    private final MemberRegistration registration;
    private final MemberRepository memberRepository;

    public MemberController(MemberRegistration registration, MemberRepository memberRepository) {
        this.registration = registration;
        this.memberRepository = memberRepository;
    }

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("members", memberRepository.findAllOrderedByName());
        model.addAttribute("member", new Member());
        return "index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("member") Member member, 
                          BindingResult result, 
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberRepository.findAllOrderedByName());
            return "index";
        }
        
        try {
            registration.register(member);
            return "redirect:/";
        } catch (DuplicateKeyException e) {
            result.rejectValue("email", "duplicate", "This email is already registered");
            model.addAttribute("members", memberRepository.findAllOrderedByName());
            return "index";
        } catch (Exception e) {
            result.rejectValue("email", "error", "An error occurred during registration");
            model.addAttribute("members", memberRepository.findAllOrderedByName());
            return "index";
        }
    }
}
