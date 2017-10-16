
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

public class Ting {
	class Tile{
		public Tile(){
			setToWall();
		}
		@Override
		public String toString(){
			return ""+displayChar;
		}
		public boolean visited=false;
		public void setToWall(){
			displayChar='█';
		}
		public void setTo(char value){
			displayChar=value;
		}
		private char displayChar;
	}
	class Board{
		public Board(int rowsSub1Div2,int colsSub1Div2){
			rows=rowsSub1Div2*2+1;//ensure odd rows and cols
			cols=colsSub1Div2*2+1;
			mat=new Tile[rows][cols];
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					mat[r][c]=new Tile();
					if(r%2==1&&c%2==1){
						mat[r][c].setTo(' ');
					}
				}
			}
			
			start=new Position(Direction.DOWN,0,(((int)(Math.random()*colsSub1Div2*2))%colsSub1Div2)*2+1);
			start.getTile().setTo('s');
			start=start.halfStep(Direction.DOWN);
			end=new Position(Direction.DOWN,rows-1,(((int)(Math.random()*colsSub1Div2*2))%colsSub1Div2)*2+1);
			end.getTile().setTo('e');
			end=end.halfStep(Direction.UP);
		}
		public void print(PrintStream stream){
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					stream.print(mat[r][c]);
				}
				stream.println();
			}
		}
		public void generate(){
			LinkedList<Position> positionStack=new LinkedList<Position>();
			positionStack.push(start);
			while(!positionStack.isEmpty()){
				Position next=positionStack.getLast().next();
				if(next==null){
					positionStack.removeLast();
				}else{
					positionStack.add(next);
				}
			}
		}
		private Tile[][] mat;
		private final int rows;
		private final int cols;
		private Position start;
		private Position end;
		public class Position{
			private int r;
			private int c;
			public Position(Direction previousDirection,int r,int c){
				this.r=r;
				this.c=c;
				lastDirection=previousDirection;
			}
			public Position halfStep(Direction where){
				return move(where,1);
			}
			public Position next(){
				Position nextPosition=move(currentDirection,2);
				for(int i=0;i<3;i++){
					if(!nextPosition.isValid()){
						advanceCurrentDirection();
						nextPosition=move(currentDirection,2);
					}
				}
				if(!nextPosition.isValid()){
					nextPosition=null;
				}
				if(currentDirection!=lastDirection||nextPosition==null){
					halfStep(lastDirection).getTile().setTo('▒');
				}
				getTile().setTo(lastDirection.getChar());
				getTile().visited=true;
				return nextPosition;
			}
			public boolean isValid(){
				return r>=0&&r<rows &&
					   c>=0&&c<cols && !getTile().visited;
			}
			public Tile getTile(){
				return mat[r][c];
			}
			@Override
			public int hashCode(){
				return c^r;
			}
			@Override
			public boolean equals(Object other){
				boolean equal=false;
				if(other instanceof Position){
					Position otherP=(Position)other;
					equal=otherP.r==r&&otherP.c==c;
				}
				return equal;
			}
			private Position move(Direction where,int stepSize){
				int newr=r;
				int newc=c;
				if(   Direction.UP==where) newr-=stepSize;
				if( Direction.DOWN==where) newr+=stepSize;
				if( Direction.LEFT==where) newc-=stepSize;
				if(Direction.RIGHT==where) newc+=stepSize;
				return new Position(where,newr,newc);
			}
			private void advanceCurrentDirection(){
				     if(   Direction.UP==currentDirection) currentDirection=Direction.DOWN;
				else if( Direction.DOWN==currentDirection) currentDirection=Direction.LEFT;
				else if( Direction.LEFT==currentDirection) currentDirection=Direction.RIGHT;
				else if(Direction.RIGHT==currentDirection) currentDirection=Direction.UP;
			}
			private Direction lastDirection;
			private Direction currentDirection=Direction.random();
			
		}
	}
	public static void main(String[] args){
		Ting.Board board=new Ting().new Board(3,3);
		board.print(System.out);
		System.out.println();
		board.generate();
		board.print(System.out);
	}
}
