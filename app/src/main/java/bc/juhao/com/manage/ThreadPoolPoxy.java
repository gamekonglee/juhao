package bc.juhao.com.manage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * @time 2016/8/31  18:54
 * @desc 线程池自定义
 */
public class ThreadPoolPoxy {
    //线程池相关的类
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;//核心线程大小
    private int mMaximumPoolSize;//最大线程数
    private long mKeepAliveTime;//  线程保存时间

    public ThreadPoolPoxy(int corePoolSize, int maximumPoolSize, long mkeepAliveTime) {
        this.mCorePoolSize = corePoolSize;
        this.mMaximumPoolSize = maximumPoolSize;
        this.mKeepAliveTime = mkeepAliveTime;
    }


    public void execute(Runnable command) {
        initThreadPoolExecutor();
        mExecutor.execute(command);
    }


    public void submit(Runnable command) {
        initThreadPoolExecutor();
        mExecutor.submit(command);

    }

    /**
     * 移除线程池
     *
     * @param command
     */
    public void remove(Runnable command) {
        initThreadPoolExecutor();
        mExecutor.remove(command);
    }

    /**
     * 创建线程池
     */
    public void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolPoxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    int corePoolSize = mCorePoolSize;
                    int maximumPoolSize = mMaximumPoolSize;
                    long keepAliveTime = mKeepAliveTime;
                    TimeUnit unit = TimeUnit.NANOSECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                    mExecutor = new ThreadPoolExecutor(
                            corePoolSize,//核心线程数
                            maximumPoolSize,//最大线程数
                            keepAliveTime,//线程保存时间
                            unit,//保存时间的单位
                            workQueue,//任务队列
                            threadFactory,//线程工厂
                            handler// 异常处理的handler
                    );

                }

            }
        }
    }

}
