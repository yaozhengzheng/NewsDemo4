package com.dazhi.newsdemo2.ui.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazhi.newsdemo2.R;


public class ReFlashListView extends ListView implements OnScrollListener {
    /**
     * 顶部下拉刷新
     */
	View header;// 顶部布局文件；
	int headerHeight;// 顶部布局文件的高度；
	int firstVisibleItem;// 当前第一个可见的item的位置；
	int mScrollState;// listview 当前滚动状态；
	boolean isRemark;// 标记，当前是在listview最顶端摁下的；
	int startY;// 摁下时的Y值；

	int state;// 当前的状态；
	final int NONE = 0;// 正常状态；
	final int PULL = 1;// 提示下拉状态；
	final int RELESE = 2;// 提示释放状态；
	final int REFLASHING = 3;// 刷新状态；
	IReflashListener iReflashListener;//刷新数据的接口
    private LayoutInflater mInflater;


    /**
     * 底部
     *
     */
    private View footerView;
    private  int lastVisibleItem;
    private int totalItemCount;
    private boolean isBootomLoading;
    private FooterReflash mFooterReflash;


    public ReFlashListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ReFlashListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ReFlashListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 初始化界面，添加顶部布局文件到 listview
	 *
	 * @param context
	 */
	private void initView(Context context) {
        //顶部刷新
        mInflater = LayoutInflater.from(context);
		header = mInflater.inflate(R.layout.head_listview, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.i("tag", "headerHeight = " + headerHeight);
		topPadding(-headerHeight);
		this.addHeaderView(header);



        //底部刷新

//        footerView = mInflater.inflate(R.layout.footers_layout,null);
//
//        this.addFooterView(footerView);
//        footerView.setVisibility(View.GONE);
//
//        this.setOnScrollListener(this);


	}

	/**
	 * 通知父布局，占用的宽，高；
	 *
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 设置header 布局 上边距；
	 *
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
        //上面
		this.firstVisibleItem = firstVisibleItem;
        //下面
        lastVisibleItem = firstVisibleItem +visibleItemCount;
        this.totalItemCount = totalItemCount;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.mScrollState = scrollState;
        //下面
//        if (lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE){
//            if (!isBootomLoading){
//                isBootomLoading = true;
//                footerView.setVisibility(View.VISIBLE);
//                //加载内容
//                mFooterReflash.footerReflash();
//
//            }
//        }
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
                Log.d("我的","ACTION_DOWN");
				if (firstVisibleItem == 0) {
					isRemark = true;
					startY = (int) ev.getY();
				}
				break;

			case MotionEvent.ACTION_MOVE:
                Log.d("我的","ACTION_MOVE");
				onMove(ev);
				break;
			case MotionEvent.ACTION_UP:
                Log.d("我的","ACTION_UP");
				if (state == RELESE) {

					state = REFLASHING;
					// 加载最新数据；
					reflashViewByState();
					iReflashListener.onReflash();

				} else if (state == PULL) {
					state = NONE;
					isRemark = false;
					reflashViewByState();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 判断移动过程操作；
	 *
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
//        Log.d("我的", String.valueOf(space));
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
                Log.d("我的","headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL");
				if (space > headerHeight + 200) {
                    Log.d("我的","666666666666666666666666666666");
					state = RELESE;
					reflashViewByState();
				}
				break;
            case RELESE:
                topPadding(topPadding);
                break;

            /**
             * 自己试验的这部分case是多余的啊，源码多余啊，日日日日，好费劲啊
             */
//            case RELESE:
//				topPadding(topPadding);
//				if (space < headerHeight + 30) {
//                    Log.d("我的","space < headerHeight + 30");
//					state = PULL;
//					reflashViewByState();
//				} else if (space <= 0) {
//                    Log.d("我的","space <= 0");
//
//					state = NONE;
//					isRemark = false;
//					reflashViewByState();
//				}else {
//                    Log.d("我的","space >= height+30");
//					state = REFLASHING;
//					reflashViewByState();
//				}
//				break;
		}
	}

	/**
	 * 根据当前状态，改变界面显示；
	 */
	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tv_flush);
		ImageView arrow = (ImageView) header.findViewById(R.id.iv_flush);
		ProgressBar progress = (ProgressBar) header.findViewById(R.id.pb_progressbar);

		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);

		switch (state) {
			case NONE:
				arrow.clearAnimation();
				topPadding(-headerHeight);
				break;

			case PULL:
				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("下拉可以刷新！");
				arrow.clearAnimation();
				arrow.setAnimation(anim1);
				break;
			case RELESE:

				arrow.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				tip.setText("松开可以刷新！");
				arrow.clearAnimation();
				arrow.setAnimation(anim);
				break;
			case REFLASHING:
				topPadding(100);
				arrow.setVisibility(View.GONE);
				progress.setVisibility(View.VISIBLE);
				tip.setText("正在刷新...");
				arrow.clearAnimation();
				break;
		}
	}

	/**
	 * 获取完数据；
	 */
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) header
				.findViewById(R.id.tv_lastupdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText(time);
	}

	public void setInterface(IReflashListener iReflashListener){
		this.iReflashListener = iReflashListener;
	}
	/**
	 * 顶部刷新数据接口
	 * @author Administrator
	 */
	public interface IReflashListener{
		public void onReflash();
	}

    //底部刷新的接口
    public interface FooterReflash{
        public void footerReflash();
    }
    public void setFooterReflash(FooterReflash footerReflash){
        mFooterReflash = footerReflash;
    }

    //底部加载完成
    public void footerLoadCompleted(){
        footerView.setVisibility(View.GONE);
        isBootomLoading = false;
    }




}
