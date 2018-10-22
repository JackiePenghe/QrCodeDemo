package com.jackiepenghe.qrcodedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 流式布局的容器
 *
 * @author pengh
 * @date 2017/7/8
 */
public class FlowLayout extends ViewGroup {

    /**
     * 记录每一行的子控件的集合
     */
    private ArrayList<ArrayList<View>> viewLineList;
    /**
     * j记录每一行的高度的集合
     */
    private ArrayList<Integer> lineHeightList;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public FlowLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性集合
     */
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造器
     *
     * @param context      上下文
     * @param attrs        属性集合
     * @param defStyleAttr 默认的属性
     */
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewLineList = new ArrayList<>();
        lineHeightList = new ArrayList<>();
    }

    /**
     * 计算宽高
     *
     * @param widthMeasureSpec  宽度的规格
     * @param heightMeasureSpec 高度的规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取容器给予的建议值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //用于记录测量之后，子控件的宽度
        int measuredWidth = 0;
        //用于记录测量之后，子控件的高度
        int measuredHeight = 0;
        //用于记录当前行的宽度
        int currentLineWidth = 0;
        //用于记录当前行的高度
        int currentLineHeight = 0;
        //这种情况下我们直接使用建议值作为测量值
        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
            measuredHeight = heightSize;
        } else { //通过计算得到测量值
            //获取子控件总数
            int childCount = getChildCount();
            //用于保存当前行的所有子控件的集合
            @SuppressLint("DrawAllocation") ArrayList<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //测量子控件的宽高
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                //取出子控件的margin属性
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //计算子控件的宽度
                int childViewWidth = childView.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + childView.getPaddingLeft() + childView.getPaddingRight();
                //计算子控件的高度
                int childViewHeight = childView.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + childView.getPaddingTop() + childView.getPaddingBottom();
                // 如果当前行的宽度大于建议的宽度，就需要换行了
                if (currentLineWidth + childViewWidth > widthSize) {
                    //取当前行的数据中，当前宽度与测量之后计算的宽度的最大的一个
                    measuredWidth = Math.max(currentLineWidth, measuredWidth);
                    //将测量高度累加一次（因为现在多了一行，需要增加一行的高度）
                    measuredHeight += currentLineHeight;
                    //换行之后，将当前行的子控件集合添加到viewLineList
                    viewLineList.add(viewList);
                    //将行高添加到lineHeightList
                    lineHeightList.add(currentLineHeight);

                    //将当前行的宽度和当前行的高度重置为新一行的第一个子控件的宽度与高度
                    currentLineWidth = childViewWidth;
                    currentLineHeight = childViewHeight;

                    //将用于保存当前行的所有子控件的集合重新创建（不能清空，会导致之前的数据全部丢失），并添加新一行的第一个子控件
                    viewList = new ArrayList<>();
                    viewList.add(childView);
                } else {//如果不超过建议值，记录当前行的一些数据
                    // 累加当前的行宽度
                    currentLineWidth += childViewWidth;
                    //取每行的最大高度
                    currentLineHeight = Math.max(childViewHeight, currentLineHeight);
                    //将当前行的子控件添加到用于记录当前行子控件的集合
                    viewList.add(childView);
                }

                //如果正好是最后一行需要换行
                if (i == childCount -1){
                    //取当前行的数据中，当前宽度与测量之后计算的宽度的最大的一个
                    measuredWidth = Math.max(currentLineWidth, measuredWidth);
                    //将测量高度累加一次（因为现在多了一行，需要增加一行的高度）
                    measuredHeight += currentLineHeight;
                    //换行之后，将当前行的子控件集合添加到viewLineList
                    viewLineList.add(viewList);
                    //将行高添加到lineHeightList
                    lineHeightList.add(currentLineHeight);
                }
            }
        }
        //设置宽高
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 取出布局中的margin属性
     *
     * @param attrs 属性
     * @return LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 摆放子控件的位置
     *
     * @param changed 位置是否被改变了
     * @param l       当前容器的左边的坐标
     * @param t       当前容器的顶部的坐标
     * @param r       当前容器的右边的坐标
     * @param b       当前容器的底部的坐标
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取总行数
        int lineCount = lineHeightList.size();
        int left, top, right, bottom;   //子控件需要摆放的坐标
        int currentTop, currentLeft; //记录当前需要摆放的起始位置
        currentTop = currentLeft = 0;

        for (int i = 0; i < lineCount; i++) {
            //获取当前行的子控件集合
            ArrayList<View> views = viewLineList.get(i);
            //摆放当前行的所有子控件
            int childViewCount = views.size();
            for (int j = 0; j < childViewCount; j++) {
                //获取子控件
                View childView = views.get(j);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //子控件的左边的坐标
                left = currentLeft + marginLayoutParams.leftMargin + childView.getPaddingLeft();
                //子控件的顶部的坐标
                top = currentTop +marginLayoutParams.topMargin + childView.getPaddingTop();
                //子控件的右边的坐标
                right = left + childView.getMeasuredWidth() + childView.getPaddingRight();
                //子控件底部的坐标
                bottom = top + childView.getMeasuredHeight() + childView.getPaddingBottom();
                //摆放子控件
                childView.layout(left, top, right, bottom);
                //将起始坐标currentLeft更新一次
                currentLeft = right + marginLayoutParams.rightMargin;
            }
            //在摆放完一排之后，更新起始坐标currentTop
            currentTop += lineHeightList.get(i);
            //同时，将currentLeft重置为0
            currentLeft = 0;
        }
        //因为测量
        viewLineList.clear();
        lineHeightList.clear();
    }

    public interface OnItemClickListener{
        /**
         * 子控件被点击时进行的回调
         * @param view 子控件
         * @param position 子控件在当前布局中的位置
         */
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            childView.setClickable(true);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(childView, finalI);
                    }
                }
            });
        }
    }
}
