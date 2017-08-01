package ruanrong.com.loadingcircle;

import android.content.Context;

/**
 * Author   : luweicheng on 2017/7/16 0016 13:32
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: px 与 dp 换算工具
 */

public class DimentionUtils {
    //  dp = dip  密度无关像素点  density(密度) = dpi（像素密度do per inch）/160
    //   dp = px/density   (px与dp之间的转换)
    //  density =  context.getResource().getDisplayMetrics.density;

    /**
     * @param context
     * @param px
     * @return
     */
    public static int px2Dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将dp转换成px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将文字的大小sp转换成px
     *
     * @param context
     * @param sp
     * @return
     */
    public static int sp2Px(Context context, int sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 将文字大小px转换成sp
     *
     * @param context
     * @param px
     * @return
     */

    public static int px2Sp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity; //文字的像素密度
        return (int) (px / scale + 0.5f);
    }

}
