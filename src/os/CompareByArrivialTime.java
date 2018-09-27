import java.util.*;
import java.lang.Math.*;

class CompareByArrivalTime implements Comparator<Process> {
	
	@Override
	public int compare(Process p1, Process p2) {
		int result = p1.getArrivalTime() - p2.getArrivalTime();
		if(result == 0) {
			return p1.getPriority() - p2.getPriority();
		}
		return result;
	}
	
}//end of compare by arrival time
