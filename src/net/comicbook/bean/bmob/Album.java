package net.comicbook.bean.bmob;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Album extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5945061805081921350L;
	private String name;
	// 0:经典jd 1:评书ps 2:神话sh 3：民间mj 4：革命gm 5：武侠wx 6：外国wg 7：侦探zt 8：人物rw 9：儿童et 10：现代xd 11：其他qt
	private Integer type;
	private List<String> imgs;
	private String descri;
	private Integer status;
	private String remark;
	private Double price;
	private String cover;
	private Long length;
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public String getDescri() {
		return descri;
	}
	public void setDescri(String descri) {
		this.descri = descri;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

}
