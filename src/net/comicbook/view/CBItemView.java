package net.comicbook.view;

import net.comicbook.R;
import net.comicbook.bean.bmob.Album;
import net.comicbook.utils.Options;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EViewGroup(R.layout.item_cb_cardview)
public class CBItemView extends CardView{

	@ViewById(R.id.cb_image)
	protected ImageView image;
	@ViewById(R.id.cb_title)
	protected TextView title;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	
	private Context context;
	
	public CBItemView(Context context) {
		super(context);
		this.context = context;
		options = Options.getListOptions();
	}
	
	public void setCBItemView(Album album){
		title.setText(album.getName());
		imageLoader.displayImage(album.getCover(), image, options);
	}
}
