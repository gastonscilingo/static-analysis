package structures;

public class Vertex {
	
	private int num = 0;
	private static int n = 0;
	private String line;
	private VertexType type;

	public Vertex(String line, VertexType type) {
		super();
		num = n++ ;
		this.line = line;
		this.type = type;
	}
	
	public Vertex() {
		num = n++;
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
	
	public Boolean isDo() {
		return this.type==VertexType.DO;
	}
	
	public int getNum(){
		return num;
	}
	
	@Override
	public String toString() {
		return line;
	}
	
	
	

}
