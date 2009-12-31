package com.dazhi.newsdemo2.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazhi.newsdemo2.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2010/1/1.
 */
public class HeadListVitew extends ListView implements AbsListView.OnScrollListener {
    private View headerView;
    private int headerHeight;
    private int firstVisibleItem;
    boolean isRemark = false;
    int startY;

    //定义状态
    int state;//当前状态
    final int NONE = 0; //正常状态
    final int PULL = 1;//提示下拉状态
    final int RELESE = 2;//提示释放状态
    final int REFALUSHING = 3;//刷新状态

    int scrollState;

    ReflashListener mReflashListener;//接口


    public HeadListVitew(Context context) {
        super(context);
        initView(context);
    }

    public HeadListVitew(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeadListVitew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化headView
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        headerView = inflater.inflate(R.layout.head_listview, null);
        measureView(headerView);//设置下拉菜单的高度

        headerHeight = headerView.getMeasuredHeight();

        topPadding(-headerHeight);//设置顶部下拉菜单隐藏

        this.addHeaderView(headerView);

        this.setOnScrollListener(this);  // 设置滚动监听
    }

    /**
     * 通知父布局
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int height;
        int tempHeight = lp.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        }
        view.measure(width, height);

    }

    //设置顶部下拉菜单隐藏
    private void topPadding(int headerHeight) {
        headerView.setPadding(headerView.getPaddingLeft(), headerHeight,
                headerView.getPaddingRight(), headerView.getPaddingBottom());
        headerView.invalidate();  // 重新绘制View树
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFALUSHING;
                    //加载最新数据
                    reflashViewByState();
//                    new HomeActivity().onReflush();
//                    mReflashListener.onReflush();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            default:
                break;

        }
        return super.onTouchEvent(ev);


    }

    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;

        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }

                break;
            case RELESE:
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }

                break;
            case REFALUSHING:

                break;
        }

    }

    private void reflashViewByState() {
        TextView tip = (TextView) headerView.findViewById(R.id.tv_flush);
        ImageView arrow = (ImageView) headerView.findViewById(R.id.iv_flush);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);

        //自定义一个下拉的动画
        RotateAnimation animation = new RotateAnimation(0,180,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true); // 动画完成后出现

        RotateAnimation animation1 = new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);

        switch (state) {
            case NONE:
                arrow.clearAnimation();
                break;
            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉可以刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation1);
                break;

            case RELESE:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("松开可以刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation);
                break;
            case REFALUSHING:
                topPadding(50);
                arrow.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                break;

        }
    }

    public void reflashCompleted(){
        state = NONE;
        isRemark = false;
        reflashViewByState();

        TextView lastReflashTime = (TextView) findViewById(R.id.tv_lastupdate_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        lastReflashTime.setText(time);
    }



    //定义一个刷新数据的接口
    public interface ReflashListener{
        public void onReflush();
    }

    public void setInterface(ReflashListener reflashListener){
        this.mReflashListener = reflashListener;
    }
}
