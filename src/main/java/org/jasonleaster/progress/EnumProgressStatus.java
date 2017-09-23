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
public enum EnumProgressStatus {

    NOTSTART("notStart"),

    START("start"),

    RUNNING("running"),

    INTERRUPTED("interrupted"),

    CANCELED("canceled"),

    FINISHED("finished");

    private String status;

    EnumProgressStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
