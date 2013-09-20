package structures;

import org.jgraph.graph.DefaultEdge;

public class Edge<V> extends DefaultEdge {

	private String label;
	
	public Edge(){
		
	}
	
	
	
	public Edge(V v1, V v2, String label) {
		super();
		this.setSource(v1);
		this.setTarget(v2);
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "-("+label+ ")->";// + ", source=" + source.toString() + ", target="	+ target.toString() + "]";
	}

	
	
	

}
