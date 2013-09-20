package structures;

public class Vertex {
	
	
	private String line;
	private Boolean isCond_ = false;
	private Boolean isJoin_ = false;

	public Vertex(String line, Boolean cond) {
		super();
		this.line = line;
		this.isCond_ = cond;
	}
	
	public Vertex() {
		this.line = "join";
		this.isJoin_ = true;
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public Boolean isCond() {
		return isCond_;
	}
	public void setIsCond(Boolean isCond) {
		this.isCond_ = isCond;
	}
	
	@Override
	public String toString() {
		return line;
	}
	
	
	

}
