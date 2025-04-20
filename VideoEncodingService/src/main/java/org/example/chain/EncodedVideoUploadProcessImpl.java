package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;
import org.example.factory.EncodedVideoUploadServiceFactory;
import org.example.pojo.VideoEncodingMessage;
import org.example.service.IEncodedVideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncodedVideoUploadProcessImpl implements IVideoEncodingProcess {

    private IEncodedVideoUploadService encodedVideoUploadService;

    @Autowired
    public EncodedVideoUploadProcessImpl(EncodedVideoUploadServiceFactory encodedVideoUploadServiceFactory) {
        this.encodedVideoUploadService = encodedVideoUploadServiceFactory.getServiceProvider();
    }

    @Override
    public void doProcess(IVideoEncodingContext context, IVideoEncodingProcessChain chain) throws Exception {
        VideoEncodingMessage videoEncodingMessage = context.getVideoEncodingMessage();
        encodedVideoUploadService.upload(videoEncodingMessage);
        chain.doProcess(context);
    }
}
