package syntax;

import java.util.ArrayList;

public class SLRStateTable {
	//SLR分析的所有产生式集，带·的那种
	
	private ArrayList<SLRFormula> slrState;

	public ArrayList<SLRFormula> getSlrState() {
		return slrState;
	}

	public void setSlrState(ArrayList<SLRFormula> slrState) {
		this.slrState = slrState;
	}

	
    
}
