package cz1.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class Klotski implements Serializable {

	private final char[] piece;
	private final char[][] phase;
	private final String str_phase;
	private final int[][] empty;
	private final int[][] cao;
	private final int[][] _1;
	private final int[][] _2;
	private final int[][] _3;
	private final int[][] _4;
	private final int[][] _5;
	private final int[][] zu;
	//private final Klotski[] move;
	private final boolean target;
	
	public static void main(String[] args) {
		/**
		Klotski klotski = new Klotski(new char[][] {
				{'1','c','c','2'},
				{'1','c','c','2'},
				{'z','3','3','z'},
				{'z','4','4','z'},
				{'o','5','5','o'}},
				new char[]{'o','c','1','2','3','4','5','z'});	
		klotski.print();
		 **/

		Klotski klotski = new Klotski(new char[][] {
				{'1','c','c','2'},
				{'1','c','c','2'},
				{'3','z','z','o'},
				{'3','4','4','o'},
				{'z','5','5','z'}},
				new char[]{'o','c','1','2','3','4','5','z'});	
		klotski.print();

		Klotski[] move = klotski.move();
		for(int i=0; i<move.length; i++)
			move[i].print();
	}

	public Klotski(char[][] phase, char[] piece) {
		this.phase = phase;
		this.piece = piece;
		this.str_phase = this.stringPhase();
		this.check();
		this.empty = this.position(0, 2);
		this.cao = this.position(1, 4);
		this._1 = this.position(2, 2);
		this._2 = this.position(3, 2);
		this._3 = this.position(4, 2);
		this._4 = this.position(5, 2);
		this._5 = this.position(6, 2);
		this.zu = this.position(7, 4);
		this.target = this.target();
	}
	
	public Klotski(char[][] phase, 
			char[] piece,
			String str_phase,
			int[][] empty,
			int[][] cao,
			int[][] _1,
			int[][] _2,
			int[][] _3,
			int[][] _4,
			int[][] _5,
			int[][] zu) {
		this.phase = phase;
		this.piece = piece;
		this.str_phase = str_phase;
		this.empty = empty;
		this.cao = cao;
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
		this._4 = _4;
		this._5 = _5;
		this.zu = zu;
		this.target = this.target();
	}

	private int[][] position(int piece, int n) {
		// TODO Auto-generated method stub
		char z = this.piece[piece];
		int[][] position = new int[n][2];
		int k = 0;
		for(int i=0; i<this.phase.length; i++)
			for(int j=0; j<this.phase[i].length; j++)
				if(this.phase[i][j]==z) {
					if( k>(position.length-1) ) 
						this.error();
					position[k] = new int[]{i, j};
					k++;
				}
		this.check(position, piece);
		return position;
	}


	private char[] piece() {
		// TODO Auto-generated method stub
		Set<Character> p = new HashSet<Character>();
		for(int i=0; i<this.phase.length; i++)
			for(int j=0; j<this.phase[i].length; j++)
				p.add(phase[i][j]);
		return ArrayUtils.toPrimitive(
				p.toArray(new Character[p.size()]));
	}

	public int hashCode() {

		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(this.str_phase).
				toHashCode();
	}

	public boolean equals(Object obj) {

		if (!(obj instanceof Klotski))
			return false;
		if (obj == this)
			return true;
		return new EqualsBuilder().append(this.str_phase,
				((Klotski) obj).str_phase).isEquals();
	}

	public String stringPhase() {
		String p = "";
		for(int i=0; i<this.phase.length; i++)
			p += String.valueOf(this.phase[i]);
		return p;
	}

	public void error() {
		System.err.println("Error! Invalid Phase or Piece...");
		System.err.println();
		this.print(true);
		System.exit(1);
	}
	
	public void check() {
		boolean b = false; 
		if(this.piece.length!=8) b = true;
		if(this.phase.length!=5) b = true;
		if(this.phase[0].length!=4) b = true;
		if(b) this.error();
		return;
	}

	private void check(int[][] position, int piece) {
		// TODO Auto-generated method stub
		switch(piece) {
		case 0:
			if( repeat(position) )
				this.error();
			break;
		case 1:
			if( repeat(position) )
				this.error();
			if( !rectangle(position) )
				this.error();
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			if( repeat(position) )
				this.error();
			if( !rectangle(position) )
				this.error();
			break;
		case 7:
			if( repeat(position) )
				this.error();
			break;
		default:
			this.error();
		}
	}

	private boolean repeat(int[][] position) {
		// TODO Auto-generated method stub
		for(int i=0; i<position.length; i++)
			for(int j=(i+1); j<position.length; j++)
				if(position[i][0]==position[j][0]
						&& position[i][1]==position[j][1])
					return true;
		return false;
	}

	private boolean rectangle(int[][] position) {
		// TODO Auto-generated method stub
		int[][] t = transpose(position);
		int x = max(t[0])-min(t[0]),
				y = max(t[1])-min(t[1]);
		if(position.length==2) return (x+y)==1;
		if(position.length==4) return x==1 && y==1;
		return false;
	}



	private int min(int[] is) {
		// TODO Auto-generated method stub
		int m = Integer.MAX_VALUE;
		for(int i=0; i<is.length; i++)
			if(is[i]<m)
				m = is[i];
		return m;
	}

	private int min(int[][] is) {
		// TODO Auto-generated method stub
		int m = Integer.MAX_VALUE, t;
		for(int i=0; i<is.length; i++)
			if( (t=min(is[i]))<m )
				m = t;
		return m;
	}

	private int max(int[] is) {
		// TODO Auto-generated method stub
		int m = Integer.MIN_VALUE;
		for(int i=0; i<is.length; i++)
			if(is[i]>m)
				m = is[i];
		return m;
	}

	private int[][] transpose(int[][] mat) {
		// TODO Auto-generated method stub
		int a = mat.length, b = mat[0].length;
		int[][] t = new int[b][a];
		for(int i=0; i<a; i++)
			for(int j=0; j<b; j++)
				t[j][i] = mat[i][j];
		return t;
	}

	public Klotski[] move() {
		Set<Klotski> next = new HashSet<Klotski>();
		int[][] empty = this.empty;
		Klotski k;

		for(int i=0; i<empty.length; i++) {
			int x = empty[i][0],
					y = empty[i][1];
			if( ( k=move(new int[][]{{x, y-1}}, new int[][]{{x, y}}, "east") )!=null )
				next.add(k);
			if( ( k=move(new int[][]{{x, y+1}}, new int[][]{{x, y}}, "west") )!=null )
				next.add(k);
			if( ( k=move(new int[][]{{x-1, y}}, new int[][]{{x, y}}, "south") )!=null )
				next.add(k);
			if( ( k=move(new int[][]{{x+1, y}}, new int[][]{{x, y}}, "north") )!=null )
				next.add(k);
		}

		if( (empty[1][0]-empty[0][0])==1 &&
				empty[0][1]==empty[1][1]) {
			int x = empty[0][0],
					y = empty[0][1];
			if( ( k=move(new int[][]{{x, y-1}, {x+1, y-1}}, empty, "east") )!=null )
				next.add(k);
			if( ( k=move(new int[][]{{x, y+1}, {x+1, y+1}}, empty, "west") )!=null )
				next.add(k);
		}

		if( empty[0][0]==empty[1][0] &&
				(empty[1][1]-empty[0][1])==1 ) {
			int x = empty[0][0],
					y = empty[0][1];
			if( ( k=move(new int[][]{{x-1, y}, {x-1, y+1}}, empty, "south") )!=null )
				next.add(k);
			if( ( k=move(new int[][]{{x+1, y}, {x+1, y+1}}, empty, "north") )!=null )
				next.add(k);
		}

		return next.size()==0 ? null : 
			next.toArray(new Klotski[next.size()]);
	}

	private Klotski move(int[][] position, int[][] fill, String direction) {
		// TODO Auto-generated method stub
		if(outbound(position)) return null;
		char[] piece = new char[position.length];
		for(int i=0; i<piece.length; i++)
			piece[i] = this.phase[position[i][0]][position[i][1]];
		if(piece.length==1) {
			if(piece[0]==this.piece[1] || 
					piece[0]==this.piece[0]) return null;
			if(piece[0]==this.piece[7]) {
				char[][] phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = this.piece[7];
				phase[position[0][0]][position[0][1]] = this.piece[0];
				return new Klotski(phase, this.piece);
			}
			int f;
			char[][] phase;
			switch(direction) {
			case "south":
				f = position[0][0]-1;
				if(f<0 || this.phase[f][position[0][1]]
						!=piece[0] ) return null;
				phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = piece[0];
				phase[f][position[0][1]] = this.piece[0];
				return new Klotski(phase, this.piece);
			case "north":
				f = position[0][0]+1;
				if(f>4 || this.phase[f][position[0][1]]
						!=piece[0] ) return null;
				phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = piece[0];
				phase[f][position[0][1]] = this.piece[0];
				return new Klotski(phase, this.piece);
			case "east":
				f = position[0][1]-1;
				if(f<0 || this.phase[position[0][0]][f]
						!=piece[0] ) return null;
				phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = piece[0];
				phase[position[0][0]][f] = this.piece[0];
				return new Klotski(phase, this.piece);
			case "west":
				f = position[0][1]+1;
				if(f>3 || this.phase[position[0][0]][f]
						!=piece[0] ) return null;
				phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = piece[0];
				phase[position[0][0]][f] = this.piece[0];
				return new Klotski(phase, this.piece);
			}
		} else if(piece.length==2) {

			if(piece[0]!=piece[1]) return null;
			int f;
			char[][] phase;
			if(piece[0]==this.piece[7]) return null;
			if(piece[0]!=this.piece[1]) {
				phase = deepCopy(this.phase);
				phase[fill[0][0]][fill[0][1]] = piece[0];
				phase[fill[1][0]][fill[1][1]] = piece[0];
				phase[position[0][0]][position[0][1]] = this.piece[0];
				phase[position[1][0]][position[1][1]] = this.piece[0];
				return new Klotski(phase, this.piece);
			} else {
				switch(direction) {
				case "south":
					f = position[0][0]-1;
					if(f<0 || this.phase[f][position[0][1]]
							!=piece[0] ) return null;
					phase = deepCopy(this.phase);
					phase[fill[0][0]][fill[0][1]] = piece[0];
					phase[fill[1][0]][fill[1][1]] = piece[0];
					phase[f][position[0][1]] = this.piece[0];
					phase[f][position[1][1]] = this.piece[0];
					return new Klotski(phase, this.piece);
				case "north":
					f = position[0][0]+1;
					if(f>4 || this.phase[f][position[0][1]]
							!=piece[0] ) return null;
					phase = deepCopy(this.phase);
					phase[fill[0][0]][fill[0][1]] = piece[0];
					phase[fill[1][0]][fill[1][1]] = piece[0];
					phase[f][position[0][1]] = this.piece[0];
					phase[f][position[1][1]] = this.piece[0];
					return new Klotski(phase, this.piece);
				case "east":
					f = position[0][1]-1;
					if(f<0 || this.phase[position[0][0]][f]
							!=piece[0] ) return null;
					phase = deepCopy(this.phase);
					phase[fill[0][0]][fill[0][1]] = piece[0];
					phase[fill[1][0]][fill[1][1]] = piece[0];
					phase[position[0][0]][f] = this.piece[0];
					phase[position[1][0]][f] = this.piece[0];
					return new Klotski(phase, this.piece);
				case "west":
					f = position[0][1]+1;
					if(f>3 || this.phase[position[0][0]][f]
							!=piece[0] ) return null;
					phase = deepCopy(this.phase);
					phase[fill[0][0]][fill[0][1]] = piece[0];
					phase[fill[1][0]][fill[1][1]] = piece[0];
					phase[position[0][0]][f] = this.piece[0];
					phase[position[1][0]][f] = this.piece[0];
					return new Klotski(phase, this.piece);
				}
			}
		}
		return null;
	}

	public static char[][] deepCopy(char[][] original) {
		if (original == null) {
			return null;
		}

		final char[][] copy = new char[original.length][];
		for (int i = 0; i < original.length; i++) 
			copy[i] = Arrays.copyOf(original[i], original[i].length);
		return copy;
	}

	private boolean outbound(int[][] position) {
		// TODO Auto-generated method stub
		int[][] tmp = transpose(position);
		int maxx = max(tmp[0]), 
				minx = min(tmp[0]),
				maxy = max(tmp[1]), 
				miny = min(tmp[1]);
		return(minx<0 || miny<0 || maxx>4 || maxy>3);
	}


	public void print() {
		// TODO Auto-generated method stub
		this.print(false);
	}

	public void print(boolean b) {
		if(b) System.err.println("##phase");
		for(int i=0; i<6; i++) System.err.print("#");
		System.err.println();
		for(int i=0; i<this.phase.length; i++) {
			System.err.print("#");
			for(int j=0; j<this.phase[i].length; j++) {
				System.err.print(this.phase[i][j]);
			}
			System.err.println("#");
		}
		for(int i=0; i<6; i++) System.err.print("#");
		System.err.println("\n");
		if(b) {
			System.err.println("##piece");
			for(int i=0; i<this.piece.length; i++)
				System.err.print(this.piece[i]);
			System.err.println();
		}
	}
	
	public void write(BufferedWriter bw) throws IOException {
		for(int i=0; i<6; i++) bw.write("#");
		bw.write("\n");
		for(int i=0; i<this.phase.length; i++) {
			bw.write("#");
			for(int j=0; j<this.phase[i].length; j++) {
				bw.write(this.phase[i][j]);
			}
			bw.write("#\n");
		}
		for(int i=0; i<6; i++) bw.write("#");
		bw.write("\n");
	}
	
	public boolean target() {
		char c = this.piece[1];
		boolean b = true;
		for(int i=3; i<5; i++)
			for(int j=1; j<3; j++)
				b = b&&this.phase[i][j]==c;
		return b;
	}

	public String getStrPhase() {
		// TODO Auto-generated method stub
		return this.str_phase;
	}
}
