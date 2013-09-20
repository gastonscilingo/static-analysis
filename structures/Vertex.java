package structures;

public class Vertex {
	
	
	
	private String line;
	private VertexType type;

	public Vertex(String line, VertexType type) {
		super();
		this.line = line;
		this.type = type;
	}
	
	public Vertex() {
		this.line = "join";
		this.type = VertexType.JOIN;
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public Boolean isCond() {
		return this.type==VertexType.COND;
	}
	public void setType(VertexType t) {
		this.type = t;
	}
	
	@Override
	public String toString() {
		return line;
	}
	
	
	

}
