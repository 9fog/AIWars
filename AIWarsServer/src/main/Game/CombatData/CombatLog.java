package main.Game.CombatData;

import java.io.File;
import java.io.FileWriter;

public class CombatLog {	
	private String _log;
	private String _fileName;
	
	public CombatLog(String fileName) {
		_log = "";
		_fileName = fileName;
		
		try {
			File flt = new File(fileName);
			flt.createNewFile();
			FileWriter wrt = new FileWriter(flt);
			wrt.append("init");
			wrt.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public void append(String line) {
		_log += line + "\n";
		appendFile(line);
	}
	
	private void appendFile(String line) {
		try {
			File flt = new File(_fileName);
			FileWriter wrt = new FileWriter(flt);
			wrt.append(line + "\n\r");		
			wrt.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}		
	}
}
