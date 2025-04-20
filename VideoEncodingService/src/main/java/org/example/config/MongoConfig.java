package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.example.properties.MongoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Autowired
    private MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        String connectionString = mongoProperties.getUri()
                .replace("{username}", mongoProperties.getUsername())
                .replace("{password}", mongoProperties.getPassword())
                .replace("{hostname}", mongoProperties.getHost())
                .replace("{port}", String.valueOf(mongoProperties.getPort()))
                .replace("{database}", mongoProperties.getDatabase())
                .replace("{authDatabase}", mongoProperties.getAuthDatabase())
                .replace("{authMechanism}", mongoProperties.getAuthMechanism());
        return MongoClients.create(connectionString);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), mongoProperties.getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }
}
