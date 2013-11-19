package utils;

import structures.Vertex;

public class Outputs {
	
	static StringBuffer outputDotBody;
	static String graphName;

	public Outputs() {
		outputDotBody = new StringBuffer("");
	}
	
	public StringBuffer getOutputDot() {
		StringBuffer outputDot = new StringBuffer("digraph "+graphName+" {\n");
		outputDot.append(outputDotBody) ;
		outputDot.append("}\n");
		return outputDot;
	}

	public StringBuffer getOutputBody() {
		return outputDotBody;
	}
	
	
	private void writeGraph(Vertex a , Vertex b,String color){
		  
	  	outputDotBody.append("\""+a.getNum()+". "+a.getLine()+"\""+ "  ->  " + "\""+b.getNum()+". "+b.getLine() +"\""+"[color="+color+"]"+ "\n");
	  
	  }
	
	private void writeGraphWithColor(Vertex a,String color){
		  
	  	outputDotBody.append("\""+a.getNum()+". "+a.getLine()+"\""+ " [style=filled, fillcolor="+color+"];"+ "\n");
	  
	  }

	private void flashOutputDot(String newName) {
		graphName = newName;
		outputDotBody = new StringBuffer(""); 
	}
	
}
