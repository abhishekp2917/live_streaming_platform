package org.example.factory;

import org.example.service.IVideoDownloadService;
import org.example.service.IVideoEncodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VideoEncodingServiceFactory {

    private final Map<String, IVideoEncodingService> videoEncodingServiceMap;

    private final String serviceProvider;

    @Autowired
    public VideoEncodingServiceFactory(Map<String, IVideoEncodingService> videoEncodingServiceMap,
                                       @Value("${video.encoding.service-provider}") String serviceProvider) {
        this.videoEncodingServiceMap = videoEncodingServiceMap;
        this.serviceProvider = serviceProvider.toLowerCase();
    }

    public IVideoEncodingService getServiceProvider() {
        return videoEncodingServiceMap.get(serviceProvider);
    }
}
