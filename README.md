# JProgress

由Java实现的进度查询服务组件，通过 `TaskProgressService` 工具服务类对外暴露接口，
用户主要通过该类实现与进度查询服务的交互。

适用场景:
1. 简单的单任务进度查询服务
2. 有从属关系的多任务查询服务(一个父任务，同时包含不同进度的子任务，每个任务都有进度值)

常用方法：
1. 异步任务线程通过 TaskProgressService.createProgressHandler 方法构造出一个进度处理器 `IProgressHandler`
2. 在异步任务执行过程中，通过异步任务处理器接口暴露出的各个接口方法完成对任务对应进度状态的更新
3. 客户端查询线程通过 TaskProgressService.getProgressInfo 方法获取进度信息

样例:
1. 服务端对 IProgressHandler 的使用
```
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
```
2. 模拟客户端/浏览器 polling查询进度
```
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
```

更多的使用及测试样例请参考项目下的单元测试 IProgressHandlerTest

注: 项目依赖lombok，这样更优雅