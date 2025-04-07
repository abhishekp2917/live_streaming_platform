package org.example.service;

import org.example.model.EncodedVideo;
import java.io.File;

public interface IVideoEncodingService {
    EncodedVideo encode(File videoFile) throws Exception;
}
