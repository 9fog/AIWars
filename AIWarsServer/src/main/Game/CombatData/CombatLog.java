package main.Game.CombatData;

import java.io.File;
import java.io.FileWriter;

public class CombatLog {	
	private String _log;
	private String _filePath;
	private String _fileName;
	private FileWriter _wrt;
	
	public CombatLog(String filePath, String fileName) {
		_log = "";
		_filePath = filePath;
		_fileName = fileName;
		
		try {
			File flt = new File(filePath+fileName);
			flt.createNewFile();
			//FileWriter wrt = new FileWriter(flt);
			_wrt = new FileWriter(flt);
			_wrt.append("init\r\n");
			//wrt.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public void append(String line) {
		_log += line + "\r\n";
		appendFile(line);
	}
	
	private void appendFile(String line) {
		try {
			//File flt = new File(_fileName);
			//FileWriter wrt = new FileWriter(flt);
			_wrt.append(line + "\r\n");		
			//wrt.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}		
	}
	
	public void close() {
		try {
			//_wrt.flush();
			_wrt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
