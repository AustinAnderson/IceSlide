//int xPos
//int yPos
//string tileType
function Tile(xPos,yPos,tileType){
    var x=xPos;
    var y=yPos;
    var id="t"+x+y;
    var type=tileType;
    var renderer=new TileType();
    var passable=null
    if(type===TileType.ICE){
        passable=true;
    }
    this.render=function(){
        //if element exists delete it
        if(type==TileType.ICE){
            renderer.getIce().render(x,y);
        }else if(type==TileType.ROCK){
            renderer.getRock().render(x,y);
        }
    }
    this.isPassable=function(){
        return passable;
    }
}
