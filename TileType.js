function TileType(){
    this.ICE="IceType";
    this.ROCK="RockType";
    var Ice=new IceTileRenderer();
    var Rock=new RockTileRenderer();
    this.getIce=function(){
        return Ice;
    }
    this.getRock=function(){
        return Rock;
    }
}
