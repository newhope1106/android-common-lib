package cn.appleye.commonlib.utils;

import android.graphics.Paint;

/**
 * @author newhope1106
 * date 2018/8/13
 * 字体工具类
 */
public class FontUtil {
    /**
     * @param paint paint
     * @param str 文本
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontLength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @param paint paint
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
    /**
     * @param paint paint
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading- fm.ascent;
    }
}
