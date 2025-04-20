package org.example.persistence;

import org.example.enums.VideoEncodingStatus;
import org.example.model.Video;

public interface IVideoDbService {

    Video getVideo(String videoId);

    Video updateEncodingStatus(String videoId, VideoEncodingStatus status);
}
