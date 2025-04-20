package org.example.persistence;

import org.example.enums.VideoEncodingStatus;
import org.example.model.Video;
import org.example.repository.IVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class MongoVideoDbServiceImpl implements IVideoDbService {

    @Autowired
    private IVideoRepository videoRepository;

    @Override
    public Video getVideo(String videoId) {
        Video video = null;
        try{
            video = videoRepository.findById(videoId).get();
            return video;
        }
        catch (NoSuchElementException ex) {
            throw new RuntimeException("Video not found with id: " + videoId);
        }
    }

    public Video updateEncodingStatus(String videoId, VideoEncodingStatus status) {
        Optional<Video> optionalVideo = videoRepository.findById(videoId);
        if (optionalVideo.isEmpty()) {
            throw new RuntimeException("Video with ID " + videoId + " not found");
        }
        Video video = optionalVideo.get();
        video.setEncodingStatus(status);
        return videoRepository.save(video);
    }
}
