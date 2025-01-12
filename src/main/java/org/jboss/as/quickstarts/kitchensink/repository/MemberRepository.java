package org.jboss.as.quickstarts.kitchensink.repository;

import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' }}")
    List<Member> findAllOrderedByName(String namePattern);

    @Query(value = "{}", sort = "{ 'name': 1 }")
    List<Member> findAllOrderedByName();

    Member findByEmail(String email);
}