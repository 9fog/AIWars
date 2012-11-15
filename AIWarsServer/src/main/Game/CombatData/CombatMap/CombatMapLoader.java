package main.Game.CombatData.CombatMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import core.Utils;

public class CombatMapLoader {	
	private final String TAG_PLAYERS = "players";
	
	private final char TAG_ROCK5   = '1';
	private final char TAG_ROCK10  = '2';
	private final char TAG_ROCK100 = '3';
	private final char TAG_FLAG    = 'F';
	
	private final char TAG_RECON0     = 'a';
	private final char TAG_TANK0      = 'b';
	private final char TAG_ARTILLERY0 = 'c';
	private final char TAG_BASE0      = 'd';
	
	private final char TAG_RECON1     = 'e';
	private final char TAG_TANK1      = 'f';
	private final char TAG_ARTILLERY1 = 'g';
	private final char TAG_BASE1      = 'h';
	
	private final char TAG_RECON2     = 'i';
	private final char TAG_TANK2      = 'j';
	private final char TAG_ARTILLERY2 = 'k';
	private final char TAG_BASE2      = 'l';
	
	private final char TAG_RECON3     = 'm';
	private final char TAG_TANK3      = 'n';
	private final char TAG_ARTILLERY3 = 'o';
	private final char TAG_BASE3      = 'p';

	
	public int sizeX;
	public int sizeY;
	public int playersCount;
	public ArrayList<ObjectPointer> objects;
	
	public CombatMapLoader(String mapName) throws Exception {
		//FileReader input = new FileReader(Config.getInstance().getVarString("mapsFolder")+mapName);
		FileReader input = new FileReader("maps/"+mapName);
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;
				
		int lineNumber = 0;
		int flagsCount = 0;
		int unitsCount = 0;
		objects = new ArrayList<ObjectPointer>();
		while ((myLine = bufRead.readLine()) != null) {
			if (myLine.contains(TAG_PLAYERS)) {
				String[] s = myLine.split(" ");
				playersCount = Integer.parseInt(s[1]);				
			} else {
				for (int i=0; i<myLine.length(); i++) {
					switch (myLine.charAt(i)) {
						case TAG_ROCK5:
						  if (Utils.runProb(5)) {
							  objects.add(new RockPointer(i, lineNumber));
						  }
						break;
						case TAG_ROCK10:
						  if (Utils.runProb(10)) {
							  objects.add(new RockPointer(i, lineNumber));
						  }
						break;
						case TAG_ROCK100:
							  objects.add(new RockPointer(i, lineNumber));
							break;
						case TAG_FLAG:
							  objects.add(new FlagPointer(flagsCount+1, i, lineNumber));
							  flagsCount++;
						break;			
						
						case TAG_RECON0:
							  objects.add(new UnitPointer(unitsCount+1, 1, 0, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_TANK0:
							  objects.add(new UnitPointer(unitsCount+1, 2, 0, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_ARTILLERY0:
							  objects.add(new UnitPointer(unitsCount+1, 3, 0, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_BASE0:
							  objects.add(new UnitPointer(unitsCount+1, 4, 0, i, lineNumber));
							  unitsCount++;
						break;	
						
						case TAG_RECON1:
							  objects.add(new UnitPointer(unitsCount+1, 1, 1, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_TANK1:
							  objects.add(new UnitPointer(unitsCount+1, 2, 1, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_ARTILLERY1:
							  objects.add(new UnitPointer(unitsCount+1, 3, 1, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_BASE1:
							  objects.add(new UnitPointer(unitsCount+1, 4, 1, i, lineNumber));
							  unitsCount++;
						break;	
						
						case TAG_RECON2:
							  objects.add(new UnitPointer(unitsCount+1, 1, 2, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_TANK2:
							  objects.add(new UnitPointer(unitsCount+1, 2, 2, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_ARTILLERY2:
							  objects.add(new UnitPointer(unitsCount+1, 3, 2, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_BASE2:
							  objects.add(new UnitPointer(unitsCount+1, 4, 2, i, lineNumber));
							  unitsCount++;
						break;	
						
						case TAG_RECON3:
							  objects.add(new UnitPointer(unitsCount+1, 1, 3, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_TANK3:
							  objects.add(new UnitPointer(unitsCount+1, 2, 3, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_ARTILLERY3:
							  objects.add(new UnitPointer(unitsCount+1, 3, 3, i, lineNumber));
							  unitsCount++;
						break;						
						case TAG_BASE3:
							  objects.add(new UnitPointer(unitsCount+1, 4, 3, i, lineNumber));
							  unitsCount++;
						break;							
					}
					sizeX = myLine.length();
					//Utils.log(myLine.charAt(i)+"");
				}
				lineNumber++;
			}
		}	
		
		sizeY = lineNumber;
		
		/*
		Utils.log("sizeX = "+sizeX);
		Utils.log("sizeY = "+sizeY);
		Utils.log("playersCount = "+playersCount);
		Utils.log("unitsCount = "+unitsCount);
		Utils.log("flagsCount = "+flagsCount);
		Utils.log("objects = "+objects.size());
		*/		
	}

}
