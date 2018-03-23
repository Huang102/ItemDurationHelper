package com.kisen.lovers.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.*
import android.view.View

/**
 * description: ItemDivider 代理
 * author: KisenHuang
 * date: 2018/3/20 15:06
 * version: 1
 */
class ItemDividerHelper {

    //Divider代理
    private var mDividerDelegate: IDividerDelegate? = null
    //列表方向
    private var mOrientation = OrientationHelper.VERTICAL

    //不添加divider的itemType
    private var mListSpecialType = emptyArray<Int>()

    /**
     * RecyclerView内部边距
     */
    private var mGroupPaddingLeft = 0
    private var mGroupPaddingTop = 0
    private var mGroupPaddingRight = 0
    private var mGroupPaddingBottom = 0

    /**
     * Divider的外间距
     */
    private var mDividerMarginLeft = 0
    private var mDividerMarginTop = 0
    private var mDividerMarginRight = 0
    private var mDividerMarginBottom = 0

    /**
     * divider 宽高
     */
    private var mItemSpaceWidth = 5
    private var mItemSpaceHeight = 5

    /**
     * divider背景色
     */
    private var mDivider: Drawable? = null

    /**
     * group padding 部分是否添加divider
     */
    private var drawGroupPadding = false

    init {
        mDivider = ColorDrawable(Color.parseColor("#ff0000"))
    }

    /**
     * 获取对应布局类型的代理
     */
    fun getDelegate(view: RecyclerView?): IDividerDelegate? {
        val layoutManager = view?.layoutManager
        if (layoutManager != null) {
            when (layoutManager) {
                is GridLayoutManager -> {
                    mDividerDelegate = GridDelegate()
                    mOrientation = layoutManager.orientation
                }
                is LinearLayoutManager -> {
                    mDividerDelegate = LinearDelegate()
                    mOrientation = layoutManager.orientation
                }
                is StaggeredGridLayoutManager -> {
                    mDividerDelegate = StaggerDelegate()
                    mOrientation = layoutManager.orientation
                }
            }
        }
        return mDividerDelegate
    }

    fun setDivider(divider: Drawable) {
        mDivider = divider
    }

    fun setItemSpace(width: Int, height: Int) {
        mItemSpaceWidth = width
        mItemSpaceHeight = height
    }

    fun setGroupPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mGroupPaddingLeft = left
        mGroupPaddingTop = top
        mGroupPaddingRight = right
        mGroupPaddingBottom = bottom
    }

    /**
     * Divider 代理
     */
    interface IDividerDelegate {
        fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?)
        fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?)
        fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?)
    }

    /**
     * LinearLayoutManager对应Item代理
     */
    inner class LinearDelegate : IDividerDelegate {
        override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        }

        override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
            when (mOrientation) {
                OrientationHelper.VERTICAL -> {
                    drawVer(c, parent)
                }
                OrientationHelper.HORIZONTAL -> {
                    drawHor(c, parent)
                }
            }
        }

        private fun drawVer(c: Canvas?, parent: RecyclerView?) {
            val left = parent?.paddingLeft ?: 0 + mDividerMarginLeft
            val right = parent?.width ?: 0 - parent?.paddingRight!! - mDividerMarginRight

            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount - 1) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + mItemSpaceWidth
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        private fun drawHor(c: Canvas?, parent: RecyclerView?) {
            val top = parent?.paddingTop ?: 0 + mDividerMarginTop
            val bottom = parent?.height ?: 0 - parent?.paddingBottom!! - mDividerMarginBottom

            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount - 1) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType ?: 0
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val left = child.right + params.rightMargin
                val right = left + mItemSpaceWidth
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            val space = mItemSpaceWidth
            when (mOrientation) {
                OrientationHelper.VERTICAL -> {
                    outRect?.set(mGroupPaddingLeft,
                            if (isFirstItem(view, parent)) mGroupPaddingTop else 0,
                            mGroupPaddingRight,
                            if (isLastItem(view, parent)) mGroupPaddingBottom else if (needDivider(view, parent)) space else 0)
                }
                OrientationHelper.HORIZONTAL -> {
                    outRect?.set(if (isFirstItem(view, parent)) mGroupPaddingLeft else 0,
                            mGroupPaddingTop,
                            if (isLastItem(view, parent)) mGroupPaddingRight else if (needDivider(view, parent)) space else 0,
                            mGroupPaddingBottom)
                }
            }
        }

        private fun isLastItem(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view)
            val itemCount = parent?.adapter?.itemCount ?: 0
            return itemCount > 0 && itemPosition == itemCount - 1
        }

        private fun isFirstItem(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view)
            val itemCount = parent?.adapter?.itemCount ?: 0
            return itemCount > 0 && itemPosition == 0
        }

        private fun needDivider(view: View?, parent: RecyclerView?): Boolean {
            val itemCount = parent?.adapter?.itemCount ?: 0
            if (itemCount < 1) return false
            //如果是特殊类型 不需要
            val itemType = parent?.getChildViewHolder(view)?.itemViewType ?: 0
            return !mListSpecialType.contains(itemType)
        }
    }

    /**
     * 出现填充不满，原因是容器分配给每个item的大小相同，
     * 但是使用的间隔不同导致出现大小不同
     */
    inner class GridDelegate : IDividerDelegate {

        override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
            drawLeft(c, parent)
            drawTop(c, parent)
            drawRight(c, parent)
            drawBottom(c, parent)
        }

        private fun drawLeft(c: Canvas?, parent: RecyclerView?) {
            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                if (!drawGroupPadding || (!isFirstColumnOfVer(child, parent) && !isFirstColumnOfHor(child, parent)))
                    continue
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val left = child.left - params.leftMargin - mGroupPaddingTop
                val top = child.top - params.topMargin
                val right = left + mItemSpaceWidth
                val bottom = child.bottom + params.bottomMargin + mItemSpaceHeight
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        private fun drawTop(c: Canvas?, parent: RecyclerView?) {
            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                if (!drawGroupPadding || (!isFirstRowOfVer(child, parent) && !isFirstRowOfHor(child, parent)))
                    continue
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val left = child.left - params.leftMargin - mGroupPaddingLeft
                val top = child.top - params.topMargin - mGroupPaddingTop
                val right = child.right + mItemSpaceWidth
                val bottom = top + mItemSpaceHeight
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        private fun drawRight(c: Canvas?, parent: RecyclerView?) {
            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                if (!drawGroupPadding && (isLastColumnOfVer(child, parent) || isLastColumnOfHor(child, parent)))
                    continue
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val left = child.right + params.rightMargin
                val top = child.top - params.topMargin
                val right = left + mItemSpaceWidth
                val bottom = child.bottom + params.bottomMargin
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        private fun drawBottom(c: Canvas?, parent: RecyclerView?) {
            val childCount = parent?.childCount ?: 0
            for (i in 0 until childCount) {
                val child = parent?.getChildAt(i)
                val viewType = parent?.getChildViewHolder(child)?.itemViewType
                if (mListSpecialType.contains(viewType)) {
                    continue
                }
                if (!drawGroupPadding && (isLastRowOfVer(child, parent) || isLastRowOfHor(child, parent)))
                    continue
                val params = child?.layoutParams as RecyclerView.LayoutParams
                val left = child.left + params.leftMargin
                val top = child.bottom + params.bottomMargin

                val right = child.right + params.rightMargin +
                        if (!drawGroupPadding && (isLastColumnOfVer(child, parent) || isLastColumnOfHor(child, parent))) 0
                        else mItemSpaceWidth
                val bottom = top + mItemSpaceHeight
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

        override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        }

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            when (mOrientation) {
                OrientationHelper.VERTICAL -> {
                    val left = if (isFirstColumnOfVer(view, parent)) mGroupPaddingLeft else 0
                    val top = if (isFirstRowOfVer(view, parent)) mGroupPaddingTop else 0
                    val right = if (isLastColumnOfVer(view, parent)) mGroupPaddingRight else mItemSpaceWidth
                    val bottom = if (isLastRowOfVer(view, parent)) mGroupPaddingBottom else mItemSpaceHeight
                    changeViewPadding(left, view, parent)
                    outRect?.set(left,
                            top,
                            right,
                            bottom)
                }
                OrientationHelper.HORIZONTAL -> {
                    outRect?.set(if (isFirstColumnOfHor(view, parent)) mGroupPaddingLeft else 0,
                            if (isFirstRowOfHor(view, parent)) mGroupPaddingTop else 0,
                            if (isLastColumnOfHor(view, parent)) mGroupPaddingRight else mItemSpaceWidth,
                            if (isLastRowOfHor(view, parent)) mGroupPaddingBottom else mItemSpaceHeight)
                }
            }
        }

        private fun changeViewPadding(left: Int, view: View?, parent: RecyclerView?) {
            if (left == 0)
                return
            if (isFirstColumnOfVer(view, parent)) {
                val tag = view?.tag
                if (tag == null) {
                    view?.tag = left
                    parent?.layoutManager?.layoutDecoratedWithMargins(view, left, 0, 0, 0)
                }
            }
        }

        /**
         * 纵向列表判断是否是第一行
         */
        private fun isFirstRowOfVer(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view) ?: 0
            if (parent?.layoutManager is GridLayoutManager) {
                val gridManager = parent.layoutManager as GridLayoutManager
                val spanCount = gridManager.spanCount
                return itemPosition < spanCount
            }
            return false
        }

        /**
         * 纵向列表判断是否是最后一行
         */
        private fun isLastRowOfVer(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view) ?: 0
            if (parent?.layoutManager is GridLayoutManager) {
                val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
                return itemPosition / spanCount == (parent.adapter.itemCount - 1) / spanCount
            }
            return false
        }

        /**
         * 纵向列表判断是否是第一列
         */
        private fun isFirstColumnOfVer(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view) ?: 0
            if (parent?.layoutManager is GridLayoutManager) {
                val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
                return itemPosition % spanCount == 0
            }
            return false
        }

        /**
         * 纵向列表判断是否是最后一列
         */
        private fun isLastColumnOfVer(view: View?, parent: RecyclerView?): Boolean {
            val itemPosition = parent?.getChildAdapterPosition(view) ?: 0
            if (parent?.layoutManager is GridLayoutManager) {
                val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
                return (itemPosition + 1) % spanCount == 0
            }
            return false
        }

        /**
         * 横向列表判断是否是第一行
         */
        private fun isFirstRowOfHor(view: View?, parent: RecyclerView?): Boolean {
            return false
        }

        /**
         * 横向列表判断是否是最后一行
         */
        private fun isLastRowOfHor(view: View?, parent: RecyclerView?): Boolean {
            return false
        }

        /**
         * 横向列表判断是否是最后一行
         */
        private fun isFirstColumnOfHor(view: View?, parent: RecyclerView?): Boolean {
            return false
        }

        /**
         * 横向列表判断是否是最后一列
         */
        private fun isLastColumnOfHor(view: View?, parent: RecyclerView?): Boolean {
            return false
        }

    }


    inner class StaggerDelegate : IDividerDelegate {

        override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {

        }

        override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        }

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        }
    }

}
