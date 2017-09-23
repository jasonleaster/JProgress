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

/**
 * Author: jasonleaster
 * Date  : 2017/8/17
 * Email : jasonleaster@gmail.com
 */
public interface IProgressHandler {

    /**
     * Start the progress with this handler
     */
    void start();

    /**
     * Update the current progress value with the parameter.
     * @param progressInPercentage progress value in percentage like "80.0"
     * means 80.0% of the progress have been finished.
     */
    void update(double progressInPercentage);

    /**
     * Cancel the running progress. If the progress have sub-progress,
     * all of them will also be canceled.
     */
    void cancel();

    /**
     * Try to set the status of the progress and
     * <i>all<i/> the sub-progress as "finished".
     */
    void end();

    void addSubProgress(IProgressHandler subProgress, double weight);

    /**
     * @return The ID of this progress.
     */
    String getProgressId();

    /**
     * This method will return a snapshot of the basic information
     * about this progress including the status of sub-progress.
     * @return {@link ProgressInfo}
     */
    ProgressInfo getProgressInfo();
}
