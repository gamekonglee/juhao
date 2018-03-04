package bc.juhao.com.factory;

import java.util.HashMap;
import java.util.Map;

import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.fragment.OrderFragment;

/**
 * @author Jun
 * @time 2016/8/19  19:41
 * @desc ${TODD}
 */
public class OrderFragmentFactory {
    private static final int FRAGMENT_ALL = 0;//全部
    private static final int FRAGMENT_WAIT_PAY = 1;//待付款
    private static final int FRAGMENT_AL_PAY_MONEY = 2;//已付定金
    private static final int FRAGMENT_WAIT_PAY_RETAINGE = 3;//待付尾款
    private static final int FRAGMENT_AL_PAY = 4;//已付款
    private static final int FRAGMENT_WAIT_SEND_GOODS = 5;//待发货
    private static final int FRAGMENT_WAIT_RECEIPT_GOODS = 6;//待收货
    private static final int FRAGMENT_AL_CANCEL = 7;//已取消
    private static final int FRAGMENT_AL_COMPLETE = 8;//已完成
    private static BaseFragment mfragment;

    private static Map<Integer, BaseFragment> mMapFragment = new HashMap<>();

    /**
     * 获取Fragment
     *
     * @param position
     * @return
     */
    public static BaseFragment getFragment(int position) {
        if (mMapFragment.containsKey(position)) {
            mfragment = mMapFragment.get(position);
            return mfragment;
        }
        switch (position) {
            case FRAGMENT_ALL://全部
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_WAIT_PAY://待付款
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_AL_PAY_MONEY://已付定金
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_WAIT_PAY_RETAINGE://待付尾款
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_AL_PAY://已付款
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_WAIT_SEND_GOODS://待发货
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_WAIT_RECEIPT_GOODS:////待收货
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_AL_CANCEL://已取消
                mfragment = new OrderFragment();
                break;
            case FRAGMENT_AL_COMPLETE://已完成
                mfragment = new OrderFragment();
                break;
        }
        mMapFragment.put(position, mfragment);
        return mfragment;
    }
}
