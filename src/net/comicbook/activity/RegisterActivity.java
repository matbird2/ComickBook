package net.comicbook.activity;

import net.comicbook.R;
import net.comicbook.bean.bmob.ComickUser;
import net.comicbook.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.listener.SaveListener;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity{

	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;
	
	@ViewById(R.id.username)
	protected EditText eusername;
	@ViewById(R.id.password)
	protected EditText epassword;
	@ViewById(R.id.confirm)
	protected EditText econfirm;
	
	@AfterViews
	void initViews() {
		setSlideable(false);
		initTitleBar();
	}

	private void initTitleBar() {
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		top_more.setVisibility(View.VISIBLE);
		top_more.setImageResource(R.drawable.ok);
		title_recent.setText("注册");
	}
	
	@Click(R.id.top_more)
	void onRegister(View v){
		String username = eusername.getText().toString().trim();
		String password = epassword.getText().toString().trim();
		String confirm = econfirm.getText().toString().trim();
		
		if(TextUtils.isEmpty(username)){
			showShortToast("账号不可为空.");
			return ;
		}
		if(TextUtils.isEmpty(password)){
			showShortToast("密码不可为空.");
			return ;
		}
		if(TextUtils.isEmpty(confirm)){
			showShortToast("确认密码不可为空.");
			return ;
		}
		if(username.length() != 11){
			showShortToast("手机号位数不正确.");
			return ;
		}
		if(password.length() < 6){
			showShortToast("密码位数不能少于6位.");
			epassword.setText("");
			return ;
		}
		if(!password.equals(confirm)){
			showShortToast("两次输入的密码不一致");
			econfirm.setText("");
			return ;
		}
		
		changeStatus(false);
		showProgressDialog();
		String md5_password = Utils.encryptWithMD5(password);
		ComickUser user = new ComickUser();
		user.setUsername(username);
		user.setPassword(md5_password);
		user.signUp(this, new SaveListener() {
			@Override
			public void onFailure(int arg0, String arg1) {
				dismissProgressDialog();
				changeStatus(true);
				if(arg0 == 202){
					showCustomToast("用户名已被注册.");
				}else{
					showCustomToast("注册失败:"+arg1);
				}
			}
			@Override
			public void onSuccess() {
				dismissProgressDialog();
				showShortToast("注册成功.");
				finish();
			}
		});
	}
	
	private void changeStatus(boolean isEnable){
		eusername.setEnabled(isEnable);
		epassword.setEnabled(isEnable);
		econfirm.setEnabled(isEnable);
		top_more.setEnabled(isEnable);
		top_head.setEnabled(isEnable);
	}
	
	@Click(R.id.top_head)
	void onBack(View v){
		this.finish();
	}
}
