package com.ha.cjy.jyswitchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 滑动开关按钮
 *
 * 支持自定义颜色值setOpenColor/setCloseColor/setCircleColor;
 * 支持设置偏移量setOffset;
 * 支持设置初始状态changeState;
 * 支持获取默认状态getDefaultState;
 * 支持开启/关闭状态的监听setListener(OnSwitchStateChangeListener);
 * 支持设置滑动圆形图标的边距setCirclePadding;
 * 支持设置开启/关闭文本和颜色setOpenText、setCloseText、setOpenTextColor、setCloseTextColor
 * 支持设置文本字体大小setTextSize
 *
 * Created by cjy on 17/11/14.
 */

public class JYSwitchButton extends View {
    private Context mContext;

    //画笔
    private Paint mPaint;
    //移动距离
    private int mCurrentX;
    //宽度
    private int mViewWidth;
    //高度
    private int mViewHeight;
    //Y中心
    private int mCenterY;
    //左边圆的X中心点
    private int mStartX;
    //右边圆的X中心点
    private int mEndX;
    //滑动圆形图标的半径
    private int mRadius;
    //滑动圆形图标的边距
    private int mCirclePadding = 2;
    //是否已经初始化好宽高了
    private boolean mIsInit = false;
    //是否开启，默认是关闭状态
    private boolean mIsOpen = false;
    //状态监听器
    private OnSwitchStateChangeListener mListener;
    //矩形4个角的半径坐标,左上，右上，右下，左下（顺时针）
    private float[] mRadiusArr = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    //偏移量，用来控制view的显示大小
    private int mOffset = 20;
    //开启状态的背景色
    private int mOpenColor = Color.BLUE;
    //关闭状态的背景色
    private int mCloseColor = Color.GRAY;
    //滑动圆形图标的颜色
    private int mCircleColor = Color.LTGRAY;
    //字体最大值、最小值
    private float mTextMaxSize = 32;
    private float mTextMinSize = 10;
    //开启/关闭文本
    private String mOpenText="";
    private String mCloseText="";
    //开启/关闭文本的颜色
    private int mOpenTextColor = Color.WHITE;
    private int mCloseTextColor = Color.WHITE;

    public JYSwitchButton(Context context) {
        this(context, null);
    }

    public JYSwitchButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JYSwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getProperty(context,attrs);
        init();
        defaultRoundRadius();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     */
    private void getProperty(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.switchbutton);
        mOpenColor = typedArray.getInteger(R.styleable.switchbutton_openColor,mOpenColor);
        mCloseColor = typedArray.getInteger(R.styleable.switchbutton_closeColor,mCloseColor);
        mCircleColor = typedArray.getInteger(R.styleable.switchbutton_circleColor,mCircleColor);
        mOpenText = typedArray.getString(R.styleable.switchbutton_openText);
        mCloseText = typedArray.getString(R.styleable.switchbutton_closeText);
        if (mOpenText == null)
            mOpenText = "";
        if (mCloseText == null)
            mCloseText = "";
        mOpenTextColor = typedArray.getInteger(R.styleable.switchbutton_openTextColor,mOpenTextColor);
        mCloseTextColor = typedArray.getInteger(R.styleable.switchbutton_closeTextColor,mCloseTextColor);
        mTextMaxSize = typedArray.getDimension(R.styleable.switchbutton_textSize,mTextMaxSize);

        //取完属性，记得释放
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredWidth() / 2 + mOffset;
        setMeasuredDimension(mViewWidth, mViewHeight);
        mCenterY = mViewHeight / 2 - mOffset;
        mRadius = mViewHeight / 2 - mOffset;
        mCurrentX = mRadius;
        mStartX = mRadius;
        mEndX = mViewWidth - mRadius;

        mIsInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsInit) {
            //控制滑动区域
            mCurrentX = mCurrentX > mStartX ? mCurrentX : mStartX;
            mCurrentX = mCurrentX < mEndX ? mCurrentX : mEndX;

            Path path = new Path();
            RectF rectF = new RectF();
            rectF.top = 0;
            rectF.left = 0;
            rectF.right = mViewWidth;
            rectF.bottom = mRadius * 2;
            //画圆角矩形背景图
            path.addRoundRect(rectF, mRadiusArr, Path.Direction.CW);

            if (mIsOpen) {
                //左边色块
                mPaint.setColor(mOpenColor);
                canvas.drawPath(path, mPaint);
                //绘制文本
                drawText(canvas,mOpenText,mOpenTextColor);
            } else {
                //右边色块
                mPaint.setColor(mCloseColor);
                canvas.drawPath(path, mPaint);
                //绘制文本
                drawText(canvas,mCloseText,mCloseTextColor);
            }
            //滑动圆形
            mPaint.setColor(mCircleColor);
            int realRadius = mRadius-mCirclePadding;
            if (realRadius < mRadius/2 ){
                realRadius = mRadius/2;
            }else if(realRadius > mRadius){
                realRadius = mRadius;
            }
            canvas.drawCircle(mCurrentX, mCenterY,realRadius, mPaint);
        }
    }

    /**
     * 绘制文本
     * @param canvas
     * @param text 文本
     * @param color 文本颜色
     */
    private void drawText(Canvas canvas,String text,int color){
        if(text.isEmpty())
            return;
        mPaint.setColor(color);
        mPaint.setTextSize(mTextMaxSize);
        //文本宽度
        int textWidth = mViewWidth-mRadius*2;
        float trySize = mTextMaxSize;
        //根据文本宽度，字体大小适配
        while (mPaint.measureText(text)<textWidth){
            trySize += 1;
            mPaint.setTextSize(trySize);
        }
        while (mPaint.measureText(text)>textWidth){
            trySize -= 1;
            if (trySize < mTextMinSize){
                trySize = mTextMinSize;
                break;
            }
            mPaint.setTextSize(trySize);
        }
        mPaint.setTextSize(px2sp(mContext,trySize));

        int x = mIsOpen?mStartX+10:mEndX-mRadius-10;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int textHeight = (int)(fontMetrics.descent-fontMetrics.ascent);
        int baseline = (int) (mCenterY+(mCenterY*1.0/3.0));
        canvas.drawText(text,x,baseline,mPaint);
    }

    /**
     * 默认的圆角数据
     */
    private void defaultRoundRadius() {
        mRadiusArr[0] = 120;
        mRadiusArr[1] = 120;
        mRadiusArr[2] = 120;
        mRadiusArr[3] = 120;
        mRadiusArr[4] = 120;
        mRadiusArr[5] = 120;
        mRadiusArr[6] = 120;
        mRadiusArr[7] = 120;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int lastX = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastX = (int) event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mCurrentX = (int) (event.getX() - lastX);
                break;
            }
            case MotionEvent.ACTION_UP: {
                mCurrentX = (int) (event.getX() - lastX);
                if (mCurrentX > mViewWidth / 2) {//从左到右滑动
                    mCurrentX = mEndX;
                    if (mIsOpen == false) {
                        mIsOpen = true;
                        if (mListener != null) {
                            mListener.onSwitchStateChange(mIsOpen);
                        }
                    }
                } else {//从右向左滑动
                    mCurrentX = mStartX;
                    if (mIsOpen == true) {
                        mIsOpen = false;
                        if (mListener != null) {
                            mListener.onSwitchStateChange(mIsOpen);
                        }
                    }
                }
                break;
            }
        }
        postInvalidate();
        return true;
    }

    /**
     * 获取默认状态
     * @return
     */
    public boolean getDefaultState(){
        return this.mIsOpen;
    }

    /**
     * 设置状态：开启/关闭
     * @param isOpen 是否开启 true-开启 false-关闭
     */
    public void changeState(boolean isOpen){
        this.mIsOpen = isOpen;
        postDelayed(new Runnable() {//延迟100毫秒，等计算好宽高再进行重新绘制
            @Override
            public void run() {
                if (mIsInit) {
                    if (mIsOpen) {
                        mCurrentX = mEndX;
                    } else {
                        mCurrentX = mStartX;
                    }
                }
                invalidate();
            }
        },100);

    }

    /**
     * 设置滑动圆形图标的边距
     * @param padding
     */
    public void setCirclePadding(int padding){
        this.mCirclePadding = padding;
    }

    /**
     * 设置开启状态的颜色
     * @param color
     */
    public void setOpenColor(int color){
        this.mOpenColor = color;
    }
    /**
     * 设置关闭状态的颜色
     * @param color
     */
    public void setCloseColor(int color){
        this.mCloseColor = color;
    }
    /**
     * 设置滑动圆形的颜色
     * @param color
     */
    public void setCircleColor(int color){
        this.mCircleColor = color;
    }

    /**
     * 设置开启状态的文本
     * @param value
     */
    public void setOpenText(String value){
        this.mOpenText = value;
    }

    /**
     * 设置关闭状态的文本
     * @param value
     */
    public void setCloseText(String value){
        this.mCloseText = value;
    }

    /**
     * 设置开启状态的文本
     * @param color
     */
    public void setOpenTextColor(int color){
        this.mOpenTextColor = color;
    }

    /**
     * 设置关闭状态的文本颜色
     * @param color
     */
    public void setCloseTextColor(int color){
        this.mCloseTextColor = color;
    }

    /**
     * 设置字体大小
     * @param textSize
     */
    public void setTextSize(int textSize){
        this.mTextMaxSize = textSize;
    }

    /**
     * 设置偏移量
     * @param offset 偏移量
     */
    public void setOffset(int offset){
        this.mOffset = offset;
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void setListener(OnSwitchStateChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param context
     * @param pxValue
     * @return
     */
    private float px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }


    /**
     * 开启/关闭状态的监听
     */
    interface OnSwitchStateChangeListener {
        /**
         * 状态变化的事件
         * @param isOpen true-开启 false-关闭
         */
        void onSwitchStateChange(boolean isOpen);
    }
}
