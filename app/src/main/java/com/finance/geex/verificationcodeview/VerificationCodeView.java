package com.finance.geex.verificationcodeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created on 2018/12/6 11:12.
 * 自定义
 * @author Wang
 */
public class VerificationCodeView extends LinearLayout implements TextWatcher, View.OnClickListener{

    private static final int EditViewNum = 6; //默认输入框数量(可改成4位)
    private ArrayList<TextView> mTextViewsList = new ArrayList<>(); //存储EditText对象
    private Context mContext;
    private EditText mEditText;
    private int borderSize = 35; //方格边框大小
    private int borderMargin = 10; //方格间距
    private int textSize = 8; //字体大小
    private int textColor = 0xff; //字体颜色
    private int inputTyte = InputType.TYPE_CLASS_NUMBER;
    private inputEndListener callBack;

    public VerificationCodeView(Context context) {
        super(context);
        init(context);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context,attrs);
        init(context);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context,attrs);
        init(context);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
        borderSize = array.getInteger(R.styleable.VerificationCodeView_borderSize, 35);
        borderMargin = array.getInteger(R.styleable.VerificationCodeView_borderMargin, 13);
        textSize = array.getInteger(R.styleable.VerificationCodeView_textSize, 8);
        textColor = array.getColor(R.styleable.VerificationCodeView_textColor, ContextCompat.getColor(context,R.color.color_FAFAFA));
    }

    /**
     * 获取输入框内容
     */
    public String getText() {
        return mEditText.getText().toString();
    }

    /**
     * 设置输入框中的内容
     * @param text
     */
    public void setText(String text){

        mEditText.setText(text);
        for (int i = 0; i < mTextViewsList.size(); i++) {

            TextView textView = mTextViewsList.get(i);
            textView.setText("");

        }

    }

    public void setOnInputEndCallBack(inputEndListener onInputEndCallBack) {
        callBack = onInputEndCallBack;
    }

    public interface inputEndListener{
        void input(String text);
        void afterTextChanged(String text);
    }

    private void init(Context context) {
        mContext = context;
        initEditText(context);
        //设置间距
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(mContext,borderSize), DensityUtil.dip2px(mContext, (float) (borderSize*1.5))); //设置方格框的宽高
        params.setMargins(DensityUtil.dip2px(mContext,borderMargin),0,0,0);
        //设置文字
        for (int i = 0; i < EditViewNum; i++) {
            TextView textView = new TextView(mContext);
            textView.setBackgroundResource(R.drawable.shape_verfication_code_normal); //设置TextView的背景
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(DensityUtil.sp2px(mContext,textSize));
            textView.getPaint().setFakeBoldText(true);
            textView.setLayoutParams(params);
            textView.setInputType(inputTyte);
            textView.setTextColor(textColor);
            textView.setOnClickListener(this);
            mTextViewsList.add(textView);
            addView(textView);
        }
        //显示隐藏软键盘
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                mEditText.setHintTextColor(Color.parseColor("#ff0000"));
//            }
//        }, 500);
        //监听删除键
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (mEditText.getText().length()>=mTextViewsList.size()) return false;
                    mTextViewsList.get(mEditText.getText().length()).setText("");
                }
                return false;
            }
        });
    }

    private void initEditText(Context context) {
        mEditText = new EditText(context);
        mEditText.setBackgroundColor(Color.parseColor("#00000000"));
        mEditText.setMaxLines(1);
        mEditText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(EditViewNum)});
        mEditText.addTextChangedListener(this);
        mEditText.setTextSize(0);
        addView(mEditText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (callBack!=null) {
            callBack.afterTextChanged(s.toString());
        }
        if (s.length() <= 1) {
            mTextViewsList.get(0).setText(s);
        } else {
            mTextViewsList.get(mEditText.getText().length() - 1).setText(s.subSequence(s.length() - 1, s.length()));
        }
        if (s.length()==EditViewNum) {
            if (callBack!=null) {
                callBack.input(mEditText.getText().toString());
            }
        }
    }

    @Override
    public void onClick(View v) { //TextView点击时获取焦点弹出输入法
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
