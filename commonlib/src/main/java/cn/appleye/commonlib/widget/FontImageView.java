package cn.appleye.commonlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import cn.appleye.commonlib.R;
import cn.appleye.commonlib.utils.DisplayUtil;
import cn.appleye.commonlib.utils.FontUtil;

/**
 * @author newhope1106
 * @date 2018/8/13
 * 可以显示图片或者字体的控件
 */
public class FontImageView extends AppCompatImageView {
    private int mBackgroundColor = 0xff868686;
    private int mTextColor = 0xffffffff;
    private String mText;
    private int mTextSize = 24;

    private Paint mPaint = new Paint();

    public FontImageView(Context context) {
        this(context, null);
    }

    public FontImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FontImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.FontImageView);
        CharSequence text = typedArray.getText(R.styleable.FontImageView_text);
        if(text != null){
            mText = text.toString();
        }

        mBackgroundColor = typedArray.getColor(R.styleable.FontImageView_backgroundColor, 0xff868686);
        mTextColor = typedArray.getColor(R.styleable.FontImageView_textColor, 0xffffffff);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.FontImageView_textSize, DisplayUtil.sp2px(context, 18f));
        typedArray.recycle();
    }

    /**
     * 获取背景颜色
     * @return 背景颜色
     * */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * 设置背景颜色
     * @param backgroundColor 背景颜色
     * */
    public void setBackgroundColor(int backgroundColor) {
        //super.setBackgroundColor(backgroundColor);
        this.mBackgroundColor = backgroundColor;
    }

    /**
     * 获取字体颜色
     * @return 字体颜色
     * */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 设置背景颜色
     * */
    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    /**
     * 获取文本
     * @return 文本
     * */
    public String getText() {
        return mText;
    }

    /**
     * 设置文本
     * @param text 文本
     * */
    public void setText(String text) {
        this.mText = text;
    }

    /**
     * 获取字体大小
     * @return 字体大小
     * */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 设置字体大小
     * @param textSize 字体大小
     * */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!TextUtils.isEmpty(mText)) {
            int width = getWidth();
            int height = getHeight();

            int circle = Math.min(width, height) / 2;
            mPaint.setColor(mBackgroundColor);
            mPaint.setAntiAlias(true);
            canvas.drawCircle(width/2, height/2, circle, mPaint);

            mPaint.setTextSize(mTextSize);

            float posX = (width - FontUtil.getFontLength(mPaint, mText))/2;
            float poxY = (height - FontUtil.getFontHeight(mPaint))/2 + FontUtil.getFontLeading(mPaint);

            mPaint.setColor(mTextColor);
            canvas.drawText(mText, posX, poxY, mPaint);
        }
    }
}
