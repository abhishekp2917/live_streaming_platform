package org.example.chain;

import org.example.chain.context.IVideoEncodingContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoEncodingProcessChainImpl implements IVideoEncodingProcessChain {

    private final List<IVideoEncodingProcess> processes = new ArrayList<>();
    private Iterator<IVideoEncodingProcess> iterator;

    public VideoEncodingProcessChainImpl() {

    }

    public VideoEncodingProcessChainImpl(List<IVideoEncodingProcess> initialProcesses) {
        if (initialProcesses != null) {
            this.processes.addAll(initialProcesses);
        }
        resetIterator();
    }

    @Override
    public void doProcess(IVideoEncodingContext context) throws Exception {
        if (iterator.hasNext()) {
            IVideoEncodingProcess nextProcess = iterator.next();
            nextProcess.doProcess(context, this);
        }
    }

    public void addProcessBefore(Class<? extends IVideoEncodingProcess> targetProcessClass,
                                 IVideoEncodingProcess newProcess) {
        int index = findFilterIndex(targetProcessClass);
        if (index != -1) {
            processes.add(index, newProcess);
        } else {
            processes.add(0, newProcess);
        }
        resetIterator();
    }

    public void addProcessAfter(Class<? extends IVideoEncodingProcess> targetProcessClass,
                               IVideoEncodingProcess newProcess) {
        int index = findFilterIndex(targetProcessClass);
        if (index != -1) {
            processes.add(index + 1, newProcess);
        } else {
            processes.add(newProcess);
        }
        resetIterator();
    }

    private int findFilterIndex(Class<? extends IVideoEncodingProcess> targetClass) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getClass().equals(targetClass)) {
                return i;
            }
        }
        return -1;
    }

    private void resetIterator() {
        this.iterator = processes.iterator();
    }
}
