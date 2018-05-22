package Common;

public class Direction {
	public static final Direction UP=new Direction('^',-1,0);
	public static final Direction DOWN=new Direction('v',1,0);
	public static final Direction LEFT=new Direction('<',0,-1);
	public static final Direction RIGHT=new Direction('>',0,1);
	public static Direction parse(char dirChar){
		Direction dir=Direction.UP;
		if(dirChar==DOWN.disp) dir=Direction.DOWN;
		if(dirChar==LEFT.disp) dir=Direction.LEFT;
		if(dirChar==RIGHT.disp) dir=Direction.RIGHT;
		return dir;
	}
	@Override
	public String toString(){
		return ""+disp;
	}
	private final char disp;
	private final int modR;
	private final int modC;
	private Direction(char disp,int modR,int modC){
		this.disp=disp;
		this.modR=modR;
		this.modC=modC;
	}
	public Direction next(){
		Direction next=null;
             if(this==   Direction.UP){ next=Direction.DOWN;}
        else if(this== Direction.DOWN){ next=Direction.LEFT;}
        else if(this== Direction.LEFT){next=Direction.RIGHT;}
        return next;
	}
	public int getModR(){return modR;}
	public int getModC(){return modC;}
}
