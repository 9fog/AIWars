package main.Game.CombatData.CombatMap;

import java.util.ArrayList;
import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import main.Game.CombatData.MapObject;
import main.Game.CombatData.MapObjectRock;

import core.*;

public class CombatMap {
	private final int _sizeX, _sizeY;
	private MapObject[][] _objects;
	//private String _mapDump;
	private JSONArray _mapDump;
	
	public CombatMap(CombatMapLoader cml) {
		_sizeX = cml.sizeX;
		_sizeY = cml.sizeY;
		
		_objects = new MapObject[_sizeY][_sizeX];
		//_mapDump = "";
		_mapDump = new JSONArray();
		
		for (ObjectPointer op : cml.objects) {
			if (op instanceof RockPointer) {
				_objects[op.y][op.x] = new MapObjectRock();
				//_mapDump += op.x+":"+op.y+";";
				JSONObject p = new JSONObject();
				p.put("x", op.x);
				p.put("y", op.y);
				_mapDump.add(p);
			}
		}
	}
	
	public int getSizeX() {return _sizeX;}
	public int getSizeY() {return _sizeY;}
	
	public JSONArray getDump() {return _mapDump;}
		
	public void placeObject(MapObject o, int x, int y) {
		_objects[y][x] = o;
	}
	public void placeObject(MapObject obj) {
		_objects[obj.getY()][obj.getX()] = obj;
	}
	public boolean isObject(int x, int y) {
		return _objects[y][x]!=null;
	}
	public MapObject getObject(int x, int y) {
		return _objects[y][x];
	}
	
	public void moveObject(MapObject obj, int toX, int toY) {
		_objects[obj.getY()][obj.getX()] = null;
		placeObject(obj, toX, toY);
	}
	
	
	public ArrayList<ObjectPointer> findPath(int fromX, int fromY, int toX, int toY){
		int dx = Math.abs(toX - fromX);
		int dy = Math.abs(toY - fromY);
		
		if ((dx==0)&&(dy==0)) {return null;}
		
		if ((dx<=1)&&(dy<=1)&&(_objects[toY][toX]!=null)) {
			return null;
		}
		
		ArrayList<ObjectPointer> path = new ArrayList<ObjectPointer>();
		
		//Оптимизация для перемещений на одну клетку по кресту
		if (((dx==0)&&(dy==1))||((dx==1)&&(dy==0))) {
    		path.add(new ObjectPointer(toX, toY));	
    		return path;
		}
		
        int[][] TA=new int[_sizeY+3][_sizeX+3];

        boolean done=false;
        boolean fail=false;
        boolean OK=false;
        int curW=0;

      //=== Prepare structures
        //Place objects
        for (int i=1; i<=_sizeX; i++){
            for (int j=1; j<=_sizeY; j++){
                if (_objects[j-1][i-1]!=null){
                    TA[j][i]=48192;  //Bad point
                }else{
                    TA[j][i]=48191;  //Good point
                }
            }
        }

        //Prepare borders
        for (int i=0; i<=_sizeX+1; i++){
            TA[0][i]=48192;
            TA[_sizeY+1][i]=48192;
        }
        for (int i=0; i<=_sizeY+1; i++){
            TA[i][0]=48192;
            TA[i][_sizeX+1]=48192;
        }

        //Start and stop points
        TA[fromY+1][fromX+1]=48190;  //Point "A" marker
        TA[toY+1][toX+1]=0;         //Point "B" marker
        
        //===Debug===========
        /*
            String s;
            for (int j=1; j<=_sizeY; j++){
                s="";
                for (int i=1; i<=_sizeX; i++){
                    s=s+TA[j][i]+" ";
                    if (TA[j][i]<10000){
                        s=s+" ";
                    }
                    if (TA[j][i]<1000){
                        s=s+" ";
                    }
                    if (TA[j][i]<100){
                        s=s+" ";
                    }
                    if (TA[j][i]<10){
                        s=s+" ";
                    }
                }
                log(s);
            }
            log("");
            */        
        //=====================        
            //if (!done) return path;
		
        int left, right, up, down;
        while (!done&&!fail){
            //Prepare margins
            left=toX-curW + 1;
            right=toX+curW + 1;
            up=toY-curW + 1;
            down=toY+curW + 1;
            if (left<1){left=1;}
            if (right>_sizeX){right=_sizeX;}
            if (up<1){up=1;}
            if (down>_sizeY){down=_sizeY;}

            //Run one wave
            for (int j=up; j<=down; j++){  //Y
                for (int i=left; i<=right; i++){  //X
                    if (done) {break;}
                    if (TA[j][i]==curW){
                        //Look at diagonals
                          for (int jj=-1; jj<=1; jj=jj+2){
                            for (int ii=-1; ii<=1; ii=ii+2){
                                  if ((TA[(j+jj)][(i+ii)]==48190)&&
                                      (TA[j][(i+ii)]==48191)&&
                                      (TA[(j+jj)][i]==48191)){
                                         TA[(j+jj)][(i+ii)]=curW+1;
                                         done=true;
                                         break;
                                  }
                                  if ((TA[(j+jj)][(i+ii)]==48191)&&
                                      (TA[j][(i+ii)]==48191)&&
                                      (TA[(j+jj)][i]==48191)){
                                           TA[(j+jj)][(i+ii)]=curW+1;
                                  }
                            }
                         }

                        //Look at cross
                          for (int jj=-1; jj<=1; jj++){
                            for (int ii=-1; ii<=1; ii++){
                               if ((jj==0)||(ii==0)){
                                  if (TA[(j+jj)][(i+ii)]==48190){
                                     TA[(j+jj)][(i+ii)]=curW+1;
                                        done=true;
                                        break;
                                  }
                                  if (TA[(j+jj)][(i+ii)]==48191){
                                     TA[(j+jj)][(i+ii)]=curW+1;
                                  }
                              }
                            }
                          }
                          OK=true;
                    }
                }
            }

            //DEBUG========
            //String s;
            /*
            for (int j=1; j<=_sizeY; j++){
                s="";
                for (int i=1; i<=_sizeX; i++){
                    s=s+TA[j][i]+" ";
                    if (TA[j][i]<10000){
                        s=s+" ";
                    }
                    if (TA[j][i]<1000){
                        s=s+" ";
                    }
                    if (TA[j][i]<100){
                        s=s+" ";
                    }
                    if (TA[j][i]<10){
                        s=s+" ";
                    }
                }
                log(s);
            }
            log("");
            */
            
            //===================
            
            fail=!OK; //If no way
            curW++;
        }    //While
        
        
        if (!fail){
            //log("Way will be found");

           //Generate path
            int x=fromX+1; int y=fromY+1;
            int min=TA[y][x];
            int pc=1;
            int x1=0; int y1=0;
                        
            while (TA[y][x]>0){            	
            	//log("1: x="+x+"; y="+y);
                for (int jj=-1; jj<=1; jj=jj+2){
                    for (int ii=-1; ii<=1; ii=ii+2){
                       if ((TA[(y+jj)][(x+ii)]<min)&&
                            (TA[y][(x+ii)]!=48192)&&
                            (TA[(y+jj)][x]!=48192)){
                               x1=x+ii;
                               y1=y+jj;
                               break;
                       }
                    }
                  }
            	//log("2: x1="+x1+"; y1="+y1);
                
                for (int jj=-1; jj<=1; jj++){
                    for (int ii=-1; ii<=1; ii++){
                      if ((jj==0)||(ii==0)){
                         if (TA[(y+jj)][(x+ii)]<min){
                  		   	x1=x+ii;
                    		y1=y+jj;
                     		break;
                        }
                      }
                    }
                  }
            	//log("3: x1="+x1+"; y1="+y1);
                                    
                if ((x1==0)&&(y1==0)) {
                	return null;
                }

                pc++;
                min--;
                x=x1;
                y=y1;
                
        		path.add(new ObjectPointer(x1-1, y1-1));	        		
        		
                //log("WP: "+(x-1)+":"+(y-1));     
                //if (pc>10) break;
            }  //While TA

    		path.add(new ObjectPointer(x1-1, y1-1));
    		/*
    		HashMap<String, Integer> point = new HashMap<String, Integer>();
    		point.put("x", x1-1);
    		point.put("y", y1-1);		
    		path.add(point);
    		*/        
        }
		
		return path;
	}
	
	
    public void log(String txt){
        Utils.log("CombatMap: "+txt);
    }	
}
