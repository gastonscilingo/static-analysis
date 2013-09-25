import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		
		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		StringBuffer dotFile;
		try {
			//graph = new SimpleDirectedGraph<String,Edge>(Edge.class);
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			
			dotFile = new StringBuffer("digraph name {\n");
			scenarioParser.parseProgram(graph,dotFile);
			dotFile.append("}\n");
			
			
			// Print graph
			System.out.println("\n	Graph : \n");
			System.out.println("Vertex : "+graph.vertexSet().toString());
			System.out.println("Edges : "+graph.edgeSet().toString());
			
			System.out.println(graph.toString());
			

			/* Write output file*/
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter("graph.txt");
				fileWriter.write(dotFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO add your handling code here:
			//Process p = Runtime.getRuntime().exec("dot -T jpg -o graph.jpg graph.txt");
			Process p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o graph.jpg graph.txt");
	    	InputStream in = p.getInputStream();
	    	InputStreamReader inread = new InputStreamReader(in);
	    	BufferedReader bufferedreader = new BufferedReader(inread);
	    	
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			System.out.println("Salida : "+bufferedreader.readLine());
			p = Runtime.getRuntime().exec("open graph.jpg");
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (parser.ParseException e) {
			e.printStackTrace();
			System.out.println("error in parsing program !!!");
		}
		
		
		

	}

}
