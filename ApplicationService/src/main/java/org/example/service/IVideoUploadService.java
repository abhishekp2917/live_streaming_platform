package org.example.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IVideoUploadService {

    boolean upload(MultipartFile file) throws IOException;
}
