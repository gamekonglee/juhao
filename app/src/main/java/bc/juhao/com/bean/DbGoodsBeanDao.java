package bc.juhao.com.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DB_GOODS_BEAN".
*/
public class DbGoodsBeanDao extends AbstractDao<DbGoodsBean, Long> {

    public static final String TABLENAME = "DB_GOODS_BEAN";

    /**
     * Properties of entity DbGoodsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Price = new Property(1, String.class, "price", false, "PRICE");
        public final static Property Original_img = new Property(2, String.class, "original_img", false, "ORIGINAL_IMG");
        public final static Property Current_price = new Property(3, String.class, "current_price", false, "CURRENT_PRICE");
        public final static Property Create_time = new Property(4, String.class, "create_time", false, "CREATE_TIME");
        public final static Property Id = new Property(5, int.class, "id", false, "ID");
        public final static Property G_id = new Property(6, Long.class, "g_id", true, "_id");
    }


    public DbGoodsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DbGoodsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DB_GOODS_BEAN\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"PRICE\" TEXT," + // 1: price
                "\"ORIGINAL_IMG\" TEXT," + // 2: original_img
                "\"CURRENT_PRICE\" TEXT," + // 3: current_price
                "\"CREATE_TIME\" TEXT," + // 4: create_time
                "\"ID\" INTEGER NOT NULL ," + // 5: id
                "\"_id\" INTEGER PRIMARY KEY );"); // 6: g_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DB_GOODS_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DbGoodsBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(2, price);
        }
 
        String original_img = entity.getOriginal_img();
        if (original_img != null) {
            stmt.bindString(3, original_img);
        }
 
        String current_price = entity.getCurrent_price();
        if (current_price != null) {
            stmt.bindString(4, current_price);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(5, create_time);
        }
        stmt.bindLong(6, entity.getId());
 
        Long g_id = entity.getG_id();
        if (g_id != null) {
            stmt.bindLong(7, g_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DbGoodsBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(2, price);
        }
 
        String original_img = entity.getOriginal_img();
        if (original_img != null) {
            stmt.bindString(3, original_img);
        }
 
        String current_price = entity.getCurrent_price();
        if (current_price != null) {
            stmt.bindString(4, current_price);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(5, create_time);
        }
        stmt.bindLong(6, entity.getId());
 
        Long g_id = entity.getG_id();
        if (g_id != null) {
            stmt.bindLong(7, g_id);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6);
    }    

    @Override
    public DbGoodsBean readEntity(Cursor cursor, int offset) {
        DbGoodsBean entity = new DbGoodsBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // price
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // original_img
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // current_price
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // create_time
            cursor.getInt(offset + 5), // id
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // g_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DbGoodsBean entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPrice(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOriginal_img(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCurrent_price(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreate_time(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setId(cursor.getInt(offset + 5));
        entity.setG_id(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DbGoodsBean entity, long rowId) {
        entity.setG_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DbGoodsBean entity) {
        if(entity != null) {
            return entity.getG_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DbGoodsBean entity) {
        return entity.getG_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
