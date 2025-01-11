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
package org.jboss.as.quickstarts.kitchensink.rest;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/members")
public class MemberResourceRESTService {

    private final Logger log;
    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberResourceRESTService(Logger log, MemberRepository repository, MemberRegistration registration) {
        this.log = log;
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping
    public List<Member> listAllMembers() {
        return repository.findAllOrderedByName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> lookupMemberById(@PathVariable("id") long id) {
        Member member = repository.findById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            if (emailAlreadyExists(member.getEmail())) {
                Map<String, String> responseObj = new HashMap<>();
                responseObj.put("email", "Email taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
            }

            registration.register(member);
            return ResponseEntity.ok().build();
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private boolean emailAlreadyExists(String email) {
        try {
            return repository.findByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
