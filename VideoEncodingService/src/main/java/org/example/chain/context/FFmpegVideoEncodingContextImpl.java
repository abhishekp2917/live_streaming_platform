package org.example.chain.context;

import lombok.*;
import org.example.pojo.VideoEncodingMessage;
import java.io.File;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FFmpegVideoEncodingContextImpl implements IVideoEncodingContext {

    private VideoEncodingMessage videoEncodingMessage;
    private File videoFile;

}
