package bc.juhao.com.utils;

import android.app.Activity;

import bc.juhao.com.bean.DaoMaster;
import bc.juhao.com.bean.DaoSession;

/**
 * Created by gamekonglee on 2018/4/9.
 */

public class DbHelper {
    public  static DaoSession getSession(Activity context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "my-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }
}
