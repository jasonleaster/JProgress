/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jasonleaster.progress.container;

import org.jasonleaster.progress.IProgressHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: jasonleaster
 * Date  : 2017/8/18
 * Email : jasonleaster@gmail.com
 * Description:
 */
public class InMemoryProgressContainer implements IProgressInstanceContainer {

    private final Map<String, IProgressHandler> container = new ConcurrentHashMap<>();

    @Override
    public IProgressHandler getProgress(String progressId) {
        return container.get(progressId);
    }

    @Override
    public void putProgress(IProgressHandler progress) {
        container.put(progress.getProgressId(), progress);
    }
}
