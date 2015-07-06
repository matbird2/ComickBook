package net.comicbook.activity;

import net.comicbook.R;
import net.comicbook.bean.bmob.Album;
import net.comicbook.utils.LogUtils;
import net.comicbook.utils.Options;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import fi.harism.curl.CurlPage;
import fi.harism.curl.CurlView;

@EActivity(R.layout.activity_watch)
public class WatchComickActivity extends BaseActivity{

	@ViewById(R.id.curl)
	protected CurlView mCurlView;
	private Album album;
	private ImageLoader imageLoader;
	protected DisplayImageOptions options;
	
	@AfterInject
	void init(){
		album = (Album) getIntent().getExtras().getSerializable("album");
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();
	}
	
	@AfterViews
	void initViews(){
		setSlideable(false);
		
		int index = 0;
		if (getLastNonConfigurationInstance() != null) {
			index = (Integer) getLastNonConfigurationInstance();
		}
		mCurlView.setPageProvider(new PageProvider());
		mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		mCurlView.setCurrentIndex(index);
		mCurlView.setBackgroundColor(0xFF202830);
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return mCurlView.getCurrentIndex();
	}
	
	/*@Background
	public void loadImage(int width, int height,int index){
		ImageSize targetImageSize = new ImageSize(width, height);
		Bitmap bitmap = imageLoader.loadImageSync(album.getImgs().get(index), targetImageSize);
	}*/
	
	/**
	 * Bitmap provider.
	 */
	private class PageProvider implements CurlView.PageProvider {

		private int[] mBitmapIds = { R.drawable.base_article_bigimage, R.drawable.default_round_head };

		public int getPageCount() {
			return album.getImgs().size();
		}

		private Bitmap loadBitmap(int width, int height, int index) {
			Bitmap b = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			b.eraseColor(0xFFFFFFFF);
			Canvas c = new Canvas(b);
			Drawable d = getResources().getDrawable(mBitmapIds[index]);

			int margin = 7;
			int border = 3;
			Rect r = new Rect(margin, margin, width - margin, height - margin);

			int imageWidth = r.width() - (border * 2);
			int imageHeight = imageWidth * d.getIntrinsicHeight()
					/ d.getIntrinsicWidth();
			if (imageHeight > r.height() - (border * 2)) {
				imageHeight = r.height() - (border * 2);
				imageWidth = imageHeight * d.getIntrinsicWidth()
						/ d.getIntrinsicHeight();
			}

			r.left += ((r.width() - imageWidth) / 2) - border;
			r.right = r.left + imageWidth + border + border;
			r.top += ((r.height() - imageHeight) / 2) - border;
			r.bottom = r.top + imageHeight + border + border;

			Paint p = new Paint();
			p.setColor(0xFFC0C0C0);
			c.drawRect(r, p);
			r.left += border;
			r.right -= border;
			r.top += border;
			r.bottom -= border;

			d.setBounds(r);
			d.draw(c);

			return b;
		}

		
	public void updatePage(final CurlPage page, final int width, final int height,final int index) {
			loadImageAndSetTexture(page, width, height, index);
	}

	private void loadImageAndSetTexture(final CurlPage page, final int width, final int height,final int index){
			imageLoader.loadImage(album.getImgs().get(index), options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);

							if (loadedImage != null&& !loadedImage.isRecycled()){
								page.setTexture(loadedImage, CurlPage.SIDE_BOTH);
								LogUtils.i("comick", "setTexture.");
							}
							
							if(index < (album.getImgs().size() -1)){
								imageLoader.loadImage(album.getImgs().get(index+1), options, null);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							super.onLoadingFailed(imageUri, view, failReason);

							Bitmap loadingBitmap = loadBitmap(width, height, 1);
							page.setTexture(loadingBitmap, CurlPage.SIDE_BOTH);
						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							super.onLoadingStarted(imageUri, view);

							Bitmap loadingBitmap = loadBitmap(width, height, 0);
							page.setTexture(loadingBitmap, CurlPage.SIDE_BOTH);
						}
					});
		}
	}
	/**
	 * CurlView size changed observer.
	 */
	private class SizeChangedObserver implements CurlView.SizeChangedObserver {
		public void onSizeChanged(int w, int h) {
			/*if (w > h) {
				mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
				mCurlView.setMargins(.1f, .05f, .1f, .05f);
			} else {
				mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
				mCurlView.setMargins(.1f, .1f, .1f, .1f);
			}*/
			mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
			mCurlView.setMargins(.02f, .01f, .01f, .01f);
		}
	}

}
