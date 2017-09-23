package org.jasonleaster.progress;

import org.junit.Assert;
import org.junit.Test;

/**
 * Author: jasonleaster
 * Date  : 2017/8/17
 * Email : jasonleaster@gmail.com
 * Description:
 *  Unit test for JProgress and demo for how to use it
 */
public class IProgressHandlerTest {

    /**
     * One Second in millisecond
     */
    private static final int ONE_SECOND = 1000;

    private Thread userTask;

    /**
     * Simulate for a client which is polling the status of the progress
     */
    @Test
    public void shouldReturnProgressInfo(){
        final String customPID = "42";
        userTask = new Thread(new ProgressWithThread(customPID), "DemoThreadForHowToUseTaskProgressService");
        userTask.start();

        EnumProgressStatus currentStatus = pollingStatusOfProgress(customPID);

        waitThreadToEnd(userTask);

        Assert.assertEquals(currentStatus, EnumProgressStatus.FINISHED);
    }

    @Test
    public void shouldExistSafelyAfterInterruptionHappened(){
        String customPID = "42";
        userTask = new Thread(new ProgressWithThread(customPID), "DemoThreadForHowToUseTaskProgressService");
        userTask.start();

        Thread rascal = new Thread(() -> {

            synchronized (this){
                for (;true;){
                    ProgressInfo progressInfo = TaskProgressService.getInstance().getProgressInfo(customPID);
                    if (progressInfo != null &&
                        progressInfo.getStatus() == EnumProgressStatus.RUNNING &&
                        progressInfo.getValue() > 0.4){

                        userTask.interrupt();
                        return;
                    } else {
                        try {
                            this.wait(ONE_SECOND / 4);
                        }catch (InterruptedException e){
                            log("Polling thread has been interrupted!");
                        }
                    }
                }
            }
        });

        rascal.start();

        EnumProgressStatus currentStatus = pollingStatusOfProgress(customPID);

        waitThreadToEnd(rascal);
        waitThreadToEnd(userTask);

        Assert.assertEquals(currentStatus, EnumProgressStatus.CANCELED);
    }

    @Test
    public void differentWayToCreateSubProgress()
    {
        // TODO
    }

    @Test
    public void shouldReturnMultiTaskProgressStatus(){
        String mainProgressId = "mainProgressId";
        int numOfSubProgress = 3;
        Thread[] subProgress = new Thread[numOfSubProgress];
        IProgressHandler[] progressHandlers = new IProgressHandler[numOfSubProgress];

        for (int i = 0; i < numOfSubProgress; i++){
            progressHandlers[i] = TaskProgressService.getInstance()
                .createProgressHandler("subProgress" + i);

            subProgress[i] = new Thread(new ProgressWithThread(progressHandlers[i]));
            subProgress[i].start();
        }

        TaskProgressService.getInstance()
            .createProgressHandler(mainProgressId,  "mainProgress", progressHandlers);

        EnumProgressStatus finalStatusOfMainProgress = pollingStatusOfProgress(mainProgressId);

        for (Thread job : subProgress){
            waitThreadToEnd(job);
        }

        Assert.assertEquals(finalStatusOfMainProgress, EnumProgressStatus.FINISHED);
    }

    private EnumProgressStatus pollingStatusOfProgress(String customPID){

        EnumProgressStatus currentStatus = EnumProgressStatus.NOTSTART;
        ProgressInfo progressInfo = null;

        synchronized (this){
            while (progressInfo == null || (
                currentStatus != EnumProgressStatus.FINISHED &&
                currentStatus != EnumProgressStatus.CANCELED)){

                progressInfo = TaskProgressService.getInstance().getProgressInfo(customPID);
                if (progressInfo == null){
                    log("Progress have not been created.");
                    try {
                        this.wait(ONE_SECOND / 4);
                    } catch (InterruptedException e){
                        log("Must be error! Main thread should't be interrupted");
                    }
                    continue;
                }

                // update
                currentStatus = progressInfo.getStatus();

                log("The current status of that progress: " + progressInfo.getStatus().getStatus());
                log("The current progress value         : " + progressInfo.getValue());
                try {
                    this.wait(ONE_SECOND / 4);
                } catch (InterruptedException e){
                    log("Must be error! Main thread should't be interrupted");
                }
            }
        }

        return currentStatus;
    }


    private static class ProgressWithThread implements Runnable{

        private IProgressHandler progressHandler;

        ProgressWithThread(String progressId){
            this.progressHandler = TaskProgressService.getInstance().createProgressHandler(progressId);
        }

        ProgressWithThread(IProgressHandler progressHandler) {
            this.progressHandler = progressHandler;
        }

        @Override
        public void run() {
            if (progressHandler == null){
                return;
            }

            progressHandler.start();

            int total = 10000;
            int slice = 1000;
            synchronized (this){
                for (int i = 0; i < total; i++){
                    if (i % slice == 0){
                        try {
                            progressHandler.update(i * 100.0 / total);
                            this.wait(ONE_SECOND);
                        } catch (InterruptedException e){
                            log("Thread was interrupted by others");

                            progressHandler.cancel();
                            return;
                        }
                    }
                }
            }
            progressHandler.end();
        }
    }

    private void waitThreadToEnd(Thread job){
        if (job != null){
            try {
                job.join();
            }catch (InterruptedException e){
                System.out.println("");
            }
        }
    }

    private static void log(String message){
        System.out.println(message);
    }
}