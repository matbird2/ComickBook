package net.comicbook.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.comicbook.R;
import net.comicbook.activity.BaseActivity;
import net.comicbook.activity.WatchComickActivity_;
import net.comicbook.adapter.CBAdapter;
import net.comicbook.adapter.CardsAnimationAdapter;
import net.comicbook.bean.bmob.Album;
import net.comicbook.initview.InitView;
import net.comicbook.utils.LogUtils;
import net.comicbook.utils.StringUtils;
import net.comicbook.wedget.swiptlistview.SwipeListView;
import net.comicbook.wedget.viewimage.SliderTypes.BaseSliderView;
import net.comicbook.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

import com.alibaba.fastjson.JSON;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

@EFragment(R.layout.activity_main)
public class PSFragment extends BaseFragment implements
		SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {

	@ViewById(R.id.swipe_container)
	protected SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	protected SwipeListView mListView;
	@ViewById(R.id.progressBar)
	protected ProgressBar mProgressBar;

	@Bean
	protected CBAdapter cbAdapter;
	protected List<Album> listsModles;
	private int index = 0;
	private boolean isRefresh = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@AfterInject
	protected void init() {
		listsModles = new ArrayList<Album>();
		LogUtils.i("comick:", "AfterInject listsModles size:"+listsModles.size());
	}

	@AfterViews
	protected void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.instance().initSwipeRefreshLayout(swipeLayout);
		InitView.instance().initListView(mListView, getActivity());
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(cbAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		LogUtils.i("comick:", "AfterViews listsModles size:"+listsModles.size());
		cbAdapter.clear();
		loadData(index);

		mListView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPagte++;
				index = index + 10;
				loadData(index);
			}
		});
	}

	private void loadData(int record) {
		if (getMyActivity().hasNetWork()) {
			LogUtils.i("comick:", "from server,load data.");
			loadCBList(record);
		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("PSFragment" + currentPagte);
			if (!StringUtils.isEmpty(result)) {
				LogUtils.i("comick", "cache result:" + result);
				LogUtils.i("comick:", "from cache,load data.");
				getResult(result);
			}
		}
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				currentPagte = 1;
				isRefresh = true;
				LogUtils.i("comick:", "onRefresh,load data.");
				loadData(0);
			}
		}, 2000);
	}

	@ItemClick(R.id.listview)
	protected void onItemClick(int position) {
		
	}

	public void enterDetailActivity(Album album) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("album", album);
		Class<?> class1 = WatchComickActivity_.class;
		((BaseActivity) getActivity()).openActivity(class1, bundle, 0);
	}

	@SuppressWarnings("rawtypes")
	@Background
	void loadCBList(int record) {
		BmobQuery query = new BmobQuery("Album");
		query.setLimit(10);
		query.addWhereEqualTo("type", 1);
		query.order("-createdAt");
		query.addQueryKeys("objectId,name,cover,descri");
		query.setSkip(record);
		query.findObjects(getActivity(), new FindCallback() {
			@Override
			public void onFailure(int code, String error) {
				((BaseActivity) getActivity()).showCustomToast("失败：" + code+ " msg:" + error);
				LogUtils.i("test", "error:" + code + error);
			}

			@Override
			public void onSuccess(JSONArray result) {
				LogUtils.i("test", "json:" + result.toString());
				if (result.length() > 10) {
					mListView.setHasMore(true);
				} else {
					mListView.setHasMore(false);
				}
				getResult(result.toString());
			}
		});
	}

	@UiThread
	public void getResult(String result) {
		getMyActivity().setCacheStr("PSFragment" + currentPagte, result);
		if (isRefresh) {
			isRefresh = false;
			cbAdapter.clear();
			listsModles.clear();
		}
		mProgressBar.setVisibility(View.GONE);
		swipeLayout.setRefreshing(false);
		List<Album> list = JSON.parseArray(result, Album.class);
		LogUtils.i("comick", "list size:" + list.size());
		cbAdapter.appendList(list);
		listsModles.addAll(list);
		mListView.onBottomComplete();
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
	}

	@Override
	public void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("MainScreen");
	}

}
