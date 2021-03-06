package structures;

import java.util.LinkedList;

public class Vertex {
	
	private int num = 0;
	private static int n = 0;
	private String line;
	private VertexType type;
	private LinkedList<Vertex>dominators;
	private Vertex iDominator;
	private String varModified;
	private String exprGenerated;
	
	private LinkedList<String>in;
	private LinkedList<String>out;
	
	private LinkedList<Pair<Integer,String>> kill;
	
	
	private LinkedList<Pair<Integer,String>> defOut;
	private LinkedList<Pair<Integer,String>> defIn;
	private boolean mark = false;
	
	public Vertex clone(){
		Vertex v = new Vertex(this.line,this.type);
		v.num = num;
		v.n = n;
		v.dominators = (LinkedList<Vertex>) dominators.clone();
		v.in = (LinkedList<String>) in.clone();
		v.out = (LinkedList<String>) out.clone();
		v.defIn = (LinkedList<Pair<Integer,String>>) defIn.clone();
		v.defOut = (LinkedList<Pair<Integer,String>>) defOut.clone();
		v.kill = (LinkedList<Pair<Integer,String>>) kill.clone();
		
		if (this.iDominator!=null)
			v.iDominator = iDominator.clone();
		return v;
	}

	public Vertex(String line, VertexType type) {
		super();
		num = n++ ;
		this.line = line;
		this.type = type;
		in = new LinkedList<String>();
		out = new LinkedList<String>();
		defIn = new LinkedList<Pair<Integer,String>>();
		defOut = new LinkedList<Pair<Integer,String>>();
		kill = new LinkedList<Pair<Integer,String>>();
	}
	
	public Vertex() {
		num = n++;
		this.line = "join";
		this.type = VertexType.JOIN;
		in = new LinkedList<String>();
		out = new LinkedList<String>();
		defIn = new LinkedList<Pair<Integer,String>>();
		defOut = new LinkedList<Pair<Integer,String>>();
		kill = new LinkedList<Pair<Integer,String>>();
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
	
	public Boolean isCondWhile() {
		return this.type==VertexType.CONDW;
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
	
	
	public LinkedList<Vertex> getDominators() {
		return dominators;
	}

	public void setDominators(LinkedList<Vertex> dominators) {
		this.dominators = dominators;
	}
	
	public void addDominators(Vertex v){
		if(!dominators.contains(v))
			dominators.add(v);
	}
	
	public Vertex getiDominators() {
		return iDominator;
	}

	public void setiDominators(Vertex iDominators) {
		this.iDominator = iDominators;
	}

	public LinkedList<Vertex> getSDominators(){
		LinkedList<Vertex> sDoms = (LinkedList<Vertex>)this.dominators.clone();
		sDoms.remove(this);
		return sDoms;
	}
	
	@Override
	public String toString() {
		return num+". "+line;
	}

	public boolean isBegin() {
		return type == VertexType.BEGIN;
	}
	
	public boolean isEnd(){
		return type == VertexType.END;
	}

	public String getExprGenerated() {
		return exprGenerated;
	}
		
	public void setExprGenerated(String exprGenerated) {
		this.exprGenerated = exprGenerated;
	}

	public String getVarModified() {
		return varModified;
	}

	public void setVarModified(String varModified) {
		this.varModified = varModified;
	}

	public LinkedList<String> getIn() {
		return in;
	}

	public void setIn(LinkedList<String> in) {
		this.in = in;
	}

	public LinkedList<String> getOut() {
		return out;
	}

	public void setOut(LinkedList<String> out) {
		this.out = out;
	}

	public LinkedList<Pair<Integer, String>> getDefOut() {
		return defOut;
	}

	public void setDefOut(LinkedList<Pair<Integer, String>> defOut) {
		this.defOut = defOut;
	}

	public LinkedList<Pair<Integer, String>> getDefIn() {
		return defIn;
	}

	public void setDefIn(LinkedList<Pair<Integer, String>> defIn) {
		this.defIn = defIn;
	}

	public Pair<Integer,String> getDefGen() {
		return new Pair<Integer,String> (num,varModified);
	}

	public LinkedList<Pair<Integer,String>> getDefKill() {
		return kill;
	}

	public void mark() {
		this.mark  = true;
		
	}

	public boolean isMarked() {
		return mark;
	}
	
	
}
