
public class TrackmnVO {

	private String trackName;
	private String subName;
	private String level;
	
	public TrackmnVO() {
		trackName = null;
		subName = null;
		level = null;
	}
	
	public void setTrackName(String name) {
		this.trackName = name;
	}
	public String getTrackName() {
		return this.trackName;
	}
	public void setSubName(String name) {
		this.subName = name;
	}
	public String getSubName() {
		return this.subName;
	}
	public void setLevel(String lev) {
		this.level = lev;
	}
	public String getLevel() {
		return this.level;
	}
}
