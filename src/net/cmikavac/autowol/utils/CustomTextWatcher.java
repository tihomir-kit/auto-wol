package net.cmikavac.autowol.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * This class is used to clear the error off of EditText
 * field when the user starts typing in it.
 */
public class CustomTextWatcher implements TextWatcher {
    private EditText mEditText;

    /**
     * Constructor.
     * @param editText      EditText entity for which to set TextWatcher.
     */
    public CustomTextWatcher(EditText editText) { 
        mEditText = editText;
    }

    /* (non-Javadoc)
     * Do nothing before text changed (method needs to be "implemented").
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
     */
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /* (non-Javadoc)
     * Removes error from EditText when the user starts typing into the EditText field.
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mEditText.setError(null);
    }

    /* (non-Javadoc)
     * Do nothing after text changed (method needs to be "implemented").
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged(Editable s) {
    }
}
