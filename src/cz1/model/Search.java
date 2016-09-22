package cz1.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Search {

	final String operatingSystem = System.getProperty("os.name");

	public void clear() throws IOException {
		if (operatingSystem .contains("Windows")) {
			Runtime.getRuntime().exec("cls");
		}
		else {
			Runtime.getRuntime().exec("clear");
		}
	}
	
	public static void main(String[] args) 
			throws IOException {
		
		CommandLineParser parser = new PosixParser();

		Options options = new Options();
		options.addOption( "i", "input", true, "input file name." );
		options.addOption( "o", "output", true, "output file name." );
		String input = null, output = null;
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			if( line.hasOption("i") ) {
				input = line.getOptionValue('i');
			}
			if(line.hasOption("o")) {
				output = line.getOptionValue("o");
			}
		} catch( ParseException exp ) {
			System.out.println( "Unexpected exception:" + exp.getMessage() );
		}
		Search search;
		if(input!=null)
			search = new Search(input);
		else search = new Search();
		search.search();
		search.trace();
		search.print();
		if(output!=null) search.write(output);
	}

	private void print() {
		// TODO Auto-generated method stub
		for(int i=0; i<trace.length; i++) {
			//System.err.println(i);
			trace[i].klotski().print();
			//clear();
		}
	}

	private void write(String output) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(output));
		for(int i=0; i<trace.length; i++) 
			trace[i].klotski().write(bw);
		bw.close();
	}

	private Node[] trace;
	private void trace() throws IOException {
		// TODO Auto-generated method stub
		//clear();
		Node target = null;
		for(Node node : this.all) 
			if(node.klotski().target()) {
				target = node;
				break;
			}
		trace = new Node[target.level()+1];
		trace[trace.length-1] = target;
		for(int i=trace.length-2; i>=0; i--)  
			trace[i] = trace[i+1].trace();
	}

	private final Klotski start;
	/**
	private final Klotski start = new Klotski(new char[][] {
			{'1','c','c','2'},
			{'1','c','c','2'},
			{'z','3','3','z'},
			{'z','4','4','z'},
			{'o','5','5','o'}},
			new char[]{'o','c','1','2','3','4','5','z'});
	
	private final Klotski start = new Klotski(new char[][] {
			{'1','c','c','2'},
			{'1','c','c','2'},
			{'3','z','z','o'},
			{'3','4','4','z'},
			{'z','5','5','o'}},
			new char[]{'o','c','1','2','3','4','5','z'});	
	**/
	private Set<Node> all;

	public Search(String input) throws IOException {
		this.start = this.start(input);
		this.all = new HashSet<Node>();
		all.add(new Node(this.start, null, true, 0));
	}
	
	public Search() throws IOException {
		this.start = this.start();
		this.all = new HashSet<Node>();
		all.add(new Node(this.start, null, true, 0));
	}

	private Klotski start() {
		// TODO Auto-generated method stub
		return new Klotski(new char[][] {
				{'1','c','c','2'},
				{'1','c','c','2'},
				{'z','3','3','z'},
				{'z','4','4','z'},
				{'o','5','5','o'}},
				new char[]{'o','c','1','2','3','4','5','z'});
	}

	private Klotski start(String input) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(
				new FileReader(input));
		String[] s;
		String line;
		char[][] phase = null;
		char[] piece = null;
		boolean check_phase = false, 
				check_piece = false;
		while( (line=br.readLine())!=null ) {
			if(line.trim().length()==0 || 
					line.startsWith("#%")) continue;
			if(line.startsWith("#")) {
				String d = line.replaceAll("#", "");
				switch(d.toUpperCase()) {
				case "PHASE":
					phase = new char[5][4];
					int r = 0;
					while( r<5 && (line=br.readLine())!=null ) {
						if(line.replaceAll("#", "").
								trim().length()==0 || 
								line.startsWith("#%"))
							continue;
						line = line.replaceAll("#", "");
						s = line.split("");
						for(int i=0; i<phase[r].length; i++)
							phase[r][i] = s[i].charAt(0);
						r++;
					}
					check_phase = r==5;
					break;
				case "PIECE":
					piece = new char[8];
					r = 0;
					while( r<1 && (line=br.readLine())!=null ) {
						if(line.replaceAll("#", "").
								trim().length()==0 ||
								line.startsWith("#%"))
							continue;
						line = line.replaceAll(" ", "");
						line = line.replaceAll(",", "");
						line = line.replaceAll(";", "");
						s = line.split("");
						for(int i=0; i<piece.length; i++)
							piece[i] = s[i].charAt(0);
						r++;
					}
					check_piece = r==1;
					break;
				}
			}
		}
		br.close();
		if(!check_piece) piece = new char[]{'o','c','1','2','3','4','5','z'};
		if(!check_phase) phase = new char[][] {
				{'1','c','c','2'},
				{'1','c','c','2'},
				{'3','z','z','o'},
				{'3','4','4','o'},
				{'z','5','5','z'}};
		return new Klotski(phase, piece);
	}

	public void search() {
		int level = 1;
		boolean br = false;
		while(true) {
			for(Node node : new HashSet<Node>(this.all)) {
				if(node.level()==(level-1)) {
					Klotski[] moves = node.klotski.move();
					for(int i=0; i<moves.length; i++) {
						if(moves[i].target()) 
							br = true;
						this.all.add(new Node(moves[i], node, 
								false, level));
					}
				}
			}
			//System.err.println(level+"\t"+this.all.size());
			level++;
			if(br) return;
		}
	}

	private class Node {

		private final Klotski klotski;
		private final Node trace;
		private final boolean root;
		private final int level;

		public Node(Klotski klotski,
				Node trace,
				boolean root,
				int level) {
			this.klotski = klotski;
			this.trace = trace;
			this.root = root;
			this.level = level;
		}


		public int hashCode() {
			return new HashCodeBuilder(17, 31). 
					// two randomly chosen prime numbers
					// if deriving: appendSuper(super.hashCode()).
					append(this.klotski.getStrPhase()).
					toHashCode();
		}

		public boolean equals(Object obj) {

			if (!(obj instanceof Node))
				return false;
			if (obj == this)
				return true;
			return new EqualsBuilder().append(
					this.klotski.getStrPhase(),
					((Node) obj).klotski.getStrPhase()).
					isEquals();
		}

		public Klotski klotski() {
			return this.klotski;
		}

		public Node trace() {
			return this.trace;
		}

		public boolean root() {
			return this.root;
		}

		public int level() {
			return this.level;
		}
	}
}
