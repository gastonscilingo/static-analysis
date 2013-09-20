import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import parser.ProgramParser;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		try {
			//graph = new SimpleDirectedGraph<String,Edge>(Edge.class);
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			scenarioParser.parseProgram(graph);
			
			
			
			// Print graph
			System.out.println("\n	Graph : \n");
			System.out.println("Vertex : "+graph.vertexSet().toString());
			System.out.println("Edges : "+graph.edgeSet().toString());
			
			System.out.println(graph.toString());
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (parser.ParseException e) {
			e.printStackTrace();
			System.out.println("error in parsing program !!!");
		}
		
		
		

	}

}
