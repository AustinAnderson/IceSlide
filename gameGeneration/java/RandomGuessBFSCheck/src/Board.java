import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

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
		private final static char UP='^',DOWN='v',LEFT='<',RIGHT='>',DONE='e';
		public final static int StateCount=4;
		public Position(int r,int c,boolean isRock){
			this.r=r;
			this.c=c;
			this.rock=isRock;
		}
		public String getPath(){
			return path;
		}
		public final int r;
		public final int c;
		public final boolean rock;
		private boolean visited=false;
		private String path="";
		private char state=UP;
		public Position next(){
			Position toReturn=null;
			0nji9
			
			if(state!=DONE){
                int otherR=r;
                int otherC=c;
                int modr=0;
                int modc=0;
                char direction=state;
                     if(state==   UP){state=LEFT;  modr=-1;}
                else if(state== LEFT){state=DOWN;  modc=-1;}
                else if(state== DOWN){state=RIGHT; modr=1; }
                else if(state==RIGHT){state=DONE;  modc=1; }
                while(!board[otherR+modr][otherC+modc].rock&&board[otherR][otherC]!=end){
                    otherR+=modr;
                    otherC+=modr;
                }
                toReturn=board[otherR][otherC];
                if(toReturn.visited){
                	toReturn=null;
                }else{
                	toReturn.visited=true;
                	toReturn.path=this.path+direction;
                }
			}
			
			return toReturn;
		}
		public void print(PrintStream printer) {
			           char out='+';
			if(rock)        out='@';
			if(this==start) out='s';
			if(this==end)   out='e';
			printer.print(out);
		}
		@Override
		public String toString(){
			return String.format("{(%d,%d) %s, %s}",r,c,state,path);
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
	public String solve(){
		Queue<Position> queue=new LinkedList<Position>();
		queue.add(start);
		while(!queue.isEmpty()&&queue.peek()!=end){
			Position head=queue.remove();
			for(int i=0;i<Position.StateCount;i++){
				Position next=head.next();
				if(next!=null) {
					queue.add(next);
				}
			}
		}
		String result=null;
		if(!queue.isEmpty()) result=queue.poll().getPath();//if !empty then should be end position
		return result;
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
		Board b1=new Board(new boolean[][]{//v>^>v<v>v
			{XX,O ,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
			{XX,O , o,oo,oo,oo,oo,oo,oo,oo,oo,XX,__,XX},
			{XX,Oo,oO,XX,__,__,__,__,__,__, O,__,__,XX},
			{XX,XX,__,__,__,XX,oo,oo,oo,oo,oO,__,__,XX},
			{XX,__,__,__,__,__,O ,__,__,__,XX,__,__,XX},
			{XX,__,__,__,__,__,Oo,oo,oo,oo,oo,oo,XX,XX},
			{XX,__,__,__,__,__,XX,__,__,__,__, O,__,XX},
			{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX, O,XX,XX},
		});
		Board b2=new Board(new boolean[][]{//v<v<^>v<v
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
		System.out.println(b2.solve());
	}
}
