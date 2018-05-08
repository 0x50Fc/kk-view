package cn.kkmofang.view;

import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.util.Map;

import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.V;

/**
 * Created by hailong11 on 2018/4/23.
 */

public class InputElement extends ViewElement {

    public InputElement() {
        super();
        set("#view", EditText.class.getName());
    }

    protected void onSetProperty(View view, String key, String value) {
        super.onSetProperty(view,key,value);
        if(view instanceof EditText) {
            EditText v = (EditText) view;

            if("value".equals(key)) {
                v.setText(value);
            } else if("placeholder".equals(key)) {
                v.setText(value);
            } else if("placeholder-color".equals(key)) {
                v.setHintTextColor(Color.valueOf(value,0xff999999));
            } else if("color".equals(key)) {
                v.setTextColor(Color.valueOf(value,0xff000000));
            } else if("autofocus".equals(key)) {
                if(V.booleanValue(value,false)) {
                    v.requestFocus();
                }
            } else if("text-align".equals(key)) {
                if("right".equals(value)) {
                    v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                } else if("center".equals(value)) {
                    v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                } else {
                    v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
            } else if("font".equals(key)) {
                Paint paint = new Paint();
                Font.valueOf(value,paint);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX,paint.getTextSize());
                v.setTypeface(paint.getTypeface());
            } else if("type".equals(key)) {
                if("password".equals(value)) {
                    v.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                } else if("email".equals(value)) {
                    v.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else if("phone".equals(value)) {
                    v.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                } else if("number".equals(value)) {
                    v.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                } else {
                    v.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                }
            }
        }
    }

    private TextWatcher _textWatcher ;

    public void setView(View view) {
        View v = view();
        if(v != null && v instanceof EditText) {
            EditText text = (EditText) v;
            text.setOnFocusChangeListener(null);
            if(_textWatcher != null) {
                text.removeTextChangedListener(_textWatcher);
            }
        }
        super.setView(view);
        if(view instanceof EditText) {
            EditText text = (EditText) view;

            text.setFocusable(true);
            text.setFocusableInTouchMode(true);
            text.setImeOptions(EditorInfo.IME_ACTION_DONE);
            text.setPadding(0,0,0,0);
            text.setBackground(null);

            final WeakReference<InputElement> e = new WeakReference<>(this);

            text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    InputElement element = e.get();

                    if(element != null) {

                        element.setStatus(hasFocus ? "active" : "");

                        Element.Event event = new Element.Event(element);
                        event.setData(element.data());
                        element.emit(hasFocus ? "focus" :"blur",event);

                    }
                }
            });

            if(_textWatcher == null) {

                _textWatcher = new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        InputElement element = e.get();

                        if(element != null) {


                            Element.Event event = new Element.Event(element);

                            Map<String,Object> data = element.data();

                            data.put("value",s);

                            event.setData(data);

                            element.emit("change",event);

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };

            }

            text.addTextChangedListener(_textWatcher);


        }
    }
}
