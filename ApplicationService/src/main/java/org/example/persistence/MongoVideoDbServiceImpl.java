package org.example.persistence;

import com.mongodb.DuplicateKeyException;
import org.example.properties.VideoDBProperties;
import org.example.model.Video;
import org.example.repository.IVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class MongoVideoDbServiceImpl implements IVideoDbService {

    @Autowired
    private IVideoRepository videoRepository;

    @Autowired
    private VideoDBProperties videoDBProperties;

    @Override
    public Video save(Video video) {
        int attempt = 0;
        int maxAttempts = videoDBProperties.getMaxAttempts();
        while (attempt < maxAttempts) {
            video.setVideoId(UUID.randomUUID().toString());
            try {
                return videoRepository.save(video);
            } catch (DuplicateKeyException ex) {
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new RuntimeException("Failed to save video after multiple attempts due to ID collision.");
                }
            }
        }
        throw new RuntimeException("Unexpected error in saving video.");
    }

}
