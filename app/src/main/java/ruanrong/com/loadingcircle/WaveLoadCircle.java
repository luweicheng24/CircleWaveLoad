package ruanrong.com.loadingcircle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author   : luweicheng on 2017/7/15 0015 11:44
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 自定义实现波浪的加载view
 */

public class WaveLoadCircle extends View {
    private Paint mPaint; //基本画笔
    private Paint textPaint; //文字画笔
    private Path path; //路径
    private int mWidth = DimentionUtils.px2Dp(getContext(), 50); //默认的view的宽度
    private int mHeight = DimentionUtils.px2Dp(getContext(), 50);//默认view的高度
    private int textSize = DimentionUtils.px2Sp(getContext(), 10);//默认文字的大小
    private String content = "卢";//文字内容
    private float curPercent; //波浪线水平移动的速率
    private int color; //文字颜色(默认颜色为红色)
    private float ratio = 0.5f;// 波浪的高度与view的比值，默认0.5

    public WaveLoadCircle(Context context) {
        this(context, null);
    }

    public WaveLoadCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {// 确定值或者match_parent
            mWidth = width;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        }
        setMeasuredDimension(mWidth, mHeight);
        textSize = mWidth / 4;//文字大小为宽度的四分之一
        textPaint.setTextSize(textSize);
    }

    /**
     * 初始化画笔和路径
     *
     * @param attr
     */
    private void init(AttributeSet attr) {
        TypedArray arr = getContext().obtainStyledAttributes(attr, R.styleable.WaveLoadCircle);
        //自定义颜色和文字，默认蓝色
        int c = arr.getColor(R.styleable.WaveLoadCircle_color, Color.BLUE);
        String text = arr.getString(R.styleable.WaveLoadCircle_text);
        if (c != 0) {
            Log.i("tag", "init:color " + c);
            color = c;
        }
        if (text != null) {
            content = text;
            Log.i("tag", "init:text " + text);

        }
        arr.recycle();//回收资源

        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//去除锯齿
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);//填充
        mPaint.setColor(color);

        //初始化文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//反锯齿标志
        textPaint.setColor(color);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setDither(true);

        //闭合的波浪路径
        path = new Path();
        // 波浪水平移动的速率
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(1000);
        anim.setRepeatCount(ValueAnimator.INFINITE); // 无限重复
        anim.setRepeatMode(ValueAnimator.RESTART);//重头再来
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curPercent = animation.getAnimatedFraction();// 将0~1 的动画值不断得赋值当前的进度
                Log.i("tag", "onAnimationUpdate: " + curPercent);
                invalidate();
            }
        });
        anim.start();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        //画底部的文字
        textPaint.setColor(color);
        drawCentertext(canvas, textPaint);
        // 获取path路径为一个上边为贝塞尔曲线的矩形
        path = getWavePath(curPercent);
        Log.i("tag", "onDraw:percent " + curPercent);
        // 在画布上面裁剪该path
        canvas.clipPath(path);
        // 画圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mPaint);
        textPaint.setColor(Color.WHITE);
        // 再画一个颜色与上面字体颜色不一样的字
        drawCentertext(canvas, textPaint);
    }
    //在画板中部画字

    /**
     * 为了将文字画在画布的中央，centerY = (画布高度 - 字体.assent - 字体.descent)/2
     *
     * @param canvas
     * @param paint
     */
    private void drawCentertext(Canvas canvas, Paint paint) {
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics pf = paint.getFontMetrics();
        int centerY = (int) ((mHeight - pf.ascent - pf.descent) / 2);
        canvas.drawText(content, rect.centerX(), centerY, paint);

    }

    /**
     * 绘制一个上边是由4段二阶贝塞尔曲线的矩形，区间位置-mWidth ~ mWidth
     *
     * @param percent
     * @return
     */
    public Path getWavePath(float percent) {
        Path path = new Path();
        float x = -mWidth * percent;
        path.moveTo(x, mHeight * (1 - ratio));
        //控制点的相对宽度
        int qWidth = mWidth / 4;
        //控制点的相对高度
        int qHeight = qWidth / 2;
        //第一个波浪
        path.rQuadTo(qWidth, qHeight, qWidth * 2, 0);
        path.rQuadTo(qWidth, -qHeight, qWidth * 2, 0);
        //第二个波浪
        path.rQuadTo(qWidth, qHeight, qWidth * 2, 0);
        path.rQuadTo(qWidth, -qHeight, qWidth * 2, 0);
        //右侧的直线
        path.lineTo(x + mWidth * 2, mHeight);

        path.lineTo(x, mHeight);
        //自动闭合补出左边的直线
        path.close();
        return path;
    }

    /**
     * 设置波浪的高度与view高度的比值（0f ~ 1f）
     *
     * @param ratio
     */
    public void setWaveHeightRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }
}
