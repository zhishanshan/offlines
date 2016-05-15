package com.zhidao.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-3-13
 * TODO
 */

public class DeletableEditText extends EditText{
	private Drawable mRightDrawable;
    private boolean isHasFocus;
     
    public DeletableEditText(Context context) {
        super(context);
        init();
    }
    public DeletableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public DeletableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
     
    private void init(){
        //getCompoundDrawables:
        //Returns drawables for the left, top, right, and bottom borders.
        Drawable [] drawables=this.getCompoundDrawables();
         
        //取得right位置的Drawable
        //即我们在布局文件中设置的android:drawableRight
        mRightDrawable=drawables[2];    
        
        //设置焦点变化的监�?
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        //设置EditText文字变化的监�?
        this.addTextChangedListener(new TextWatcherImpl());
        //初始化时让右边clean图标不可�?
        setClearDrawableVisible(false);
    }
     
     
    /**
     * 当手指抬起的位置在clean的图标的区域
     * 我们将此视为进行清除操作
     * getWidth():得到控件的宽�?
     * event.getX():抬起时的坐标(改坐标是相对于控件本身�?�言�?)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距�?
     * getPaddingRight():clean的图标右边缘至控件右边缘的距�?
     * 于是:
     * getWidth() - getTotalPaddingRight()表示:
     * 控件左边到clean的图标左边缘的区�?
     * getWidth() - getPaddingRight()表示:
     * 控件左边到clean的图标右边缘的区�?
     * �?以这两�?�之间的区域刚好是clean的图标的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
             
            boolean isClean =(event.getX() > (getWidth() - getTotalPaddingRight()))&&
                             (event.getX() < (getWidth() - getPaddingRight()));
            if (isClean) {
                setText("");
            }
            break;
 
        default:
            break;
        }
        return super.onTouchEvent(event);
    }
     
    private class FocusChangeListenerImpl implements OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
             isHasFocus=hasFocus;
             if (isHasFocus) {
                 boolean isVisible=getText().toString().length()>=1;
                 setClearDrawableVisible(isVisible);
            } else {
                 setClearDrawableVisible(false);
            }
        }
         
    }
     
    //当输入结束后判断是否显示右边clean的图�?
    private class TextWatcherImpl implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
             boolean isVisible=getText().toString().length()>=1;
             setClearDrawableVisible(isVisible);
        }
 
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
             
        }
 
        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
             
        }
         
    }   
     
    //隐藏或�?�显示右边clean的图�?
    protected void setClearDrawableVisible(boolean isVisible) {
        Drawable rightDrawable;
        if (isVisible) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        //使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], 
                             rightDrawable,getCompoundDrawables()[3]);
    } 
 
    // 显示�?个动�?,以提示用户输�?
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
        
    }
 
    //CycleTimes动画重复的次�?
    public Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
