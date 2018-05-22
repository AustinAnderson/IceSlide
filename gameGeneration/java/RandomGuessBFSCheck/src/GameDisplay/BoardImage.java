package GameDisplay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardImage extends JPanel{
    private static final long serialVersionUID = 1L;
    private final BufferedImage Ice;
    private final BufferedImage Rock;
    private boolean[][] rockMap;
    private Player player=null;
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
    public void setPlayer(Player player){
        this.player=player;
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
        if(player!=null){
            player.paintComponent(g);
        }
    }
}


