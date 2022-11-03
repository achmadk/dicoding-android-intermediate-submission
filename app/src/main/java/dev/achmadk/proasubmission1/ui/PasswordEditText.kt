package dev.achmadk.proasubmission1.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import dev.achmadk.proasubmission1.R

class PasswordEditText : androidx.appcompat.widget.AppCompatEditText {
    private lateinit var errorImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        errorImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length < 6) {
                    setButtonDrawables(endOfTheText = errorImage)
                } else {
                    setButtonDrawables()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        })
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null) {
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
}