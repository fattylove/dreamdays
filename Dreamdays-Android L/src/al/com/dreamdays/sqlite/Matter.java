package al.com.dreamdays.sqlite;

public class Matter {
	private int _id;
	private String matterName;
	private String matterTime;
	private String createTime;
	private String videoName;
	private String picName;
	private int if_notify;
	private int classifyType;
	private int ifStick;
	private int sort_id;
	private int repeat_type;

	public int getRepeat_type() {
		return repeat_type;
	}

	public void setRepeat_type(int repeat_type) {
		this.repeat_type = repeat_type;
	}

	private boolean isDeleteType;

	public boolean isDeleteType() {
		return isDeleteType;
	}

	public void setDeleteType(boolean isDeleteType) {
		this.isDeleteType = isDeleteType;
	}

	/**
	 * @return
	 */
	public int get_id() {
		return _id;
	}

	public int getIf_notify() {
		return if_notify;
	}

	public void setIf_notify(int if_notify) {
		this.if_notify = if_notify;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getMatterTime() {
		return matterTime;
	}

	public void setMatterTime(String matterTime) {
		this.matterTime = matterTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public int getClassifyType() {
		return classifyType;
	}

	public void setClassifyType(int classifyType) {
		this.classifyType = classifyType;
	}

	public int getIfStick() {
		return ifStick;
	}

	public void setIfStick(int ifStick) {
		this.ifStick = ifStick;
	}

	public int getSort_id() {
		return sort_id;
	}

	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

}
