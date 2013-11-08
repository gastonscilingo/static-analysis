package algorithms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedGraph;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Pair;
import structures.Vertex;
import structures.VertexType;

public class AlgorithmsDominators {
	
	static StringBuffer outputDot;

	public AlgorithmsDominators() {
		outputDot = new StringBuffer("digraph name {\n");
	}
	
	public static StringBuffer getOutputDot() {
		return outputDot;
	}

	private LinkedList<Vertex> getPredecessors(Vertex v, SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> result = new LinkedList<Vertex>();
		Set<Edge> edgeSet = graph.incomingEdgesOf(v);
		
		for (Edge<Vertex> e : edgeSet){
			result.add(graph.getEdgeSource(e));
		}
		return result;
		
	}
	
	public void computeDominators(SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertexs = graph.vertexSet();
		Iterator<Vertex> iterator = vertexs.iterator();
		
		while(iterator.hasNext()){
			vertexList.add(iterator.next());
		}
		
		for (Vertex v : vertexs) {
			if (!v.isBegin()){
				v.setDominators((LinkedList<Vertex>)vertexList.clone());
			}else{
				LinkedList<Vertex> l = new LinkedList<Vertex>();
				l.add(v);
				v.setDominators(l);
			}
		}
		Boolean done = false;
		while (!done){
			done = true;
			for (Vertex v : vertexs){
				int length = v.getDominators().size();
				if(!v.isBegin()){
					LinkedList<Vertex> newDom = v.getDominators();
					LinkedList<Vertex> pre = getPredecessors(v,graph);
					for (Vertex p : pre){
						newDom = intersection(newDom, p.getDominators());
					}
					v.setDominators(newDom);
					v.addDominators(v);
				}
				if(length != v.getDominators().size()){
					done = false;
				}
			}
		}
	}
	
	private LinkedList<Vertex> intersection(LinkedList<Vertex> newDom,LinkedList<Vertex> otherDoms){
		LinkedList<Vertex> result = new LinkedList<Vertex>() ;
		for (Vertex v : newDom){
			if(otherDoms.contains(v)){
				result.add(v);
			}
		}
		return result;
	}
	
	public SimpleDirectedGraph<Vertex, Edge> reverse(SimpleDirectedGraph<Vertex, Edge> graph){
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> reverseGraph = new SimpleDirectedGraph<Vertex, Edge>(f);
		
		for (Vertex v : graph.vertexSet()){
			if(v.isBegin()){
				v.setType(VertexType.END);
			}else{
				if(v.isEnd()){
					v.setType(VertexType.BEGIN);
				}
			}
			reverseGraph.addVertex(v);
		}
		
		Set<Edge> set = graph.edgeSet();
		Vertex source;
		Vertex target;
		System.out.println(" "+set);
		for (Edge<Vertex> e : set){
			// TODO solve aliasing problem
			source = graph.getEdgeTarget(e); 
			target = graph.getEdgeSource(e);			
			reverseGraph.addEdge(source,target);
			//System.out.println("edge : "+e);
			//System.out.println("edge : "+source);
			//System.out.println("edge : "+target);
		}
		System.out.println("reverse graph "+reverseGraph.edgeSet());
		return reverseGraph;
	}
	
	public void computeidominator(SimpleDirectedGraph<Vertex,Edge> graph) throws Exception{
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertexs = graph.vertexSet();
		
		for (Vertex v : vertexs){
			LinkedList<Vertex> myDoms = v.getSDominators();
			System.out.println("myDoms size : "+myDoms.size());
			int i = 0;
			while(i<myDoms.size()){
				Vertex dom = myDoms.get(i);
				LinkedList<Vertex> intersectList =  intersection(myDoms,dom.getSDominators());
				System.out.println("myDoms : "+myDoms);
				myDoms.removeAll(intersectList);
				System.out.println("myDoms : "+myDoms);
				i++;
			}
			if(myDoms.size()>1){
				System.out.println("Bad");
				throw new Exception("there more than one idominators"); 
			}else{
				if(myDoms.size()==1)
					v.setiDominators(myDoms.getFirst());
			}
		}
	}
	
	public SimpleDirectedGraph<Vertex, Edge> computeDominatorsTree(SimpleDirectedGraph<Vertex, Edge> graph){
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> dominatorsTree = new SimpleDirectedGraph<Vertex, Edge>(f);
		
		for (Vertex v : graph.vertexSet()){
			if(v.getiDominators()!=null){
				dominatorsTree.addVertex(v);
				dominatorsTree.addVertex(v.getiDominators());
				dominatorsTree.addEdge(v.getiDominators(),v);
				writeGraph(v.getiDominators(),v);
			}
		}
		outputDot.append("}\n");
		return dominatorsTree;
	}
	
	
	public SimpleDirectedGraph<Vertex,Edge> computeControlDependenceGraph2(SimpleDirectedGraph<Vertex, Edge> graph,SimpleDirectedGraph<Vertex, Edge> tree){
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertexs = graph.vertexSet();
		Set<Edge> edges = graph.edgeSet();
		
		System.out.println("Edges");
		for (Edge e : edges){
			System.out.println(e);
		}
		
		return null;
		
	}
	
	public LinkedList<Edge> incommingEdges(SimpleDirectedGraph<Vertex,Edge> tree, Vertex b){
		LinkedList<Edge> s = new LinkedList<Edge>();
		
		for(Edge<Vertex> e: tree.edgeSet()){
			if(tree.getEdgeTarget(e)==b){
				s.add(e);
				System.out.println("ALIncomming of "+ b+ " is "+ tree.getEdgeSource(e));
			}
		}
		System.out.println("El tamaño es "+s.size());
		return s;
	}
	/*
	 * Returns a list with the nodes of the tree from the parameter to the root 
	 */
	public LinkedList<Vertex> ancestorsOf(SimpleDirectedGraph<Vertex,Edge> tree,Vertex a) throws Exception{
		LinkedList<Vertex> ancestors = new LinkedList<Vertex>();
		
		Vertex vert = a;
		while(vert != null){
			Set<Edge> incA = tree.incomingEdgesOf(vert);
			if(incA.size()==0)
				vert = null;
			else{
				//incA.size must be 1
				if(incA.size()>1){
					throw new Exception("More than one incomming edges of the vertex "+ vert);
				}
					
				for(Edge e : incA){		
					ancestors.add(tree.getEdgeSource(e));
					vert = tree.getEdgeSource(e);
				}
			}
		}
		return ancestors;
	}
/*
 * Compute the immediately common ancestor vertex of two vertex in the tree 
 */	
public Vertex lessCommonAncestor(SimpleDirectedGraph<Vertex,Edge> tree, Vertex a, Vertex b) throws Exception{
	    LinkedList<Vertex> ancestorsA = new LinkedList<Vertex>();	
	    LinkedList<Vertex> ancestorsB = new LinkedList<Vertex>();
	    
		if (tree.getEdge(a, b) != null){
			
			return a;
		}
		if (tree.getEdge(b, a) != null){
			ancestorsB.add(b);
			return b;
		}
				
		ancestorsA = ancestorsOf(tree, a);
		ancestorsA.addFirst(a);
		ancestorsB = ancestorsOf(tree, b);
		ancestorsB.addFirst(b);
		
		System.out.println("Ancestros de "+a+" = "+ ancestorsA.toString());
		System.out.println("Ancestros de "+b+" = "+ ancestorsB.toString());
		
		return intersection(ancestorsA,ancestorsB).getFirst();
}
	/*
	 * Algorithm for b step of construct CDG
	 */
	public Vertex lessCommonAncestor2(SimpleDirectedGraph<Vertex,Edge> tree, Vertex a, Vertex b) throws Exception{
		
		if (tree.getEdge(a, b) != null)
			return a;
		if (tree.getEdge(b, a) != null)
			return b;
		
		Vertex ancestor = null;
		//Set<Edge> incomingEdgesOfB = tree.incomingEdgesOf(b);
		LinkedList<Edge> incommingEdgesOfB = incommingEdges(tree,b);
		LinkedList<Vertex> ancestorsOfB = new LinkedList<Vertex>();
		// make set of all ancestors of b
		while (incommingEdgesOfB!=null) {
			System.out.println("Incomming edge de "+ b+" es "+ incommingEdgesOfB.toString());
			if (incommingEdgesOfB.size()!=1)
				throw new Exception("More than one incomming edges");
			for (Edge<Vertex> e : incommingEdgesOfB ){//incomingEdgesOfB.size() must be 1
				
				ancestor = tree.getEdgeSource(e);
				ancestorsOfB.add(ancestor);
				System.out.println("Ancestor of "+tree.getEdgeTarget(e)+" is "+ancestor);
				if (ancestor == a)
					return ancestor;
			}
			if(ancestor==null)
				throw new Exception("Ancestor wrong !!!");
			incommingEdgesOfB = incommingEdges(tree, ancestor);
		}
		
		Set<Edge> incomingEdgesOfA = tree.incomingEdgesOf(a);
		LinkedList<Vertex> ancestorsOfA = new LinkedList<Vertex>();
		while (incomingEdgesOfA!=null) {
			for (Edge<Vertex> e : incomingEdgesOfA ){//incomingEdgesOfA.size() must be 1
				ancestor = (Vertex) e.getSource();
				ancestorsOfA.add(ancestor);
				System.out.println("Ancestor of "+e.getTarget()+" is "+ancestor);
				if (ancestorsOfB.contains(ancestor))
						return ancestor;
			}
			if(ancestor==null)
				throw new Exception("Ancestor wrong !!!");
			incomingEdgesOfA = tree.incomingEdgesOf(ancestor);
		}
		return null;
	}
	
	/*
	 * Return a list of graph edges (A,B) such that B is not ancestor of A in the tree. 
	 */
	public LinkedList<Edge<Vertex>> edgesNotAncestralsInTree(SimpleDirectedGraph<Vertex,Edge> graph, SimpleDirectedGraph<Vertex,Edge> tree){
		LinkedList<Edge<Vertex>> list = new LinkedList<Edge<Vertex>>();
		Set<Edge> graphEdges = graph.edgeSet();
		LinkedList<Vertex> ancestors;
		
		for(Edge e : graphEdges){
			try {
				//compute ancestors of A
				//System.out.println("SOURCE ES: "+graph.getEdgeSource(e));
				//System.out.println("["+graph.getEdgeSource(e)+"] -> ["+graph.getEdgeTarget(e)+"]");
				ancestors = ancestorsOf(tree, graph.getEdgeSource(e));
				//is B part of them?
				if(!ancestors.contains(graph.getEdgeTarget(e))){
					//if not, add to the result list
					list.add(e);
					System.out.println("["+graph.getEdgeSource(e)+"] -> ["+graph.getEdgeTarget(e)+"]");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block				
				e1.printStackTrace();
			}
			
		}
		return list;
		
	}
	
	public SimpleDirectedGraph<Vertex,Edge> computeControlDependenceGraph(SimpleDirectedGraph<Vertex,Edge> graph, SimpleDirectedGraph<Vertex,Edge> domTree){
		flashOutputDot();
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> CDG = new SimpleDirectedGraph<Vertex, Edge>(f);
		LinkedList<Edge<Vertex>> S = edgesNotAncestralsInTree(graph, domTree);
		for(Edge<Vertex> e : S){
			try {
				Vertex a =  graph.getEdgeSource(e);
				Vertex b =  graph.getEdgeTarget(e);
				Vertex L =  lessCommonAncestor(domTree,a, b);
				//traversal from b to L
				Vertex vert = b;
				CDG.addVertex(a);
				CDG.addVertex(b);
				CDG.addEdge(a, b);
				writeGraph(a,b);
				while(vert != null){
					Set<Edge> incA = domTree.incomingEdgesOf(vert);
					if(incA.size()==0)
						vert = null;
					else{
						//incA.size must be 1
						if(incA.size()>1){
							throw new Exception("More than one incomming edges of the vertex "+ vert);
						}						
						for(Edge edge : incA){
							Vertex aux = domTree.getEdgeSource(edge);
							if(aux==L){
								//end of the process
								if(L==a){
									//MARK(a, aux);
									/*CDG.addVertex(a);
									CDG.addVertex(aux);
									CDG.addEdge(a, aux);*/
									writeGraph(a,aux);
								}
								vert = null;
							}else{
								//verify if source is L, if not, continue
								//MARK(a, aux);
								CDG.addVertex(a);
								CDG.addVertex(aux);
								CDG.addEdge(a, aux);
								writeGraph(a,aux);
								vert = aux;
							}
						}
					}		
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
		}
		outputDot.append("}\n");
		return CDG;
	}
	
	
	private static void writeGraph(Vertex a , Vertex b){
		  
	  	outputDot.append("\""+a.getNum()+". "+a.getLine()+"\""+ "  ->  " + "\""+b.getNum()+". "+b.getLine() +"\""+ "\n");
	  
	  }

	private void flashOutputDot() {
		outputDot = new StringBuffer("digraph name {\n");
	}
	
	public LinkedList<String> intersecStringList(LinkedList<String> out, LinkedList<String> in){
		LinkedList<String> result = new LinkedList<String>() ;
		for (String str : out){
			if(in.contains(str)){
				result.add(str);
			}
		}
		return result;
	}
	 
	/*This function computes the available expressions*/
	public void computeAvailableExpressions(SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> list;
		boolean change= true;
		int count=0;
		
		for (Vertex v : graph.vertexSet()){
			if(v.getExprGenerated() != null){
				v.getOut().add(v.getExprGenerated());
			}
			System.out.println(v.toString()+" gen= "+v.getExprGenerated());
		}
		
		while(change){
			change = false;
			count ++;
			for (Vertex v : graph.vertexSet()){
				list = getPredecessors(v, graph);
				
				LinkedList<String> oldIn = (LinkedList<String>)v.getIn().clone();
				
				LinkedList<LinkedList<String>> predOut = new LinkedList<LinkedList<String>>();
				
				//compute the intersection of predecessors out list
				for(Vertex j: list){					
					predOut.add(j.getOut());					
				}
				LinkedList<String> inter= new LinkedList<String>();
				
				if(predOut.size()>=1){
					inter = predOut.removeFirst();
					while(predOut.size()>=1){
						inter = intersecStringList(inter,predOut.removeFirst());
					}
				}
				v.setIn(inter);
				
				LinkedList<String> tmpOut = (LinkedList<String>) v.getIn().clone();
				LinkedList<String> newOut= new LinkedList<String>();
				
				for(String s: tmpOut){
					if(v.getVarModified()==null){
						//if the node does not modifies anything, we transfer the in to out.
						newOut.add(s);
					}else{
						 if(!s.contains(v.getVarModified())){
							 //this generated expression does not kill any other.
							 newOut.add(s);
						 }
					}
				}
				
				if(v.getExprGenerated()!=null)
					newOut.add(v.getExprGenerated()); 
			
				v.setOut(newOut);
				
				if(!isEqual(oldIn,v.getIn())){
					change = true;
				}				
			}
			
		}
		System.out.println("Se hicieron "+count+ " pasadas.");
	}
	
	/*This function computes the available expressions*/
	public void computeReachigDefinitios(SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> list;
		boolean change= true;
		int count=0;
		
		for (Vertex v : graph.vertexSet()){
			if(v.getVarModified() != null){
				v.getDefOut().add(v.getDefGen());
				v.getDefKill().add(v.getDefGen());
			}
			System.out.println(v.toString()+" defGen= "+v.getDefGen());
		}
		
		while(change){
			change = false;
			count ++;
			for (Vertex v : graph.vertexSet()){
				list = getPredecessors(v, graph);
				
				LinkedList<Pair<Integer,String>> oldOut = (LinkedList<Pair<Integer,String>>)v.getDefOut().clone();
				
				LinkedList<Pair<Integer,String>> unionPredOut = new LinkedList<Pair<Integer,String>>();
				
				//compute the union of predecessors defOut lists
				for(Vertex j: list){
					for (Pair<Integer,String> p : j.getDefOut()) {
						if (!existPair(p,unionPredOut))
							unionPredOut.add(p);
					}
										
				}
				
				v.setDefIn(unionPredOut);
				
				LinkedList<Pair<Integer,String>> tmpDefIn = (LinkedList<Pair<Integer,String>>) v.getDefIn().clone();
				LinkedList<Pair<Integer,String>> newOut= new LinkedList<Pair<Integer,String>>();
				
				LinkedList<Pair<Integer,String>> vDefKill = (LinkedList<Pair<Integer,String>>) v.getDefKill().clone();
				for(Pair<Integer,String> p: tmpDefIn){
					if (!exist(p,vDefKill) ){
						if (!existPair(p,newOut))
							newOut.add(p);
					}else{
						for (Pair<Integer,String> p2 : vDefKill ){
							if (p2.getFst().compareTo(p.getFst())!=0 && p2.getSnd().compareTo(p.getSnd())==0 && !existPair(p,v.getDefKill()))  {
								v.getDefKill().add(p);
							}
							
						}
						
					}
					
				}
			
				
				if(v.getDefGen().getSnd() != null)//TODO
					newOut.add(v.getDefGen()); 
			
				v.setDefOut(newOut);
				if(!isListPairEqual(oldOut,v.getDefOut())){
					change = true;
					
				}				
			}
			
		}
		System.out.println("Se hicieron "+count+ " pasadas.");
	}
	
	public SimpleDirectedGraph<Vertex,Edge> computeDataDependenceGraph(SimpleDirectedGraph<Vertex,Edge> graph){
		flashOutputDot();
		
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> ddg = new SimpleDirectedGraph<Vertex, Edge>(f);
		
		for (Vertex v : graph.vertexSet()){
			
			String [] str;
			if(v.getExprGenerated()!=null){
				
				str = v.getExprGenerated().split("<=|>=|[+<>]|==|!=");//[+<>]|['==']	
				System.out.println(str.toString());
				for(String s: str){
					for(Pair<Integer,String> p: v.getDefIn()){
						if(s.compareTo(p.getSnd())==0){
							Vertex def = getVertexByNum(graph.vertexSet(),p.getFst());
							
							//ASSERT def can not be null.
							
							ddg.addVertex(def);
							ddg.addVertex(v);						
							ddg.addEdge(def,v);
							
							writeGraph(def,v);
									
						}
					}
				}
			}
						
		}
		outputDot.append("}\n");
		return ddg;
	}
	private Vertex getVertexByNum (Set<Vertex> edgeSet, Integer num){
		for(Vertex v: edgeSet){
			if(v.getNum()==num){
				return v;
			}
		}
		return null;
	}
	
	private boolean existPair(Pair<Integer, String> p, LinkedList<Pair<Integer, String>> defKill) {
		for (Pair<Integer,String> pkill : defKill ){
			if(p.getSnd().compareTo(pkill.getSnd())==0 && p.getFst().compareTo(pkill.getFst())==0){
				return true;
			}
		}
		return false;
	}

	public boolean exist(Pair<Integer,String> p, LinkedList<Pair<Integer,String>> l ){
		for (Pair<Integer,String> pkill : l ){
			if(p.getSnd().compareTo(pkill.getSnd())==0){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isEqual(LinkedList<String> oldList, LinkedList<String> newList){		
		
		if(oldList.size()!=newList.size())
			return false;
		
		for(String s : newList){
			if(!oldList.contains(s)){
				return false;
			}
		}
		
		return true;
		
	}
	
public boolean isListPairEqual(LinkedList<Pair<Integer,String>> oldList, LinkedList<Pair<Integer,String>> newList){		
		
		if(oldList.size()!=newList.size())
			return false;
		
		for(Pair<Integer,String> p : newList){
			if(!exist(p,oldList)){
				return false;
			}
		}
		
		return true;
		
	}
	
	public void showAvailableExpressions(SimpleDirectedGraph<Vertex,Edge> graph){
		for (Vertex v: graph.vertexSet()){
			
			System.out.println(v.getLine()+".in: "+ v.getIn().toString());
			System.out.println(v.getLine()+".out: "+ v.getOut().toString());
			
		}
		
	}
	
	public void showReachingDefinitions(SimpleDirectedGraph<Vertex,Edge> graph){
		for (Vertex v: graph.vertexSet()){
			
			System.out.println(v.getLine()+".defGen: "+ v.getDefGen().toString());
			System.out.println(v.getLine()+".defIn: "+ v.getDefIn().toString());
			System.out.println(v.getLine()+".defOut: "+ v.getDefOut().toString());
			System.out.println(v.getLine()+".defKill: "+ v.getDefKill().toString());
		}
		
	}
	
	
	
}
