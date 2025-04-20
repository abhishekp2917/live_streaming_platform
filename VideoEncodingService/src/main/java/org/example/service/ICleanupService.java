package org.example.service;

import java.io.IOException;

public interface ICleanupService <T> {

    void cleanUp(T cleanupConfig) throws IOException;
}
