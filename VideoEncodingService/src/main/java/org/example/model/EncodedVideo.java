package org.example.model;

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

    private File manifestFile;

    private String videoFormat;
}
