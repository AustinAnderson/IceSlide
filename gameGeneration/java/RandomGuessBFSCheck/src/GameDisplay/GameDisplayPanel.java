package GameDisplay;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import BackingLogic.Board;
import Common.Direction;

public class GameDisplayPanel extends JPanel{
	private Board board;
	private BoardImage img;
	private Player cursor;
	private boolean listeningForInput=true;
	private JFrame parent;
	public GameDisplayPanel(JFrame parent){
		this.parent=parent;
	}
	public void newGame() throws IOException{
		board=new Board(15,20,20);
		int initCol=0;
		boolean[][] rocks=board.toBoolMap();
		for(int i=0;i<rocks[0].length;i++)
		{
			if(!rocks[0][i]) initCol=i;
		}
		if(img!=null)this.remove(img);
		img=new BoardImage();
		img.update(board.toBoolMap());
		cursor=new Player(0,initCol,img.getTileHeight(),img.getTileWidth(),parent);
		img.setPlayer(cursor);
		this.add(img);
        this.repaint();
	}
	public void solveGame(){
		listeningForInput=false;
		String solution=board.solve(cursor.getPos());
		if(solution!=null){
			Queue<Direction> moves=new LinkedList<Direction>();
			for(int i=0;i<solution.length();i++){
				moves.add(Direction.parse(solution.charAt(i)));
			}
			new Thread(new AutoMoveTask(this,moves)).start();
		}
		listeningForInput=true;
	}
	public Thread move(Direction dir){
		Thread moveTask=null;
        if(dir!=null){
            moveTask=cursor.updatePosition(board.getNextPosition(cursor.getPos(), dir),dir);
        }
        return moveTask;
	}
	private class AutoMoveTask implements Runnable{
		private Queue<Direction> moves;
		private GameDisplayPanel disp;
		public AutoMoveTask(GameDisplayPanel disp,Queue<Direction> moves){
			this.moves=moves;
			this.disp=disp;
		}
		public void run(){
			while(!moves.isEmpty()){
                try {
                    disp.move(moves.poll()).join();
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
			}
		}
	}
}
