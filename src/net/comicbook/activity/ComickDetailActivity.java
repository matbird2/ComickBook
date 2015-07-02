package net.comicbook.activity;

import net.comicbook.R;
import android.app.Activity;
import android.os.Bundle;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class ComickDetailActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FadingActionBarHelper helper = new FadingActionBarHelper()
        .actionBarBackground(R.drawable.ab_background_light)
        .headerLayout(R.layout.header_fading)
        .contentLayout(R.layout.activity_detail);
    setContentView(helper.createView(this));
    helper.initActionBar(this);
	}
}
