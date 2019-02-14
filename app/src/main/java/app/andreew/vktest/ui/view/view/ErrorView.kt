package app.andreew.vktest.ui.view.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import app.andreew.vktest.R
import kotlinx.android.synthetic.main.error_view.view.*

class ErrorView : LinearLayout {

    interface OnErrorButtonClickListener {
        fun repeat()
    }

    enum class ErrorType {
        PARSING, FAILURE, FATAL
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.error_view, this)
    }

    fun showError(type: ErrorType, callback: OnErrorButtonClickListener?) {
        when (type) {
            ErrorType.PARSING -> {
                textError.text = resources.getString(R.string.error_text_parsing)
                imageError.setImageDrawable(resources.getDrawable(R.drawable.warning))
            }
            ErrorType.FAILURE -> {
                textError.text = resources.getString(R.string.error_text_failure)
                imageError.setImageDrawable(resources.getDrawable(R.drawable.error))
            }
            ErrorType.FATAL -> {
                textError.text = resources.getString(R.string.error_text_fatal)
                imageError.setImageDrawable(resources.getDrawable(R.drawable.error))
            }
        }
        if (callback == null)
            buttonError.visibility = View.GONE
        else
            buttonError.setOnClickListener {
                callback.repeat()
            }
    }
}