package org.example.service;

import java.io.IOException;

public interface IEncodedVideoUploadService <T>{

    boolean upload(T uploadConfig) throws IOException;
}
