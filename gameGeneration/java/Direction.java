
public class Direction {
    private char displayChar;
    private Direction(char display){
        displayChar=display;
    }
    public static final Direction UP=new Direction('^');
    public static final Direction DOWN=new Direction('v');
    public static final Direction LEFT=new Direction('<');
    public static final Direction RIGHT=new Direction('>');
    public static Direction random(){
        int value=(((int)(Math.random()*100)%4));
        Direction toReturn=UP;
        if(value==1) toReturn=DOWN;
        if(value==2) toReturn=LEFT;
        if(value==3) toReturn=RIGHT;
        return toReturn;
    }
    public char getChar(){
    	return displayChar;
    }
    @Override
    public String toString(){
    	return ""+displayChar;
    }
}
