package org.example.factory;

import org.example.service.IEncodedVideoUploadService;
import org.example.service.IVideoEncodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class EncodedVideoUploadServiceFactory {

    private final Map<String, IEncodedVideoUploadService> encodedVideoUploadServiceMap;

    private final String serviceProvider;

    @Autowired
    public EncodedVideoUploadServiceFactory(Map<String, IEncodedVideoUploadService> encodedVideoUploadServiceMap,
                                            @Value("${encoded.video.upload.service-provider}") String serviceProvider) {
        this.encodedVideoUploadServiceMap = encodedVideoUploadServiceMap;
        this.serviceProvider = serviceProvider.toLowerCase();
    }

    public IEncodedVideoUploadService getServiceProvider() {
        return encodedVideoUploadServiceMap.get(serviceProvider);
    }
}
