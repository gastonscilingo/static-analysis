package structures;

import org.jgrapht.EdgeFactory;

public class OwnEdgeFactory<V,E> implements EdgeFactory<V, E> {

	private final Class<? extends E> edgeClass;
	
	
	
	
	public OwnEdgeFactory(Class<? extends E> edgeClass) {
		this.edgeClass = edgeClass;
	}

	@Override
	public E createEdge(V arg0, V arg1) {
		try {
			return edgeClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Edge Factory Failed ",e);
		}
	}

}
