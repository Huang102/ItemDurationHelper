package com.kisen.itemdurationhelper.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * description: 正方形ImageView
 * author: KisenHuang
 * date: 2018/3/23 11:13
 * version: 1
 */
class SquareImageView constructor(context: Context, attr: AttributeSet?, arg: Int, arg2: Int) : ImageView(context, attr, arg, arg2) {
    constructor(context: Context, attr: AttributeSet?, arg: Int) : this(context, attr, arg, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}