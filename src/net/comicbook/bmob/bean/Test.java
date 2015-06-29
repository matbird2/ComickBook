package net.comicbook.bmob.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Test extends BmobObject{

	private List<String> imgs;

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
}
