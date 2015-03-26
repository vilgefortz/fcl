package research.fcl.application.parser;

import com.google.gson.annotations.Expose;

public class LogEntry {
	public LogEntry(String info, int line2, int pointer, int countLinepos) {
		this.entry=info;
		this.line=line2;
		this.pos=pointer;
		this.linepos=countLinepos;
	}
	
	@Expose
	public int linepos;
	@Expose
	public int line;
	@Expose
	public int pos;
	@Expose
	public String entry;
}
