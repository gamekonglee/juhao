package bc.juhao.com.utils.phone;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.bean.PhoneInfo;


public class PhoneUtil {

    private static List<PhoneInfo> list;

    /**
     * 获取手机通讯录
     */
    public static List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneInfo phoneInfo = new PhoneInfo(name, clearPhoneNumber(number));
            list.add(phoneInfo);
        }
        cursor.close();
        return list;
    }

    /**
     * 清理手机号码 前缀和空格
     *
     * @param phoneNumber 传入之前的手机号码
     * @return 清理之后的手机号码
     */
    public static String clearPhoneNumber(String phoneNumber) {
        String replace = phoneNumber;
        if (replace.startsWith("+86")) {
            replace = replace.replace("+86", "");
        }
        if (replace.contains(" ")) {
            replace = replace.replaceAll(" ", "");
        }
        return replace;
    }
}
