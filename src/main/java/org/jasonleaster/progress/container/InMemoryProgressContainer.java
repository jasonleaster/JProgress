package org.jasonleaster.progress.container;

import java.util.HashMap;
import java.util.Map;
import org.jasonleaster.progress.IProgressHandler;

/**
 * Author: jasonleaster
 * Date  : 2017/8/18
 * Email : jasonleaster@gmail.com
 * Description:
 */
public class InMemoryProgressContainer implements IProgressInstanceContainer {

    Map<String, IProgressHandler> container = new HashMap<>();

    @Override
    public IProgressHandler getProgress(String progressId) {
        return container.get(progressId);
    }

    @Override
    public void putProgress(IProgressHandler progress) {
        container.put(progress.getProgressId(), progress);
    }
}
