package net.cmikavac.autowol.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    private EditText mEditText;

    public CustomTextWatcher(EditText editText) { 
        mEditText = editText;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mEditText.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
