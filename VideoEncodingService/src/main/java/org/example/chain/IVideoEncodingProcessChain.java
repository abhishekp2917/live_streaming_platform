package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;

public interface IVideoEncodingProcessChain {

    void doProcess(IVideoEncodingContext context) throws Exception;
}
