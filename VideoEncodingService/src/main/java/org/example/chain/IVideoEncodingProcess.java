package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;

import java.io.IOException;

public interface IVideoEncodingProcess {

    void doProcess(IVideoEncodingContext context, IVideoEncodingProcessChain chain) throws Exception;
}
