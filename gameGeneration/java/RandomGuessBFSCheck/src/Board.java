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
		if(height<4) height=4;
		if(width<4) width=4;
		if(height>64) height=64;
		if(width>64) height=64;
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
            
            
            int startCol=getRandNonRockColNdx(height-2);
            board[0][startCol]=new Position(0,startCol,false);
            start=board[0][startCol];
            int endCol=getRandNonRockColNdx(height-2);
            board[height-1][endCol]=new Position(height-1,endCol,false);
            end=board[height-1][endCol];
            String result=solve();
            if(result!=null) solutionLength=result.length();
		}
	}
	public boolean[][] toBoolMap(){
		boolean[][] map=new boolean[board.length][];
		for(int i=0;i<map.length;i++){
			map[i]=new boolean[board[i].length];
			for(int j=0;j<map[i].length;j++){
				map[i][j]=board[i][j].rock;
			}
		}
		return map;
	}
	private Position[][] board;
	private class Position{
		public Position(int r,int c,boolean isRock){
			this.r=r;
			this.c=c;
			this.rock=isRock;
		    resetIterationState();
		}
		public final int r;
		public final int c;
		public final boolean rock;
		private Direction state;
		private String path;
		private boolean visited;
		public String getPath(){
			return path;
		}
		public void resetIterationState(){
			visited=false;
			state=Direction.UP;
			path="";
		}


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
			if(state!=null){
                int otherR=r;
                int otherC=c;
                int modr=state.getModR();
                int modc=state.getModC();
                Direction direction=state; 
                state=state.next();
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
			           char out='_';
			if(rock)        out='O';
			if(this==start) out='s';
			if(this==end)   out='e';
			printer.print(out);
		}
	}
	public void print(PrintStream printer){
	    printer.print(" |");
	    for(int c=0;c<width;c++){
	        printer.print(Integer.toHexString(c)+"|");
	    }
	    printer.println();
		for(int r=0;r<height;r++){
	        printer.print(Integer.toHexString(r));
			for(int c=0;c<width;c++){
				printer.print("|");
				board[r][c].print(printer);
			}
			printer.println("|");
		}
	}
	public String solve(){
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[i].length;j++){
				board[i][j].resetIterationState();
			}
		}
		Queue<Position> queue=new LinkedList<Position>();
		queue.add(start);
		start.setVisited();
		Position head=null;
		while(head!=end&&!queue.isEmpty()){
			head=queue.remove();
			Direction iterateDirs=Direction.UP;
			while(iterateDirs!=null){
				Position next=head.next();
				if(next!=null){
					queue.add(next);
					next.setVisited();
				}
				iterateDirs=iterateDirs.next();
			}
		}
		String solvePath=null;
		if(head==end){
			solvePath=head.getPath();
		}
		return solvePath;
	}
	public Coordinate getNextPosition(Coordinate current, Direction dir){
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[i].length;j++){
				board[i][j].resetIterationState();
			}
		}
		Position start=new Position(current.r,current.c,false);
		start.state=dir;
		Position next=start.next();
		return new Coordinate(next.r,next.c);
	}
	public static void main(String[] args){
		Board b=new Board(14,20,20);
		b.print(System.out);
		System.out.println(b.solve());
	}
}
