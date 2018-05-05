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
	private int getRandNonRockColNdx(int row){
		int[] ndxs=new int[width];
		int maxIndex=0;
		for(int i=0;i<width;i++){
			if(!board[row][i].rock){
				ndxs[maxIndex]=i;
				maxIndex++;
			}
		}
		return ndxs[((int)(Math.random()*maxIndex))%maxIndex];
	}
	private Position start;
	private Position end;
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
	public Board(int height,int width,int minMoves){
		if(height<3) height=3;
		int solutionLength=0;
        this.height=height;
        this.width=width;
		while(solutionLength<minMoves){
            board=new Position[height][width];
            for(int r=0;r<height;r++){
                for(int c=0;c<width;c++){
                    boolean rock=true;
                    if(r!=0&&r!=height-1&&c!=0&&c!=width-1) rock=randRock();
                    board[r][c]=new Position(r,c,rock);
                }
            }
            
            
            start=board[0][getRandNonRockColNdx(1)];
            int endCol=getRandNonRockColNdx(height-2);
            board[height-1][endCol]=new Position(height-1,endCol,false);
            end=board[height-1][endCol];
            String result=solve();
            if(result!=null) solutionLength=result.length();
		}
	}
	private Position[][] board;
	private class Position{
		private final static char UP='^',DOWN='v',LEFT='<',RIGHT='>',DONE='e';
		public final static int NumStates=5;
		public Position(int r,int c,boolean isRock){
			this.r=r;
			this.c=c;
			this.rock=isRock;
		}
		public final int r;
		public final int c;
		public final boolean rock;
		private char state=UP;
		private String path="";
		public String getPath(){
			return path;
		}
		private boolean visited=false;
		public void setVisited(){
			visited=true;
		}
		private boolean isOutOfBoundsOrRock(int r,int c){
            return r<0||r>=board.length||
                   c<0||c>=board[0].length||
                   board[r][c].rock;
		}
		public Position next(){
			Position toReturn=null;
			if(state!=DONE){
                int otherR=r;
                int otherC=c;
                int modr=0;
                int modc=0;
                char direction=state; 
                
                     if(state==   UP){ state=DOWN;modr=-1;}
                else if(state== DOWN){ state=LEFT;modr= 1;}
                else if(state== LEFT){state=RIGHT;modc=-1;}
                else if(state==RIGHT){ state=DONE;modc= 1;}
                while(!isOutOfBoundsOrRock(otherR+modr,otherC+modc)&&board[otherR][otherC]!=end){
                    otherR+=modr;
                    otherC+=modc;
                }
                if(!board[otherR][otherC].visited){
                    toReturn=board[otherR][otherC];
                    toReturn.path=this.path+direction;
                }
			}
			return toReturn;
		}
		public void print(PrintStream printer) {
			           char out='.';
			if(rock)        out='O';
			if(this==start) out='s';
			if(this==end)   out='e';
			printer.print(out);
		}
	}
	public void print(PrintStream printer){
	    printer.print(' ');
	    for(int c=0;c<width;c++){
	        printer.print(Integer.toHexString(c));
	    }
	    printer.println();
		for(int r=0;r<height;r++){
	        printer.print(Integer.toHexString(r));
			for(int c=0;c<width;c++){
				board[r][c].print(printer);
			}
			printer.println();
		}
	}
	public String solve(){
		Queue<Position> queue=new LinkedList<Position>();
		queue.add(start);
		start.setVisited();
		Position head=null;
		while(head!=end&&!queue.isEmpty()){
			head=queue.remove();
			for(int i=0;i<Position.NumStates;i++){
				Position next=head.next();
				if(next!=null){
					queue.add(next);
					next.setVisited();
				}
			}
		}
		String solvePath=null;
		if(head==end){
			solvePath=head.getPath();
		}
		return solvePath;
	}
	public static void main(String[] args){
		final boolean O=true;
		final boolean _=false;
		final boolean $=false;
		/*
		Board b1=new Board(new boolean[][]{
			{O,$,O,O,O,O,O,O,O,O,O,O,O,O},
			{O,$,$,$,$,$,$,$,$,$,$,O,_,O},
			{O,$,$,O,_,_,_,_,_,_,$,_,_,O},
			{O,O,_,_,_,O,$,$,$,$,$,_,_,O},
			{O,_,_,_,_,_,$,_,_,_,O,_,_,O},
			{O,_,_,_,_,_,$,$,$,$,$,$,O,O},
			{O,_,_,_,_,_,O,_,_,_,_,$,_,O},
			{O,O,O,O,O,O,O,O,O,O,O,$,O,O},
		});
		Board b2=new Board(new boolean[][]{
			{O,O,O,O,O,O,O,O,O,O,O,O,$,O,O},
			{O,_,_,_,_,_,_,_,_,_,_,_,$,_,O},
			{O,_,_,_,O,$,$,$,$,$,$,$,$,_,O},
			{O,_,_,_,_,$,_,_,_,_,_,_,O,_,O},
			{O,_,_,O,_,$,_,_,_,_,_,_,_,_,O},
			{O,_,_,$,$,$,$,$,$,$,$,O,_,_,O},
			{O,_,_,$,_,$,_,_,_,_,$,_,_,_,O},
			{O,_,O,$,$,$,O,$,$,$,$,_,_,_,O},
			{O,_,_,_,_,O,_,$,_,_,O,_,_,_,O},
			{O,O,O,O,O,O,O,$,O,O,O,O,O,O,O},
		});
		Board b3=new Board(new boolean[][]{
			{O,O,O,_,O},
			{O,_,_,_,O},
			{O,O,_,_,O},
			{O,O,_,_,O},
			{O,_,O,O,O},
		});  
		Board b4=new Board(new boolean[][]{
			{O,O,O,$,O,O,O,O,O,O},
			{O,_,O,_,_,_,_,_,_,O},
			{O,_,_,O,_,_,_,O,_,O},
			{O,O,_,_,_,O,_,_,_,O},
			{O,O,O,O,O,O,$,O,O,O}
		});
		*/
		//b2.print(System.out);
		//System.out.println(b2.solve());
		//b1.print(System.out);
		//System.out.println(b1.solve());
		new Board(10,10,8).print(System.out);
		/*
		
		failed on this one
0123456789
0OOOOOOsOOO
1O........O
2O...O....O
3OO...O..OO
4OO...O...O
5O...O....O
6O.......OO
7O..OO..O.O
8O...OO...O
9OeOOOOOOOO
		*/
	}
}
