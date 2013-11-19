
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




import org.jgrapht.graph.SimpleDirectedGraph;

import algorithms.AlgorithmsDominators;

import parser.ProgramParser;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;
import structures.AnalysisType;

import utils.Outputs;

public class Main {

	static boolean macOS = false;
	static int lineNumberToSlice;
	static AnalysisType analysisType;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, InterruptedException {	
		System.out.println("Run on : "+System.getProperty("os.name"));
		
		parsingFlags(args);


		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		SimpleDirectedGraph<Vertex,Edge> reverseGraph;
		SimpleDirectedGraph<Vertex,Edge> dominatorsTree;
		SimpleDirectedGraph<Vertex,Edge> cdg;
		SimpleDirectedGraph<Vertex,Edge> ddg;
		//SimpleDirectedGraph<Vertex,Edge> ddg; never made only written.
		
		Outputs out = new Outputs();
		StringBuffer dotFile = new StringBuffer("");
		StringBuffer pdg = new StringBuffer("digraph pdg {\n");
		
		try {
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			
			// Parse program and write CFG in dot file
			scenarioParser.parseProgram(graph,dotFile);

			/* Write output file*/
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter("graph.txt");
				fileWriter.write(dotFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
						
			// Print graph for debug
			//printGraph(graph);
			
			// Compute Dominator
			AlgorithmsDominators algorithmsDominator = new AlgorithmsDominators (out);
			algorithmsDominator.computeDominators(graph);
			
			// Compute Post-Dominator
			reverseGraph = algorithmsDominator.reverse(graph);
			algorithmsDominator.computeDominators(reverseGraph);

			// Compute Immediate Dominators
			algorithmsDominator.computeidominator(reverseGraph);
			
			// Compute Post Dominators Tree and write dot file
			dominatorsTree = algorithmsDominator.computeDominatorsTree(reverseGraph);
			StringBuffer treeFile = out.getOutputDot(); // algorithmsDominator.getOutputDot();
			try {
				fileWriter = new FileWriter("tree.txt");
				fileWriter.write(treeFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// step a: compute set S
			//LinkedList<Edge<Vertex>> S = algorithmsDominator.edgesNotAncestralsInTree(graph, dominatorsTree);
			
			cdg = algorithmsDominator.computeControlDependenceGraph(graph, dominatorsTree);
			StringBuffer cdgFile = out.getOutputDot();
			pdg.append(out.getOutputBody());
			try {
				fileWriter = new FileWriter("cdg.txt");
				fileWriter.write(cdgFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			algorithmsDominator.computeReachigDefinitios(graph);

			algorithmsDominator.computeAvailableExpressions(graph);
			algorithmsDominator.showAvailableExpressions(graph);
			algorithmsDominator.showReachingDefinitions(graph);

			ddg = algorithmsDominator.computeDataDependenceGraph(graph);
			
			StringBuffer ddgFile = out.getOutputDot();
			pdg.append(out.getOutputBody());
			StringBuffer sliceGraph = new StringBuffer(pdg.toString());
			pdg.append("}\n");
			try {
				fileWriter = new FileWriter("ddg.txt");
				fileWriter.write(ddgFile.toString());
				fileWriter.close();
				fileWriter = new FileWriter("pdg.txt");
				fileWriter.write(pdg.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Vertex s = algorithmsDominator.getVertexByNum(cdg.vertexSet(),lineNumberToSlice);
			System.out.print("Selected : "+s.toString());
			algorithmsDominator.computeSlice(cdg, ddg, s);
			
			sliceGraph.append(out.getOutputBody());
			sliceGraph.append("}\n");
			try {
				fileWriter = new FileWriter("slice.txt");
				fileWriter.write(sliceGraph.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			// Run dot program to generate image and show it
			Process p;
			
			// Run dot program to generate image and show it
			p = out.dot2image("graph");
			p = out.dot2image("tree");
			p = out.dot2image("cdg");
			p = out.dot2image("ddg");
			p = out.dot2image("pdg");
			p = out.dot2image("slice");
			
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			
			
			switch (analysisType){
			case SG : {out.openImage("slice"); break;}
			case CFG :  {out.openImage("graph");break;}
			case PDT : {out.openImage("tree");break;}
			case IPDT : {out.printIDomitators(reverseGraph);break;}
			case CDG : {out.openImage("cdg");break;}
			case DDG : {out.openImage("ddg");break;}
			case PDG : {out.openImage("pdg");break;}
			}
		
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (parser.ParseException e) {
			e.printStackTrace();
			System.out.println("error in parsing program !!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void parsingFlags(String [] args) {
		if (args.length < 2) {
			System.out.println("use with at less 2 params for: <file path> <type of analysis>");
			return;
		}
		if (args.length  >= 2) {
			
			if (args[1].contains("SG")){
				analysisType = AnalysisType.SG;
				lineNumberToSlice = Integer.valueOf(args[2]).intValue();
				//TODO try catch
			}
			if (args[1].contains("CFG")){
				analysisType = AnalysisType.CFG;
			}			
			if (args[1].contains("PDT")){
				analysisType = AnalysisType.PDT;
			}
			if (args[1].contains("IPDT")){
				analysisType = AnalysisType.IPDT;
			}
			if (args[1].contains("CDG")){
				analysisType = AnalysisType.CDG;
			}	
			if (args[1].contains("DDG")){
				analysisType = AnalysisType.DDG;
			}
			
			System.out.println("use with at less 2 params for: <file path> <type of analysis>");
			return;
		}
		
	}


	
	
}
