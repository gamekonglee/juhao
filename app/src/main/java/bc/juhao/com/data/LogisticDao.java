package bc.juhao.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.common.hxp.db.CartSQL;
import com.lib.common.hxp.db.DatabaseManager;
import com.lib.common.hxp.db.LogisticSQL;
import com.lib.common.hxp.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.bean.Logistics;
import bc.juhao.com.cons.Constance;

/**
 * @author: Jun
 * @date : 2017/3/2 16:44
 * @description :
 */
public class LogisticDao {
    private final String TAG = LogisticDao.class.getSimpleName();

    public LogisticDao(Context context) {

        SQLHelper helper = new SQLHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 添加(或更新)一条记录
     * @return -1:添加（更新）失败；否则添加成功
     */
    public long replaceOne(Logistics bean) {
        long result = -1;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put(LogisticSQL.PID, bean.getpId());
            values.put(LogisticSQL.NAME, bean.getName());
            values.put(LogisticSQL.ADDRESS, bean.getAddress());
            values.put(LogisticSQL.TEL, bean.getTel());

            if (db.isOpen()) {
                result = db.replace(LogisticSQL.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }


    /**
     * 获取所有记录
     *
     * @return
     */
    public List<Logistics> getAll() {
        Cursor cursor = null;
        List<Logistics> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor = db.query(LogisticSQL.TABLE_NAME, null, null, null, null, null, "id desc");
                while (cursor.moveToNext()) {
                    int Id = cursor.getInt(cursor.getColumnIndex(CartSQL.ID));
                    String address = cursor.getString(cursor.getColumnIndex(LogisticSQL.ADDRESS));
                    String name = cursor.getString(cursor.getColumnIndex(LogisticSQL.NAME));
                    String tel = cursor.getString(cursor.getColumnIndex(LogisticSQL.TEL));
                    Logistics bean = new Logistics();
                    bean.setId(Id);
                    bean.setName(name);
                    bean.setAddress(address);
                    bean.setTel(tel);

                    beans.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }
        return beans;
    }

    /**
     * 删除一个记录
     * @return 删除成功记录数
     */
    public int deleteOne(int id) {
        int result = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                result = db.delete(LogisticSQL.TABLE_NAME, LogisticSQL.ID + "=?", new String[]{String.valueOf(id)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }

    /**
     * 删除所有记录
     *
     * @return 删除成功记录数
     */
    public int deleteAll() {
        int result = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                result = db.delete(LogisticSQL.TABLE_NAME, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }


}
