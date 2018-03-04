package bc.juhao.com.factory;


import bc.juhao.com.manage.ThreadPoolPoxy;

/**
 * @author Jun
 * @time 2016/8/31  19:00
 * @desc 线程池工厂类
 */
public class ThreadPoolFactory {
    public static ThreadPoolPoxy mNormalThreadPoolPoxy;//普通线程池


    // 用来普通的网络请求或者耗时操作
    public static ThreadPoolPoxy createNormalThreadPoolPoxy(){
        if(mNormalThreadPoolPoxy==null){
            synchronized (ThreadPoolFactory.class){
                if(mNormalThreadPoolPoxy==null){
                    mNormalThreadPoolPoxy=new ThreadPoolPoxy(5,5,30000);
                }
            }
        }

        return mNormalThreadPoolPoxy;
    }

}
