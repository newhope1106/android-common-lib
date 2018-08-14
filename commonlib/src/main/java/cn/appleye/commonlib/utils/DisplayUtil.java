package cn.appleye.commonlib.utils;

import android.content.Context;

/**
 * @author newhope1106
 * date 2018/8/6
 * dp和px,sp和px之间的转换
 */
public class DisplayUtil {
    /**
     * 将px换算成dp
     * @param context 上下文
     * @param pxValue 像素
     * @return dp值
     * */
    public static int px2dp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        //下面0.5做四舍五入用
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px
     * @param context 上下文
     * @param dipValue dp
     * @return px值
     * */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转sp
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     * */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     * */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
