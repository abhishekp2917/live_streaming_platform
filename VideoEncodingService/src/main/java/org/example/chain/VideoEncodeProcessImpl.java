package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;
import org.example.factory.VideoEncodingServiceFactory;
import org.example.service.IVideoEncodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class VideoEncodeProcessImpl implements IVideoEncodingProcess {

    private IVideoEncodingService videoEncodingService;

    @Autowired
    public VideoEncodeProcessImpl(VideoEncodingServiceFactory videoEncodingServiceFactory) {
        this.videoEncodingService = videoEncodingServiceFactory.getServiceProvider();
    }

    @Override
    public void doProcess(IVideoEncodingContext context, IVideoEncodingProcessChain chain) throws Exception {
        File videoFile  = context.getVideoFile();
        videoEncodingService.encode(videoFile);
        chain.doProcess(context);
    }
}
