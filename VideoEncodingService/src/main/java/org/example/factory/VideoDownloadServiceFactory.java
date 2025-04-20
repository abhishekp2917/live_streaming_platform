package org.example.factory;

import org.example.service.IVideoDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class VideoDownloadServiceFactory {

    private final Map<String, IVideoDownloadService> videoDownloadServiceMap;

    private final String serviceProvider;

    @Autowired
    public VideoDownloadServiceFactory(Map<String, IVideoDownloadService> videoDownloadServiceMap,
                                       @Value("${video.download.service-provider}") String serviceProvider) {
        this.videoDownloadServiceMap = videoDownloadServiceMap;
        this.serviceProvider = serviceProvider.toLowerCase();
    }

    public IVideoDownloadService getServiceProvider() {
        return videoDownloadServiceMap.get(serviceProvider);
    }
}
