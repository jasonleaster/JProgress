package org.jasonleaster.progress.container;


import org.jasonleaster.progress.IProgressHandler;

/**
 * Author: jasonleaster
 * Date  : 2017/8/18
 * Email : jasonleaster@gmail.com
 * Description:
 */
public interface IProgressInstanceContainer {

    /**
     * Get Progress Handler from the container
     */
    IProgressHandler getProgress(String progressId);

    /**
     * Put Progress Handler into the container.
     */
    void putProgress(IProgressHandler progress);
}
