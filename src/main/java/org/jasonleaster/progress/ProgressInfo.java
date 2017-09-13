package org.jasonleaster.progress;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    public ProgressInfo(String progressId) {
        this.progressId = progressId;
        this.progressName = "ProgressNameFor : " + progressId;
        this.status = EnumProgressStatus.NOTSTART;
        this.value  = 0.0;
    }

    public ProgressInfo(String progressId, String progressName) {
        this.progressId = progressId;
        this.progressName = progressName;
        this.status = EnumProgressStatus.NOTSTART;
        this.value  = 0.0;
    }

    public ProgressInfo(ProgressInfo other){
        this.progressId = other.progressId;
        this.progressName = other.progressName;
        this.value = other.value;
        this.status = other.status;
        this.startTime = other.startTime;
        this.endTime   = other.endTime;

        // Attention! But we don't copy the list
        // We will do it later
    }
}
