import java.util.*;
import java.lang.Math.*;

class CompareByBurstTime implements Comparator<Process> {
	
	@Override
	public int compare(Process p1, Process p2) {
		int result = p1.getBurstTime() - p2.getBurstTime();
		if(result == 0) {
			return p1.getPriority() - p2.getPriority();
		}
		return result;
	}
}//end of compare by arrival time
