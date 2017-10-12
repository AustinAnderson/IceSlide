var printBoard=function(Array2d){
    console.log(
        Array2d.map(
            function(row){return row.join(",");}
        ).join("\n")
    );
};
var random=function(max){
    return Math.floor(Math.random()*max);
};
var setIndex=function(arr,indexTuple,value){
    arr[indexTuple.r][indexTuple.c]=value;
};
var board=[];
var height=20;
var width=10;
var slideCount=6;
for(var i=0;i<height;i++){
    var row=[];
    for(var j=0;j<width;j++){
        row.push(' ');
    }
    board.push(row);
}
    
var startPosition={ "r": 0, "c": random(width)};
var endPosition={ "r": height-1, "c": random(width)};
setIndex(board,startPosition,1);
setIndex(board,endPosition,1);

printBoard(board);

var slideStopPositions=[];
(function(){
    //randomly chooses between the two midpoints 
    //of the possible arrangements of the component vectors of pos2-pos1
    var chooseMidComponent=function(pos1,pos2){
        var midPos={"r":pos1.r,"c":pos2.c};
        if(random(2)==1){
            midPos.r=pos2.r;
            midPos.c=pos1.c;
        }
        return midPos;
    };
    //generate an array of random positions from start to finish
    slideStopPositions.push(startPosition);
    for(var i=0;i<slideCount/2;i++){
        slideStopPositions.push(
            {
                "r": random(height-1)+1,
                "c": random(width-1)+1
            }
        );
    }
    slideStopPositions.push(endPosition);

    //iterate through and put the midpoints in between each pair
    var targetLength=(slideStopPositions.length-1)*2;
    //can't use slideStopPositions.length because it's changeing
    //TODO: this approach wont work for the case where [i] and [i+1] are on the same col or on the same row
    for(var i=0;i<targetLength;i+=2){
        slideStopPositions.slice(
            i+1,
            0,
            chooseMidComponent(
                slideStopPositions[i],
                slideStopPositions[i+1],
            )
        );
    }
        
}())


//maybe 
//xxx
//xxx  goes to  x 
//xxx   
//
// before calculating random rocks
// then multiply everything back out.
//
// this ensures we never have two adjacent stop positions, which would make
// the rocks block the game from being possible


//TODO: figured it out! Ice slide is analogous to a normal maze, see scratch
