/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.jasonleaster.progress;

import java.util.Arrays;
import org.jasonleaster.progress.container.IProgressInstanceContainer;
import org.jasonleaster.progress.container.InMemoryProgressContainer;

/**
 * Author: jasonleaster
 * Date  : 2017/8/17
 * Email : jasonleaster@gmail.com
 * Description:
 */
public final class TaskProgressService {

    private static final TaskProgressService instance = new TaskProgressService();

    private static final IProgressInstanceContainer container = new InMemoryProgressContainer();

    private TaskProgressService() {}

    public static TaskProgressService getInstance(){
        return instance;
    }

    /**
     * Create a progress with a id or name.
     *
     * @param progressId Id of progress
     * @return  {@link IProgressHandler}
     */
    public IProgressHandler createProgressHandler(String progressId) {
        synchronized (container){
            IProgressHandler progressHandler = container.getProgress(progressId);
            if (progressHandler == null){
                progressHandler = new DefaultProgressHandler(progressId);
                container.putProgress(progressHandler);
            }
            return progressHandler;
        }
    }

    public IProgressHandler createProgressHandler(String progressId, String progressName) {
        synchronized (container){
            IProgressHandler progressHandler = container.getProgress(progressId);
            if (progressHandler == null){
                progressHandler = new DefaultProgressHandler(progressId, progressName);
                container.putProgress(progressHandler);
            }
            return progressHandler;
        }
    }

    /**
     * This method will create a progress which will represent for the process of all sub-progress.
     * @param mainProgressId    The ID of the parent progress
     * @param mainProgressName  The name of the parent progress
     * @param subHandlers       The handlers for sub-progress
     * @return {@link IProgressHandler}. The total progress handler for sub-progress handlers
     */
    public IProgressHandler createProgressHandler(String mainProgressId, String mainProgressName,
        IProgressHandler[] subHandlers) {
        synchronized (container){
            IProgressHandler progressHandler = container.getProgress(mainProgressId);
            if (progressHandler == null){

                progressHandler = new DefaultProgressHandler(mainProgressId, mainProgressName,
                    Arrays.asList(subHandlers));

                container.putProgress(progressHandler);
            }
            return progressHandler;
        }
    }

    /**
     * Get the progress information with the given id of progress.
     * {@link null} will be returned if there doesn't exist a progress
     * with the give id {@param progressId}.
     * Otherwise, the basic progress status and value will be returned.
     *
     * @param progressId Id of progress
     * @return {@link ProgressInfo} or null
     */
    public ProgressInfo getProgressInfo(String progressId) {

        IProgressHandler handler = container.getProgress(progressId);

        if (handler == null){
            return null;
        }else {
            return handler.getProgressInfo();
        }
    }
}
