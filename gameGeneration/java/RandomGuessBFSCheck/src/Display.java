
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import Common.Direction;
import GameDisplay.GameDisplayPanel;

public class Display extends JFrame {
	
	private GameDisplayPanel displayPanel;
	private static final long serialVersionUID = 1L;
	public Display() throws IOException{
		this.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                //if(listeningForInput){
                    Direction dir=null;
                    if(e.getKeyCode()==KeyEvent.VK_LEFT) dir=Direction.LEFT;
                    if(e.getKeyCode()==KeyEvent.VK_RIGHT) dir=Direction.RIGHT;
                    if(e.getKeyCode()==KeyEvent.VK_UP) dir=Direction.UP;
                    if(e.getKeyCode()==KeyEvent.VK_DOWN) dir=Direction.DOWN;
                    if(e.getKeyCode()==KeyEvent.VK_N) try{displayPanel.newGame();}catch(Exception ex){}
                    if(e.getKeyCode()==KeyEvent.VK_S) try{displayPanel.solveGame();}catch(Exception ex){}
                    displayPanel.move(dir);
                //}
            }
            public void keyReleased(KeyEvent e) {}
		});
		displayPanel=new GameDisplayPanel(this);
		displayPanel.newGame();
		this.add(displayPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	public static void main(String[] args) throws IOException{
		new Display();
	}
}
