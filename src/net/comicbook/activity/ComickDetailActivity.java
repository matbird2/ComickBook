package net.comicbook.activity;

import net.comicbook.R;
import net.comicbook.bean.bmob.Album;
import net.comicbook.utils.Options;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_detail)
public class ComickDetailActivity extends BaseActivity{
	
	@ViewById(R.id.image)
	protected ImageView image;
	@ViewById(R.id.back)
	protected ImageView back;
	@ViewById(R.id.watch)
	protected TextView watch;
	@ViewById(R.id.title)
	protected TextView title;
	@ViewById(R.id.uploadtime)
	protected TextView uploadTime;
	@ViewById(R.id.type)
	protected TextView type;
	@ViewById(R.id.descri)
	protected TextView descri;
	@ViewById(R.id.remark)
	protected TextView remark;
	
	private Album album;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	
	private static String[] TYPES;
	static{
		TYPES = new String[]{"经典","评书","神话","民间","革命","武侠","外国","侦探","人物","儿童","现代","其他"};
	}
	
	@AfterInject
	void init(){
		album = (Album) getIntent().getExtras().getSerializable("album");
		options = Options.getListOptions();
	}
	
	@AfterViews
	void initViews(){
		renderView(album);
		loadData();
	}
	
	private void renderView(Album album){
		if(album == null)
			return ;
		if(album.getImgs() != null && album.getImgs().size() > 0){
			watch.setEnabled(true);
		}else{
			watch.setEnabled(false); 
		}
		
		title.setText(album.getName() == null ? "--":album.getName());
		uploadTime.setText(album.getCreatedAt() == null ? "--" : album.getCreatedAt()+" 上传");
		type.setText(album.getType() == null ? "--" : TYPES[album.getType()]);
		descri.setText(album.getDescri() == null ? "--":album.getDescri());
		remark.setText(album.getRemark() == null ? "--" : album.getRemark());
		imageLoader.displayImage(album.getCover(), image, options);
	}
	
	@Background
	void loadData(){
		BmobQuery<Album> query = new BmobQuery<Album>();
		query.getObject(this, album.getObjectId(), new GetListener<Album>() {
			@Override
			public void onFailure(int code, String error) {
				showShortToast("error:"+error);
			}
			@Override
			public void onSuccess(Album data) {
				album = data;
				renderView(album);
			}
		});
	}
	
	@Click(R.id.back)
	void onBack(View v){
		this.finish();
	}
	
	@Click(R.id.watch)
	void onWatch(View v){
		 Bundle bundle = new Bundle();
	     bundle.putSerializable("album", album);
	     Class<?> class1 = WatchComickActivity_.class;
	     openActivity(class1,bundle, 0);
	}
}
