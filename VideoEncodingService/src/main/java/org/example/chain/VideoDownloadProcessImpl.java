package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;
import org.example.factory.VideoDownloadServiceFactory;
import org.example.pojo.VideoEncodingMessage;
import org.example.service.IVideoDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class VideoDownloadProcessImpl implements IVideoEncodingProcess {

    private IVideoDownloadService videoDownloadService;

    @Autowired
    public VideoDownloadProcessImpl(VideoDownloadServiceFactory videoDownloadServiceFactory) {
        this.videoDownloadService = videoDownloadServiceFactory.getServiceProvider();
    }

    @Override
    public void doProcess(IVideoEncodingContext context, IVideoEncodingProcessChain chain) throws Exception {
        VideoEncodingMessage videoEncodingMessage = context.getVideoEncodingMessage();
        File videoFile =  videoDownloadService.download(videoEncodingMessage);
        context.setVideoFile(videoFile);
        chain.doProcess(context);
    }
}
