package com.kims.hottopic;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : kims
 * @blog : http://www.kimsblog.me
 * @time : 16/8/26 下午7:04
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnKeyListener {


    private EditText et;

    private ArrayList<ForegroundColorSpan> mColorSpans = new ArrayList<>();
    private ArrayList<String> mTopicList = new ArrayList<>();
    // 正则表达式
    private static final String topicRegex = "#([^#]+?)#";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et_content);
        et.setOnClickListener(this);
        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        et.addTextChangedListener(this);
        et.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_0:
                insertContent("#程序猿#");
                break;
            case R.id.tv_1:
                insertContent("#设计喵#");
                break;
            case R.id.tv_2:
                insertContent("#攻城狮#");
                break;
            case R.id.tv_3:
                insertContent("#单身汪#");
                break;
            case R.id.et_content:
                int selectionStart = et.getSelectionStart();
                int lastPos = 0;
                int size = mTopicList.size();
                for (int i = 0; i < size; i++) {
                    String topic = mTopicList.get(i);
                    lastPos = et.getText().toString().indexOf(topic, lastPos);

                    if (lastPos != -1) {
                        if (selectionStart >= lastPos && selectionStart <= (lastPos + topic.length())) {
                            //在这position 区间就移动光标
                            et.setSelection(lastPos + topic.length());
                        }
                    }
                    lastPos = lastPos + topic.length();
                }
                break;
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (TextUtils.isEmpty(charSequence))return;
        // 查找话题
        String content = charSequence.toString();
        mTopicList.clear();
        mTopicList.addAll(findTopic(charSequence.toString()));

        // 查找到变色
        Editable editable = et.getText();
        for (int i = 0; i < mColorSpans.size(); i++) {
            editable.removeSpan(mColorSpans.get(i));
        }
        mColorSpans.clear();
        //为editable,中的话题加入colorSpan
        int findPos = 0;
        int size = mTopicList.size();
        for (int i = 0; i < size; i++) {//便利话题
            String topic = mTopicList.get(i);
            findPos = content.indexOf(topic, findPos);
            if (findPos != -1) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
                editable.setSpan(colorSpan, findPos, findPos = findPos + topic.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mColorSpans.add(colorSpan);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 删除操作
     *
     * @param v
     * @param keyCode
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {

            int selectionStart = et.getSelectionStart();
            int selectionEnd = et.getSelectionEnd();

            if (selectionStart != selectionEnd) {
                return false;
            }

            Editable editable = et.getText();
            String content = editable.toString();
            int lastPos = 0;
            int size = mTopicList.size();
            for (int i = 0; i < size; i++) {
                String topic = mTopicList.get(i);
                lastPos = content.indexOf(topic, lastPos);
                if (lastPos != -1) {
                    if (selectionStart != 0 && selectionStart >= lastPos && selectionStart <= (lastPos + topic.length())) {
                        //选中话题
                        et.setSelection(lastPos, lastPos + topic.length());
                        return true;
                    }
                }
                lastPos += topic.length();
            }
        }
        return false;
    }

    /**
     * 匹配文本方法
     *
     * @param s
     * @return
     */
    public static ArrayList<String> findTopic(String s) {
        Pattern p = Pattern.compile(topicRegex);
        Matcher m = p.matcher(s);

        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }

        return list;
    }


    public void insertContent(String content) {
        int selectStart = et.getSelectionStart();
        String con = et.getText().toString();
        String firstStr = con.substring(0, selectStart);
        String secondStr = con.substring(selectStart, con.length());

        et.setText(firstStr + content + secondStr);
        et.setSelection(selectStart + content.length());
    }

}
