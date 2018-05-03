package bc.juhao.com.bean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import bc.juhao.com.bean.DbGoodsBean;

import bc.juhao.com.bean.DbGoodsBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dbGoodsBeanDaoConfig;

    private final DbGoodsBeanDao dbGoodsBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dbGoodsBeanDaoConfig = daoConfigMap.get(DbGoodsBeanDao.class).clone();
        dbGoodsBeanDaoConfig.initIdentityScope(type);

        dbGoodsBeanDao = new DbGoodsBeanDao(dbGoodsBeanDaoConfig, this);

        registerDao(DbGoodsBean.class, dbGoodsBeanDao);
    }
    
    public void clear() {
        dbGoodsBeanDaoConfig.clearIdentityScope();
    }

    public DbGoodsBeanDao getDbGoodsBeanDao() {
        return dbGoodsBeanDao;
    }

}
