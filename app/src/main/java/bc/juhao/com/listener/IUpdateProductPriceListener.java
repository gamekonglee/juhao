package bc.juhao.com.listener;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: Jun
 * @date : 2017/6/19 16:29
 * @description :
 */
public  interface IUpdateProductPriceListener {
    void onUpdateProductPriceListener(int position, JSONObject object);
}
