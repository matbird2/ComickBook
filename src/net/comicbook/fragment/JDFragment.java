package net.comicbook.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.comicbook.R;
import net.comicbook.activity.BaseActivity;
import net.comicbook.activity.ComickDetailActivity;
import net.comicbook.activity.WatchComickActivity_;
import net.comicbook.adapter.CBAdapter;
import net.comicbook.adapter.CardsAnimationAdapter;
import net.comicbook.bean.NewModle;
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
import android.content.Intent;
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
public class JDFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,OnSliderClickListener {

//	protected SliderLayout mDemoSlider;
    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

//    protected HashMap<String, NewModle> newHashMap;
    protected HashMap<String, Album> newHashMap;

    @Bean
    protected CBAdapter newAdapter;
//    protected NewAdapter newAdapter;
//    protected List<NewModle> listsModles;
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
        url_maps = new HashMap<String, String>();

        newHashMap = new HashMap<String, Album>();
        
    }

    @AfterViews
    protected void initView() {
        swipeLayout.setOnRefreshListener(this);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mListView, getActivity());
        /*View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        mListView.addHeaderView(headView);*/
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(newAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        newAdapter.clear();
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
            loadNewList(record);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
            String result = getMyActivity().getCacheStr("JDFragment" + currentPagte);
            if (!StringUtils.isEmpty(result)) {
            	LogUtils.i("test", "cache result:"+result);
                getResult(result);
            }
        }
    }

    private void initSliderLayout(List<Album> newModles) {

        /*if (!isNullString(newModles.get(0).getCover()))
            newHashMap.put(newModles.get(0).getCover(), newModles.get(0));
        if (!isNullString(newModles.get(1).getCover()))
            newHashMap.put(newModles.get(1).getCover(), newModles.get(1));
        if (!isNullString(newModles.get(2).getCover()))
            newHashMap.put(newModles.get(2).getCover(), newModles.get(2));
        if (!isNullString(newModles.get(3).getCover()))
            newHashMap.put(newModles.get(3).getCover(), newModles.get(3));

        if (!isNullString(newModles.get(0).getCover()))
            url_maps.put(newModles.get(0).getName(), newModles.get(0).getCover());
        if (!isNullString(newModles.get(1).getCover()))
            url_maps.put(newModles.get(1).getName(), newModles.get(1).getCover());
        if (!isNullString(newModles.get(2).getCover()))
            url_maps.put(newModles.get(2).getName(), newModles.get(2).getCover());
        if (!isNullString(newModles.get(3).getCover()))
            url_maps.put(newModles.get(3).getName(), newModles.get(3).getCover());

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.setOnSliderClickListener(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name));

            textSliderView.getBundle()
                    .putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        newAdapter.appendList(newModles);*/
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                loadData(0);
                url_maps.clear();
//                mDemoSlider.removeAllSliders();
            }
        }, 2000);
    }

    @ItemClick(R.id.listview)
    protected void onItemClick(int position) {
        Album album = listsModles.get(position);
        if(album != null)
        	enterDetailActivity(album);
    }

    public void enterDetailActivity(Album album) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("album", album);
        Class<?> class1 = ComickDetailActivity.class;
//        ((BaseActivity) getActivity()).openActivity(class1,bundle, 0);
        ((BaseActivity) getActivity()).showCustomToast("click");
        getActivity().startActivity(new Intent(getActivity(), class1));
    }

    @Background
    void loadNewList(int record) {
    	/*String result;
        try {
            result = HttpUtil.getByHttpClient(getActivity(), url);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            } else {
                swipeLayout.setRefreshing(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
    	BmobQuery query = new BmobQuery("Album");
    	query.setLimit(10);
    	query.addWhereEqualTo("type", 0);
    	query.order("-createdAt");
    	query.addQueryKeys("objectId,name,cover,descri");
    	query.setSkip(record);
    	query.findObjects(getActivity(), new FindCallback() {
			@Override
			public void onFailure(int code, String error) {
				((BaseActivity)getActivity()).showCustomToast("失败："+code+" msg:"+error);
				LogUtils.i("test", "error:"+code+error);
			}
			@Override
			public void onSuccess(JSONArray result) {
				LogUtils.i("test", "json:"+result.toString());
				if(result.length() > 10){
					mListView.setHasMore(true);
				}else{
					mListView.setHasMore(false);
				}
				getResult(result.toString());
			}
		});
    }

    @UiThread
    public void getResult(String result) {
        getMyActivity().setCacheStr("JDFragment" + currentPagte, result);
        if (isRefresh) {
            isRefresh = false;
            newAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
        List<Album> list = JSON.parseArray(result, Album.class);
        LogUtils.i("comick", "list size:"+list.size());
        
//        if (index == 0 && list.size() >= 4) {
//            initSliderLayout(list);
//        } else {
            newAdapter.appendList(list);
//        }
        listsModles.addAll(list);
        mListView.onBottomComplete();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        /*NewModle newModle = newHashMap.get(slider.getUrl());
        enterDetailActivity(newModle);*/
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("MainScreen"); // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("MainScreen");
    }

}
