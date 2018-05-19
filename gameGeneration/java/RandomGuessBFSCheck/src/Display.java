
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JFrame {
	class Player {
		private BufferedImage currentSprite;
		private final static int sleepTime=3;
		private final Map<Direction,BufferedImage[]> spriteMap;
		private final int tileHeight;
		private final int tileWidth;
		private final JFrame parent;
		private AtomicBoolean moving;
		private int row;
		private int col;
		private AtomicInteger x;
		private AtomicInteger y;
		public Player(int initialRow,int initialCol,int tileHeight,int tileWidth,JFrame parent) throws IOException{
			this.parent=parent;
			this.moving=new AtomicBoolean(false);
			this.row=initialRow;
			this.col=initialCol;
			this.tileHeight=tileHeight;
			this.tileWidth=tileWidth;
			this.x=new AtomicInteger(initialCol*tileWidth);
			this.y=new AtomicInteger(initialRow*tileHeight);
			spriteMap=new HashMap<>();
			
			spriteMap.put(Direction.DOWN, new BufferedImage[]{
				ImageIO.read(new File("Resources/FairUse/downStep.png")),
				ImageIO.read(new File("Resources/FairUse/down.png"))
			});
			spriteMap.put(Direction.LEFT, new BufferedImage[]{
				ImageIO.read(new File("Resources/FairUse/leftStep.png")),
				ImageIO.read(new File("Resources/FairUse/left.png"))
			});
			spriteMap.put(Direction.RIGHT, new BufferedImage[]{
				ImageIO.read(new File("Resources/FairUse/rightStep.png")),
				ImageIO.read(new File("Resources/FairUse/right.png"))
			});
			spriteMap.put(Direction.UP, new BufferedImage[]{
				ImageIO.read(new File("Resources/FairUse/upStep.png")),
				ImageIO.read(new File("Resources/FairUse/up.png"))
			});
			currentSprite=spriteMap.get(Direction.DOWN)[1];
		}
		
		public Coordinate getPos(){return new Coordinate(row,col);}
		//return -1,0, or 1
		private int sign(int value){
			if(value==0) return 0;
			return (value>>31)|1;//arithmetic shift to fill everything with sign bit, then last number is 1
		}
		public void updatePosition(Coordinate target,Direction dir){
			if(!moving.get()){
				currentSprite=spriteMap.get(dir)[0];
                Thread t=new Thread(new Runnable(){
                    @Override
                    public void run(){
                        moving.set(true);
                        int dY=(target.r*tileHeight)-y.get();
                        int dX=(target.c*tileWidth)-x.get();
                        int incX=sign(dX);
                        int incY=sign(dY);
                        int targetDist=Math.abs(dY);
                        if(Math.abs(dX)>targetDist) targetDist=Math.abs(dX);
                        
                        if(targetDist<tileWidth){
                        	for(int i=0;i<tileHeight*2;i++){
                                try {Thread.sleep(sleepTime);} catch (InterruptedException e) {}
                                parent.repaint();
                                if(i==tileHeight-1) currentSprite=spriteMap.get(dir)[1];
                        	}
                        }
                        else{
                            for(int i=0;i<targetDist;i++){
                                if(i==(tileHeight-1)){
                                    currentSprite=spriteMap.get(dir)[1];
                                }
                                y.addAndGet(incY);
                                x.addAndGet(incX);
                                try {Thread.sleep(sleepTime);} catch (InterruptedException e) {}
                                parent.repaint();
                            }
                        }
                        row=target.r;
                        col=target.c;
                        moving.set(false);
                    }
                });
                t.start();
			}
		}
		public void paintComponent(Graphics g){
			g.drawImage(currentSprite, x.get(),y.get(),null);
		}
	}
	class BoardImage extends JPanel{
		private static final long serialVersionUID = 1L;
		private final BufferedImage Ice;
		private final BufferedImage Rock;
		private boolean[][] rockMap;
		private Player smiley=null;
		public BoardImage() throws IOException{
			try{
				Ice=ImageIO.read(new File("Resources/Ice.png"));
				Rock=ImageIO.read(new File("Resources/Rock.png"));
			}catch(MalformedURLException ex){
				throw new IOException("unable to init images",ex);
			}
            if(!(Ice.getHeight()==Rock.getHeight()&&Ice.getWidth()==Rock.getWidth())){
            	throw new IOException("rock and ice images must be the same dimensions");
            }
		}
		public void setSmile(Player smile){
			smiley=smile;
		}
		private Image getImg(boolean rock){
			if(rock) return Rock;
			return Ice;
		}
		public int getWidth(){ return Rock.getWidth()*rockMap[0].length;}
		public int getHeight(){ return Rock.getHeight()*rockMap.length;}
		public int getTileWidth(){ return Rock.getWidth();}
		public int getTileHeight(){ return Rock.getHeight();}
		public void update(boolean[][] rockMap){
			this.rockMap=rockMap;
		}
		@Override
		public void paintComponent(Graphics g){
			for(int r=0;r<rockMap.length;r++){
				for(int c=0;c<rockMap[0].length;c++){
					g.drawImage(getImg(rockMap[r][c]), c*Rock.getWidth(),r*Rock.getHeight(), null);
				}
			}
			if(smiley!=null){
				smiley.paintComponent(g);
			}
		}
	}
	private static final long serialVersionUID = 1L;
	public Display() throws IOException{
		Board board=new Board(15,20,20);
		int initCol=0;
		boolean[][] rocks=board.toBoolMap();
		for(int i=0;i<rocks[0].length;i++)
		{
			if(!rocks[0][i]) initCol=i;
		}
		BoardImage img=new BoardImage();
		
		img.update(board.toBoolMap());
		setLayout(new GridLayout(1,1));
		this.add(img);
		Player cursor=new Player(0,initCol,img.getTileHeight(),img.getTileWidth(),this);
		img.setSmile(cursor);
		this.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				Direction dir=null;
				if(e.getKeyCode()==KeyEvent.VK_LEFT) dir=Direction.LEFT;
				if(e.getKeyCode()==KeyEvent.VK_RIGHT) dir=Direction.RIGHT;
				if(e.getKeyCode()==KeyEvent.VK_UP) dir=Direction.UP;
				if(e.getKeyCode()==KeyEvent.VK_DOWN) dir=Direction.DOWN;
				if(dir!=null){
					cursor.updatePosition(board.getNextPosition(cursor.getPos(), dir),dir);
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(img.getWidth(),img.getHeight()+20));
		pack();
		setVisible(true);
	}
	public static void main(String[] args) throws IOException{
		new Display();
	}
}
