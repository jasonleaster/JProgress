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

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: jasonleaster
 * Date  : 2017/8/18
 * Email : jasonleaster@gmail.com
 */
@Data
@EqualsAndHashCode
public class ProgressInfo implements Serializable{

    private String progressId;

    private double value;

    private EnumProgressStatus status;

    private List<ProgressInfo> subProgress;

    private String progressName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Map<String, Object> attachedInfo;

    public ProgressInfo(String progressId) {
        this(progressId, "ProgressNameFor : " + progressId);
    }

    public ProgressInfo(String progressId, String progressName) {
        this.progressId = progressId;
        this.progressName = progressName;
        this.status = EnumProgressStatus.NOTSTART;
        this.value  = 0.0;
        this.attachedInfo = new ConcurrentHashMap<>();
    }

    public ProgressInfo(ProgressInfo other){
        this.progressId = other.progressId;
        this.progressName = other.progressName;
        this.value = other.value;
        this.status = other.status;
        this.startTime = other.startTime;
        this.endTime   = other.endTime;
        this.attachedInfo = new ConcurrentHashMap<>(other.attachedInfo);

        // Attention! But we don't copy the list
        // We will do it later
    }
}
