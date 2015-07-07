package net.comicbook.bean.bmob;

import cn.bmob.v3.BmobUser;

public class ComickUser extends BmobUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7209106141258960672L;
	private String nickname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
