import java.util.*;
import java.lang.Math.*;

class CompareByPriority implements Comparator<Process> {
	
	@Override
	public int compare(Process p1, Process p2) {
		int result = p1.getPriority() - p2.getPriority();
		if(result == 0) {
			return p1.getArrivalTime() - p2.getArrivalTime();
		}
		return result;
	}
	
}//end of compare by arrival time