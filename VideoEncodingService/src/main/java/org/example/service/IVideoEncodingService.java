package org.example.service;

import java.io.File;

public interface IVideoEncodingService {
    void encode(File videoFile) throws Exception;
}
