package org.jboss.as.quickstarts.kitchensink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class MongoIndexInitializer {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoMappingContext mongoMappingContext;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        try {
            IndexOperations indexOps = mongoTemplate.indexOps(Member.class);
            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
            resolver.resolveIndexFor(Member.class).forEach(indexOps::ensureIndex);
        } catch (Exception e) {
            // Log the error but don't prevent application startup
            System.err.println("Error creating MongoDB indices: " + e.getMessage());
        }
    }
} 