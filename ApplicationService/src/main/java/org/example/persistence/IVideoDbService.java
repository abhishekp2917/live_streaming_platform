package org.example.persistence;

import org.example.model.Video;

public interface IVideoDbService {

    Video save(Video video);

}
