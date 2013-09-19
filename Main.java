import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import parser.ProgramParser;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<String,DefaultEdge> graph;
		try {
			graph = new SimpleDirectedGraph<String,DefaultEdge>(DefaultEdge.class); 
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			scenarioParser.parseProgram(graph);
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
