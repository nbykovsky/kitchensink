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
package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Member entities.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Find all members ordered by name.
     *
     * @return list of members ordered by name
     */
    @Query("SELECT m FROM Member m ORDER BY m.name ASC")
    List<Member> findAllOrderedByName();

    /**
     * Find member by email.
     *
     * @param email the email to search for
     * @return the member with the given email
     */
    Member findByEmail(String email);

    /**
     * Find member by ID.
     *
     * @param id the ID to search for
     * @return the member with the given ID
     */
    default Member findById(long id) {
        return findById(Long.valueOf(id)).orElse(null);
    }
}
