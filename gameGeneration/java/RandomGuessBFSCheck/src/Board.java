import java.io.PrintStream;
import java.util.LinkedList;

public class Board {

	private final int height;
	private final int width;
	private final static int iceOdds=8;
	private boolean randRock(){
		//one in iceOdds chance of being rock
		return ((int)(Math.random()*iceOdds))%iceOdds==0;
	}
	private final Position start;
	private final Position end;
	public Board(boolean[][] toSolve){
		height=toSolve.length;
		width=toSolve[0].length;
		board=new Position[height][width];
		Position startValue=null;
		Position endValue=null;
		for(int r=0;r<height;r++){
			for(int c=0;c<width;c++){
				board[r][c]=new Position(r,c,toSolve[r][c]);
				if(r==    0   &&!toSolve[r][c]) startValue=board[r][c];
				if(r==height-1&&!toSolve[r][c]) endValue  =board[r][c];
			}
		}
		start=startValue;
		end=endValue;
	}
	public Board(int height,int width){
		this.height=height;
		this.width=width;
		board=new Position[height][width];
		for(int r=0;r<height;r++){
			for(int c=0;c<width;c++){
				boolean rock=true;
				if(r!=0&&r!=height-1&&c!=0&&c!=width-1) rock=randRock();
				board[r][c]=new Position(r,c,rock);
			}
		}
		start=null;
		end=null;
	}
	private Position[][] board;
	private class Position{
		public final static int UP=0,DOWN=1,LEFT=2,RIGHT=3,DONE=4;
		public Position(int r,int c,boolean isRock){
			this.r=r;
			this.c=c;
			this.rock=isRock;
			//assumes start is at top and UP is first num to avoid check
			if(this==start) state=DOWN;
		}
		public final int r;
		public final int c;
		public final boolean rock;
		private int state=UP;
		public Position next(){
			Position toReturn=null;
			if(state!=DONE){
                int otherR=r;
                int otherC=c;
                int modr=-1;
                int modc=-1;
                
                if(state==  UP||state== DOWN) modc=0;
                if(state==LEFT||state==RIGHT) modr=0;
                if(state== DOWN) modr=1;
                if(state==RIGHT) modc=1;
                while(!board[otherR+modr][otherC+modc].rock&&board[otherR][otherC]!=end){
                    otherR+=modr;
                    otherC+=modr;
                }
                state++;
                toReturn=board[otherR][otherC];
			}
			return toReturn;
		}
		public void print(PrintStream printer) {
			           char out='.';
			if(rock)        out='▒';
			if(this==start) out='s';
			if(this==end)   out='e';
			printer.print(out);
		}
	}
	public void print(PrintStream printer){
		for(int r=0;r<height;r++){
			for(int c=0;c<width;c++){
				board[r][c].print(printer);
			}
			printer.println();
		}
	}
	public boolean solve(){
		LinkedList<Position> queue=new LinkedList<Position>();
		queue.push(start);
		Position head=queue.peekFirst();
		while(head!=end&&!queue.isEmpty()){
			for(int i=0;i<Position.DONE;i++){
				Position next=head.next();
				if(next!=null) queue.add(next);
			}
			queue.removeFirst();
		}
		return head==end;
	}
	public static void main(String[] args){
		final boolean XX=true;
		final boolean __=false;
		final boolean  o=false;
		final boolean oo=false;
		final boolean oO=false;
		final boolean Oo=false;
		final boolean  O=false;
		Board unwinnable=new Board(new boolean[][]{
			{XX, O,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
			{XX, O, o,oo,oo,oo,oo,oo,oo,oo,oo,XX,__,XX},
			{XX, O,oO,XX,__,__,__,__,__,__, O,__,__,XX},
			{XX,XX,__,__,__,XX,oo,oo,oo,oo,oO,__,__,XX},
			{XX,__,__,__,__,__,O ,__,__,__,XX,__,__,XX},
			{XX,__,__,__,__,__,Oo,oo,oo,oo,oo,oo,XX,XX},
			{XX,__,__,__,__,__,XX,__,__,__,__,XX,__,XX},
			{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,__,XX,XX},
		});
		Board b1=new Board(new boolean[][]{
			{XX,O ,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
			{XX,O , o,oo,oo,oo,oo,oo,oo,oo,oo,XX,__,XX},
			{XX,Oo,oO,XX,__,__,__,__,__,__, O,__,__,XX},
			{XX,XX,__,__,__,XX,oo,oo,oo,oo,oO,__,__,XX},
			{XX,__,__,__,__,__,O ,__,__,__,XX,__,__,XX},
			{XX,__,__,__,__,__,Oo,oo,oo,oo,oo,oo,XX,XX},
			{XX,__,__,__,__,__,XX,__,__,__,__, O,__,XX},
			{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX, O,XX,XX},
		});
		Board b2=new Board(new boolean[][]{
			{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,O ,XX,XX},
			{XX,__,__,__,__,__,__,__,__,__,__,__,O ,__,XX},
			{XX,__,__,__,XX,oo,oo,oo,oo,oo,oo,oo,O ,__,XX},
			{XX,__,__,__,__,O ,__,__,__,__,__,__,XX,__,XX},
			{XX,__,__,XX,__,O ,__,__,__,__,__,__,__,__,XX},
			{XX,__,__,oo,oo,Oo,oo,oo,oo,oo,oo,XX,__,__,XX},
			{XX,__,__,O ,__,O ,__,__,__,__, O,__,__,__,XX},
			{XX,__,XX,Oo,oo,O ,XX,oo,oo,oo,oO,__,__,__,XX},
			{XX,__,__,__,__,XX,__,O ,__,__,XX,__,__,__,XX},
			{XX,XX,XX,XX,XX,XX,XX,O ,XX,XX,XX,XX,XX,XX,XX},
		});
		//b1.print(System.out);
		b2.print(System.out);
	}
}
