package ru.l4gunner4l.javalearn.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.l4gunner4l.javalearn.R

class AspectRatioImageView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr:Int = 0
) : ImageView(context, attributes, defStyleAttr) {

    companion object{
        private const val DEFAULT_ASPECT_RATIO = 1.78f
    }

    private var aspectRatio = DEFAULT_ASPECT_RATIO

    init {
        if (attributes!=null){
            val a = context.obtainStyledAttributes(attributes, R.styleable.AspectRatioImageView)
            aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredWidth/aspectRatio).toInt()
        setMeasuredDimension(measuredWidth, newHeight)
    }
}