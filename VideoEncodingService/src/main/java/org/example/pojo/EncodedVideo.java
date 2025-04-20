package org.example.pojo;

import lombok.*;
import java.io.File;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodedVideo {

    private Map<String, File> resolutionToFileMap;
    private File masterPlaylist;
    private String videoFormat;
}
