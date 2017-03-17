package cn.ls.thread;

import cn.ls.encoding.EncodingRun;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/3/3.
 */
public class ThreadUtil {


    public ThreadUtil(Runnable runnable) {
        multiThread(runnable);
    }

    public static void multiThread(Runnable runnable) {
        ExecutorService exec = Executors.newCachedThreadPool();
//        ExecutorService exec = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            exec.execute(runnable);
//            exec.shutdown();
        }
    }
}
