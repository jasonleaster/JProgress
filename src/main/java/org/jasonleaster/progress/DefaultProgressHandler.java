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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Author: jasonleaster
 * Date  : 2017/8/17
 * Email : jasonleaster@gmail.com
 * Description:
 */
class DefaultProgressHandler implements IProgressHandler {

    private static final Logger logger = Logger.getLogger(DefaultProgressHandler.class);

    private ProgressInfo progressInfo;

    private List<IProgressHandler> subProgressHandlers;

    public DefaultProgressHandler(String progressId) {
        this.progressInfo = new ProgressInfo(progressId);
    }

    public DefaultProgressHandler(String progressId, String progressName) {
        this.progressInfo = new ProgressInfo(progressId, progressName);
    }

    public DefaultProgressHandler(String progressId, String progressName,
        List<IProgressHandler> subProgress) {
        this.progressInfo = new ProgressInfo(progressId, progressName);
        this.subProgressHandlers = subProgress;
    }

    @Override
    public void start() {
        progressInfo.setStartTime(LocalDateTime.now());
        progressInfo.setStatus(EnumProgressStatus.START);
        progressInfo.setValue(0.0);
    }

    @Override
    public void update(double progressInPercentage) {
        progressInfo.setStatus(EnumProgressStatus.RUNNING);
        progressInfo.setValue(progressInPercentage);
    }

    @Override
    public void cancel() {

        if (subProgressHandlers != null) {
            for (IProgressHandler subProgress : subProgressHandlers) {
                subProgress.cancel();
            }
        }

        progressInfo.setEndTime(LocalDateTime.now());
        progressInfo.setStatus(EnumProgressStatus.CANCELED);
    }

    @Override
    public void end() {
        if (subProgressHandlers != null) {
            for (IProgressHandler subProgress : subProgressHandlers) {
                if (subProgress != null
                    && subProgress.getProgressInfo().getStatus() != EnumProgressStatus.FINISHED) {
                    System.out.println("ERROR! There have sub-progress un-finished!");
                    return;
                }
            }
        }

        progressInfo.setEndTime(LocalDateTime.now());
        progressInfo.setStatus(EnumProgressStatus.FINISHED);
    }

    @Override
    public void addSubProgress(IProgressHandler subProgress, double weight) {
        if (weight < 0){
            logger.error("Failed to add sub-progress for the progress");
            logger.error("Parameter @weight should not smaller than 0 !"
                + "Please that's bigger than zero and try again later !");
            return;
        }

        if (subProgress != null){
            if (this.progressInfo.getStatus() != EnumProgressStatus.NOTSTART){
                logger.error("Can't add sub-progress after started.");
                return;
            }
            this.subProgressHandlers.add(subProgress);
        }
    }

    @Override
    public String getProgressId() {
        return progressInfo.getProgressId();
    }

    @Override
    public ProgressInfo getProgressInfo() {
        if (subProgressHandlers == null || subProgressHandlers.size() == 0) {
            return new ProgressInfo(progressInfo);
        }

        int finishedCounts = 0;
        int totalCounts = subProgressHandlers.size();
        List<ProgressInfo> subProgressInfoSnapshot = new ArrayList<>();
        for (IProgressHandler handler : subProgressHandlers) {

            ProgressInfo progressInfo = handler.getProgressInfo();
            subProgressInfoSnapshot.add(progressInfo);

            if (progressInfo != null
                && progressInfo.getStatus() == EnumProgressStatus.FINISHED) {
                finishedCounts++;
            }
        }

        if (finishedCounts == totalCounts) {
            this.end();
        } else{
            this.update(finishedCounts * 100. / totalCounts);
        }

        ProgressInfo snapshot = new ProgressInfo(progressInfo);
        snapshot.setSubProgress(subProgressInfoSnapshot);

        // return a value based copy(Shallow Copy) of this object
        return snapshot;
    }

    @Override
    public void putCustomInfo(String name, Object info) {
        this.progressInfo.getAttachedInfo().put(name, info);
    }

    @Override
    public Object getCustomInfo(String name) {
        return this.progressInfo.getAttachedInfo().get(name);
    }
}
