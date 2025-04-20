package org.example.service;

import java.io.File;
import java.io.IOException;

public interface IVideoDownloadService <T> {

    File download(T downloadConfig) throws IOException;
}
