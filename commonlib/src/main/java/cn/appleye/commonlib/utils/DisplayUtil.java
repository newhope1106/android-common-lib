package cn.appleye.commonlib.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * @author newhope1106
 * date 2018/8/6
 * dp和px,sp和px之间的转换，以及显示相关的信息
 */
public final class DisplayUtil {
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

    /**
     * 获取显示器宽度
     * @param context 上下文
     * @return 宽度(px)
     * */
    public static int getDisplayWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取显示器宽度
     * @param context 上下文
     * @return 高度(px)
     * */
    public static int getDisplayHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 是否横屏
     * @param context 上下文
     * @return true-横屏
     * */
    public static boolean isLandscape(Context context){
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    /**
     * 是否横屏
     * @param context 上下文
     * @return true-竖屏
     * */
    public static boolean isPotrait(Context context){
        return Configuration.ORIENTATION_PORTRAIT == context.getResources().getConfiguration().orientation;
    }


}
