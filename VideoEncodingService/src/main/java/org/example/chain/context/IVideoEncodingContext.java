package org.example.chain.context;

import org.example.pojo.VideoEncodingMessage;
import java.io.File;
import java.io.Serializable;

public interface IVideoEncodingContext extends Serializable {

    VideoEncodingMessage getVideoEncodingMessage();

    File getVideoFile();

    void setVideoEncodingMessage(VideoEncodingMessage videoEncodingMessage);

    void setVideoFile(File videoFile);
}
