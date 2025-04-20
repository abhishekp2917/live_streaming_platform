package org.example.repository;

import org.example.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVideoRepository extends MongoRepository<Video, String> {
}
