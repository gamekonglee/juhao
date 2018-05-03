package bc.juhao.com.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.bean.ContactInfo;

/**
 * Created by gamekonglee on 2018/3/29.
 */

public class ContactUtils {/**
 * 获取联系人数据
 *
 * @param context
 * @return
 */
public static List<ContactInfo> getAllContacts(Context context) {
    List<ContactInfo> list = new ArrayList<ContactInfo>();
    // 获取解析者
    ContentResolver resolver = context.getContentResolver();
    // 访问地址
    Uri raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
    Uri data = Uri.parse("content://com.android.contacts/data");
    // 查询语句
    // select contact_id from raw_contacts;//1 2 3 4
    // select mimetype,data1 from view_data where raw_contact_id=3;
    // Cursor cursor=resolver.query(访问地址, 返回字段 null代表全部, where 语句, 参数, 排序)
    Cursor cursor = resolver.query(raw_contacts, new String[] { "contact_id" }, null, null, null);

    while (cursor.moveToNext()) {
        // getColumnIndex根据名称查列号
        String id = cursor.getString(cursor.getColumnIndex("contact_id"));
        // 创建实例
        ContactInfo info = new ContactInfo();
        info.id = id;
        Cursor item = resolver.query(data, new String[] { "mimetype", "data1" }, "raw_contact_id=?", new String[] { id }, null);

        while (item.moveToNext()) {
            String mimetype = item.getString(item.getColumnIndex("mimetype"));
            String data1 = item.getString(item.getColumnIndex("data1"));
            if ("vnd.android.cursor.item/name".equals(mimetype)) {
                info.name = data1;
            } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                info.phone = data1;
            }
        }
        item.close();
        // 添加集合
        list.add(info);
    }
    cursor.close();
    return list;
}
    /**
     * 获取联系人
     *
     * @param context
     * @return
     */
    public static List<ContactInfo> getContactsInfos(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        // 获取联系人数据 访问联系人的内容提供者
        // ContactsContract.AUTHORITY com.android.contacts 授权
        // 该内容提供者操作是需要读写权限
        // matcher.addURI(ContactsContract.AUTHORITY, "raw_contacts",
        // RAW_CONTACTS);
        // matcher.addURI(ContactsContract.AUTHORITY, "raw_contacts/#/data",
        // RAW_CONTACTS_DATA);
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor1 = resolver.query(uri, new String[] { "_id" }, null,
                null, null);
        while (cursor1.moveToNext()) {
            int _id = cursor1.getInt(0);
            ContactInfo info = new ContactInfo();
            uri = Uri.parse("content://com.android.contacts/raw_contacts/"
                    + _id + "/data");
            Cursor cursor2 = resolver.query(uri, new String[] { "data1",
                    "mimetype" }, null, null, null);
            while (cursor2.moveToNext()) {
                String data1 = cursor2.getString(0);
                String mimetype = cursor2.getString(1);
                if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {// 号码
                    info.phone = data1;
                } else if ("vnd.android.cursor.item/name".equals(mimetype)) {// 姓名
                    info.name = data1;
                }
            }

            cursor2.close();
            infos.add(info);
        }
        cursor1.close();

        return infos;
    }

}
