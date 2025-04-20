package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chain.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan("org.example")
public class AppConfig {

    @Autowired
    private VideoDownloadProcessImpl videoDownloadProcess;

    @Autowired
    private VideoEncodeProcessImpl videoEncodeProcess;

    @Autowired
    private EncodedVideoUploadProcessImpl encodedVideoUploadProcess;

    @Bean
    public Jackson2JsonMessageConverter getJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IVideoEncodingProcessChain getVideoEncodingProcessChain() {
        List<IVideoEncodingProcess> intialProcessList = Arrays.asList(videoDownloadProcess, videoEncodeProcess, encodedVideoUploadProcess);
        IVideoEncodingProcessChain videoEncodingProcessChain = new VideoEncodingProcessChainImpl(intialProcessList);
        return videoEncodingProcessChain;
    }
}
