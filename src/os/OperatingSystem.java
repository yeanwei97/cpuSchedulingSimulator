import java.util.*;
import java.lang.Math.*;

public class OperatingSystem {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //input table inputs
        int numOfProcess = 0;

        do {
            System.out.print("Select the number of processes -> ");
            numOfProcess = input.nextInt();

            if (numOfProcess < 3 || numOfProcess > 10) {
                System.out.println("Number of process should be in the range of 3~10. Please enter again.");
            }
            System.out.println();
        } while (numOfProcess < 3 || numOfProcess > 10);

        ArrayList<Process> processList = new ArrayList<Process>();
        ArrayList<Process> processListR = new ArrayList<Process>();
        ArrayList<Process> processListS = new ArrayList<Process>();
        ArrayList<Process> processListT = new ArrayList<Process>();
        int timeQuantum = 0;

        for (int i = 0; i < numOfProcess; i++) {
            System.out.println("*************** Process " + (i + 1) + " **************");
            System.out.print("Burst Time -> ");
            int inputBurstTime = input.nextInt();

            System.out.print("Arrival Time -> ");
            int inputArrivalTime = input.nextInt();

            int inputPriority = 0;
            do {
                System.out.print("Priority -> ");
                inputPriority = input.nextInt();

                if (inputPriority < 1 || inputPriority > 6) {
                    System.out.println("Priority should be in the range of 1~6. Please enter again.");
                }
                System.out.println();
            } while (inputPriority < 1 || inputPriority > 6);

            String processNumToString = Integer.toString(i);
            processList.add(new Process(processNumToString, inputArrivalTime, inputBurstTime, inputPriority));
            processListR.add(new Process(processNumToString, inputArrivalTime, inputBurstTime, inputPriority));
            processListS.add(new Process(processNumToString, inputArrivalTime, inputBurstTime, inputPriority));
            processListT.add(new Process(processNumToString, inputArrivalTime, inputBurstTime, inputPriority));
            displayTable(processList);
            System.out.println();
        }
        System.out.println("************** Time Quantum  **************");
        System.out.print("Time Quantum -> ");
        timeQuantum = input.nextInt();

        FCFS(processList);
        RRWP(processListR, timeQuantum);
        SRTN(processListS);
        TLQS(processListT, timeQuantum);

    }//end of main

    public static void displayTable(ArrayList<Process> t) {
        System.out.println("|---------+------------+--------------+----------|");
        System.out.println("| Process | Burst Time | Arrival Time | Priority |");
        System.out.println("|---------+------------+--------------+----------|");

        for (int i = 0; i < t.size(); i++) { 

            //make up spaces for process
            System.out.format("| %-8s", "P" + t.get(i).getProcessNum());

            //make up spaces for burst time
            System.out.format("| %-11s", t.get(i).getBurstTime());

            //make up spaces for arrival time
            System.out.format("| %-13s", t.get(i).getArrivalTime());

            //make up spaces for priority
            System.out.format("| %-9s|", t.get(i).getPriority());

            //close after the line
            System.out.println();
            System.out.println("|---------+------------+--------------+----------|");

        }
    }//end of displayTable

    public static void printGanttChart(ArrayList<Process> g, ArrayList<Integer> t) {

        System.out.print("+");
        for (int i = 0; i < g.size(); i++) {
            int space = g.get(i).getBurstTime() + 3;
            for (int j = 0; j < space; j++) {
                System.out.print("-");
            }
            
            if(i < g.size()-1) {
                if(g.get(i).getProcessNum().equals("empty") && g.get(i+1).getProcessNum().equals("empty")) {
                    System.out.print("-");
                }
                else {
                    System.out.print("+");
                }
            }
        }
        System.out.print("+");
        System.out.println();
        
        System.out.print("|");
        for (int i = 0; i < g.size(); i++) {
            int space = g.get(i).getBurstTime() + 1;
            
            if(g.get(i).getProcessNum().equals("empty")) {
                if(space > 0)
                    System.out.format((" %-" + space + "s "), "");
            }
            else {
                if(space > 0)
                    System.out.format((" %-" + space + "s "), "P" + g.get(i).getProcessNum());
            }
            
            if(i < g.size()-1) {
                if(g.get(i).getProcessNum().equals("empty") && g.get(i+1).getProcessNum().equals("empty")) {
                    System.out.print(" ");
                }
                else {
                    System.out.print("|");
                }
            }
            
        }
        System.out.print("|");
        System.out.println();

        System.out.print("+");
        for (int i = 0; i < g.size(); i++) {
            int space = g.get(i).getBurstTime() + 3;
            for (int j = 0; j < space; j++) {
                System.out.print("-");
            }
            
            if(i < g.size()-1) {
                if(g.get(i).getProcessNum().equals("empty") && g.get(i+1).getProcessNum().equals("empty")) {
                    System.out.print("-");
                }
                else {
                    System.out.print("+");
                }
            }
        }
        System.out.print("+");
        System.out.println();

        for (int i = 0; i < t.size(); i++) {
            if (i < t.size() - 1) {
                int space = g.get(i).getBurstTime() + 4;
                if(i == 0) {
                    System.out.format(("%-" + space + "s"), t.get(i));
                }
                else if(g.get(i-1).getProcessNum().equals("empty") && g.get(i).getProcessNum().equals("empty")) {
                    System.out.format(("%-" + space + "s"), " ");
                }
                else {
                    System.out.format(("%-" + space + "s"), t.get(i));
                } 
            } 
            else {
                System.out.print(t.get(i));
            }
        }
        System.out.println();
    }

    public static void RRWP(ArrayList<Process> processListR, int time) {
        Collections.sort(processListR, new CompareByArrivalTime()); //sort prcoessList by arrivaltime first then only priority
        //displayTable(processListR);

        ArrayList<Process> queue = new ArrayList<Process>();
        ArrayList<Process> ganttChart = new ArrayList<Process>();

        Process currentProcess = processListR.get(0); //the first one to be executed

        //check whether got process interupt before end time
        ArrayList<Integer> timeline = new ArrayList<Integer>();

        int rrmax = time;
        int currenttime = currentProcess.getArrivalTime();
        do {
            timeline.add(currenttime);
			
            if (currentProcess.getBurstTime() <= rrmax) {
                currenttime = currenttime + currentProcess.getBurstTime();
                ganttChart.add(currentProcess);
                if (processListR.get(0).getProcessNum() == currentProcess.getProcessNum()) {
                    processListR.remove(0);
                } else if (queue.size() > 0) {
                    queue.remove(0);
                }
            } 
			else {
                currenttime = currenttime + rrmax;
                Process executedProcess = new Process();
                Process leftProcess = new Process();

                executedProcess.setProcessNum(currentProcess.getProcessNum());
                executedProcess.setArrivalTime(currentProcess.getArrivalTime());
                executedProcess.setPriority(currentProcess.getPriority());
                executedProcess.setBurstTime(rrmax);
                //queue.add(executedProcess);
                ganttChart.add(executedProcess);

                leftProcess.setProcessNum(currentProcess.getProcessNum());
                leftProcess.setArrivalTime(currenttime);
                leftProcess.setPriority(currentProcess.getPriority());
                leftProcess.setBurstTime(currentProcess.getBurstTime() - rrmax);

                if (leftProcess.getBurstTime() > 0) {
                    queue.add(leftProcess);
                }

                if (processListR.get(0).getProcessNum() == currentProcess.getProcessNum()) {
                    processListR.remove(0);
                } else {
                    queue.remove(0);
                }
            }
			for (int i=processListR.size()-1; i>=0;i--)
			{
				if (processListR.get(i).getArrivalTime()<currenttime)
				{
					queue.add(processListR.get(i));
					processListR.remove(i);
				}
			}
			
            if (queue.size() > 0) 
            {
                Collections.sort(queue, new CompareByPriority()); //sort prcoessList by arrivaltime first then only priority
				
            } 
			if (queue.size() == 0 && processListR.size() > 0) {
                currentProcess = processListR.get(0);
            }
			
			else if (queue.size()>0 && processListR.size()>0){
				currentProcess = queue.get(0);
			}
        } while (processListR.size() > 0);
        //displayTable(processListR);
        Collections.sort(queue, new CompareByPriority());
		if (queue.size() >0)
		{
        do {
			
            timeline.add(currenttime);
            if (queue.get(0).getBurstTime() <= rrmax) {
                currenttime = queue.get(0).getBurstTime() + currenttime;
                ganttChart.add(queue.get(0));
                queue.remove(0);
				Collections.sort(queue, new CompareByPriority());
            } else {
                Process executedProcess = new Process();
                Process leftProcess = new Process();

                currenttime = currenttime + rrmax;
                executedProcess.setProcessNum(queue.get(0).getProcessNum());
                executedProcess.setArrivalTime(queue.get(0).getArrivalTime());
                executedProcess.setPriority(queue.get(0).getPriority());
                executedProcess.setBurstTime(rrmax);
                ganttChart.add(executedProcess);

                leftProcess.setProcessNum(queue.get(0).getProcessNum());
                leftProcess.setArrivalTime(currenttime + rrmax);
                leftProcess.setPriority(queue.get(0).getPriority());
                leftProcess.setBurstTime(queue.get(0).getBurstTime() - rrmax);

                queue.add(leftProcess);
                queue.remove(0);
				Collections.sort(queue, new CompareByPriority());
			}
        } while (queue.size() != 0);
		}
		
        timeline.add(currenttime);

        System.out.println("");
        System.out.print("*******************************************");
        for (int i = 0; i < rrmax; i++) {
            System.out.print("*");
        };
        System.out.println("*************");
        System.out.println("*              Round-Robin with Quantum =  " + rrmax + "              *");
        System.out.print("*******************************************");
        for (int i = 0; i < rrmax; i++) {
            System.out.print("*");
        };
        System.out.println("*************");
        printGanttChart(ganttChart, timeline);

    }//end of RRWP

    public static void FCFS(ArrayList<Process> processList) {

        Collections.sort(processList, new CompareByArrivalTime()); //sort prcoessList by arrivaltime first then only priority

        ArrayList<Process> queue = new ArrayList<Process>();
        ArrayList<Process> withinTime = new ArrayList<Process>();
        ArrayList<Process> ganttChart = new ArrayList<Process>();
        Process currentProcess = processList.get(0); //the first one to be executed
        processList.remove(processList.get(0)); //remove from processList

        int currentProcessStartTime = currentProcess.getArrivalTime();
        int currentProcessEndTime = currentProcess.getBurstTime() + currentProcess.getArrivalTime();
        //check whether got process interupt before end time

        ArrayList<Integer> timeline = new ArrayList<Integer>();
        do {
            Iterator<Process> processListIterator = processList.iterator();
            while (processListIterator.hasNext()) {
                Process p = processListIterator.next();
                if (p.getArrivalTime() < currentProcessEndTime) {
                    withinTime.add(p);
                    processListIterator.remove();
                }
            }

            if (withinTime.size() == 0) {
                ganttChart.add(currentProcess);
                timeline.add(currentProcessStartTime);
                currentProcessStartTime = currentProcessEndTime;
                
                if(queue.size() > 0) {
                    currentProcess = queue.get(0);
                    queue.remove(queue.get(0));
                    currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                }
                else {
                    currentProcess = processList.get(0);
                    processList.remove(processList.get(0));
                    currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                }
                
            } else {
                for (int i = 0; i < withinTime.size(); i++) {
                    if (withinTime.get(i).getPriority() < currentProcess.getPriority()) {
                        Process executedProcess = new Process();
                        Process leftProcess = new Process();
                        //executed process until interrupted
                        executedProcess.setProcessNum(currentProcess.getProcessNum());
                        executedProcess.setArrivalTime(currentProcess.getArrivalTime());
                        executedProcess.setPriority(currentProcess.getPriority());
                        executedProcess.setBurstTime(withinTime.get(i).getArrivalTime() - currentProcessStartTime);
                        ganttChart.add(executedProcess);
                        timeline.add(currentProcessStartTime);

                        //left process until interrupted
                        leftProcess.setProcessNum(currentProcess.getProcessNum());
                        leftProcess.setArrivalTime(currentProcess.getArrivalTime());
                        leftProcess.setPriority(currentProcess.getPriority());
                        leftProcess.setBurstTime(currentProcessEndTime - withinTime.get(i).getArrivalTime());
                        if (leftProcess.getBurstTime() > 0) {
                            queue.add(leftProcess);
                        }

                        currentProcess = withinTime.get(i); //change current process
                        currentProcessStartTime = currentProcess.getArrivalTime();
                        currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                    } else if ((i == withinTime.size() - 1) && (withinTime.get(i).getPriority() >= currentProcess.getPriority())) {
                        queue.add(withinTime.get(i));
                        ganttChart.add(currentProcess);
                        timeline.add(currentProcessStartTime);
                        currentProcessStartTime = currentProcessEndTime;

                        Collections.sort(queue, new CompareByPriority());
                        currentProcess = queue.get(0);
                        queue.remove(queue.get(0));
                        currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                    } else {
                        queue.add(withinTime.get(i));
                    }
                }
                withinTime.clear();
            }
        } while (processList.size() != 0);

        ganttChart.add(currentProcess);
        timeline.add(currentProcessStartTime);

        Collections.sort(queue, new CompareByPriority());
        for (int i = 0; i < queue.size(); i++) {
            ganttChart.add(queue.get(i));
            currentProcessStartTime = currentProcessEndTime;
            timeline.add(currentProcessStartTime);
            currentProcess = queue.get(i);
            currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
        }
        timeline.add(currentProcessEndTime);

        System.out.println("");
        System.out.println("*****************************************************");
        System.out.println("*              First Come First Serve               *");
        System.out.println("*****************************************************");
        printGanttChart(ganttChart, timeline);

    }//end of FCFS

    public static void SRTN(ArrayList<Process> processListS) {
        {
            Collections.sort(processListS, new CompareByArrivalTime()); //sort prcoessList by arrivaltime first then only priority
            //	System.out.println("aa");
            //displayTable(processListS);

            ArrayList<Process> queue = new ArrayList<Process>();
            ArrayList<Process> withinTime = new ArrayList<Process>();
            ArrayList<Process> ganttChart = new ArrayList<Process>();
            ArrayList<Integer> timeline = new ArrayList<Integer>();
            Process currentProcess = processListS.get(0); //the first one to be executed
            processListS.remove(processListS.get(0)); //remove from processList

            int currentProcessStartTime = currentProcess.getArrivalTime();
            int currentProcessEndTime = currentProcess.getBurstTime() + currentProcess.getArrivalTime();
            //check whether got process interupt before end time
            do {
                Iterator<Process> processListIterator = processListS.iterator();
                while (processListIterator.hasNext()) {
                    Process p = processListIterator.next();
                    if (p.getArrivalTime() < currentProcessEndTime) {
                        withinTime.add(p);
                        processListIterator.remove();
                    }
                }

                if (withinTime.size() == 0) {
                    timeline.add(currentProcessStartTime);
                    ganttChart.add(currentProcess);
                    currentProcessStartTime = currentProcessEndTime;

                    if(queue.size() > 0) {
                        currentProcess = queue.get(0);
                        queue.remove(queue.get(0));
                        currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                    }
                    else {
                        currentProcess = processListS.get(0);
                        processListS.remove(processListS.get(0));
                        currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                    }
                } else {
                    for (int i = 0; i < withinTime.size(); i++) {
                        if (withinTime.get(i).getBurstTime() < (currentProcessEndTime-withinTime.get(i).getArrivalTime())) {
                            timeline.add(currentProcessStartTime);
                            Process executedProcess = new Process();
                            Process leftProcess = new Process();
                            //executed process until interrupted
                            executedProcess.setProcessNum(currentProcess.getProcessNum());
                            executedProcess.setArrivalTime(currentProcess.getArrivalTime());
                            executedProcess.setPriority(currentProcess.getPriority());
                            executedProcess.setBurstTime(withinTime.get(i).getArrivalTime() - currentProcessStartTime);
                            ganttChart.add(executedProcess);

                            //left process until interrupted
                            leftProcess.setProcessNum(currentProcess.getProcessNum());
                            leftProcess.setArrivalTime(currentProcess.getArrivalTime());
                            leftProcess.setPriority(currentProcess.getPriority());
                            leftProcess.setBurstTime(currentProcessEndTime - withinTime.get(i).getArrivalTime());
                            if (leftProcess.getBurstTime() > 0) {
                                queue.add(leftProcess);
                            }

                            currentProcess = withinTime.get(i); //change current process
                            currentProcessStartTime = currentProcess.getArrivalTime();
                            currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                        } else if (withinTime.get(i).getBurstTime() == (currentProcessEndTime-withinTime.get(i).getArrivalTime())) {
                            if (withinTime.get(i).getPriority() <= currentProcess.getPriority()) {
                                timeline.add(currentProcessStartTime);
                                Process executedProcess = new Process();
                                Process leftProcess = new Process();
                                //executed process until interrupted
                                executedProcess.setProcessNum(currentProcess.getProcessNum());
                                executedProcess.setArrivalTime(currentProcess.getArrivalTime());
                                executedProcess.setPriority(currentProcess.getPriority());
                                executedProcess.setBurstTime(withinTime.get(i).getArrivalTime() - currentProcessStartTime);
                                ganttChart.add(executedProcess);

                                //left process until interrupted
                                leftProcess.setProcessNum(currentProcess.getProcessNum());
                                leftProcess.setArrivalTime(currentProcess.getArrivalTime());
                                leftProcess.setPriority(currentProcess.getPriority());
                                leftProcess.setBurstTime(currentProcessEndTime - withinTime.get(i).getArrivalTime());

                                if (leftProcess.getBurstTime() > 0) {
                                    queue.add(leftProcess);
                                }

                                currentProcess = withinTime.get(i); //change current process
                                currentProcessStartTime = currentProcess.getArrivalTime();
                                currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                            } else {
                                queue.add(withinTime.get(i));
                            }
                        } else if ((i == withinTime.size() - 1) && (withinTime.get(i).getBurstTime() > (currentProcessEndTime-withinTime.get(i).getArrivalTime()))) {
                            timeline.add(currentProcessStartTime);
                            queue.add(withinTime.get(i));
                            ganttChart.add(currentProcess);
                            currentProcessStartTime = currentProcessEndTime;

                            Collections.sort(queue, new CompareByBurstTime());
                            currentProcess = queue.get(0);
                            queue.remove(queue.get(0));
                            currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
                        } else {
                            queue.add(withinTime.get(i));
                        }
                    }
                    withinTime.clear();
                }
            } while (processListS.size() != 0);

            ganttChart.add(currentProcess);
            timeline.add(currentProcessStartTime);
            
            Collections.sort(queue, new CompareByBurstTime());
            for (int i = 0; i < queue.size(); i++) {
                ganttChart.add(queue.get(i));
                currentProcessStartTime = currentProcessEndTime;
                timeline.add(currentProcessStartTime);
                currentProcess = queue.get(i);
                currentProcessEndTime = currentProcessStartTime + currentProcess.getBurstTime();
            }
            timeline.add(currentProcessEndTime);

            System.out.println("");
            System.out.println("*************************************************************************");
            System.out.println("*              Shortest Remaining Time Next with Priority               *");
            System.out.println("*************************************************************************");
            printGanttChart(ganttChart, timeline);

        }//end of SRTN
    }

    public static void TLQS(ArrayList<Process> ProcessListT, int TimeQuantum) {
        Collections.sort(ProcessListT, new CompareByPriority());//Compare by priority then put in three queue.

        ArrayList<Process> Queue1 = new ArrayList<Process>();
        ArrayList<Process> Queue2 = new ArrayList<Process>();
        ArrayList<Process> Queue3 = new ArrayList<Process>();

        ArrayList<Process> ganttChart1 = new ArrayList<Process>();
        ArrayList<Process> ganttChart2 = new ArrayList<Process>();
        ArrayList<Process> ganttChart3 = new ArrayList<Process>();


        for (int i = 0; i < ProcessListT.size(); i++) {
            if (ProcessListT.get(i).getPriority() == 1 || ProcessListT.get(i).getPriority() == 2) {
                Queue1.add(ProcessListT.get(i));
            } else if (ProcessListT.get(i).getPriority() == 3 || ProcessListT.get(i).getPriority() == 4) {
                Queue2.add(ProcessListT.get(i));
            } else if (ProcessListT.get(i).getPriority() == 5 || ProcessListT.get(i).getPriority() == 6) {
                Queue3.add(ProcessListT.get(i));
            }
        }

        Collections.sort(ProcessListT, new CompareByArrivalTime());
        Collections.sort(Queue1, new CompareByArrivalTime());
        Collections.sort(Queue2, new CompareByArrivalTime());
        Collections.sort(Queue3, new CompareByArrivalTime());

        Process minArrival = ProcessListT.get(0);
        
        Process workingProcess = minArrival;
        ArrayList<Integer> timeline1 = new ArrayList<Integer>();
        ArrayList<Integer> timeline2 = new ArrayList<Integer>();
        ArrayList<Integer> timeline3 = new ArrayList<Integer>();
        int QTime = TimeQuantum;
        int currentTime = workingProcess.getArrivalTime();
        timeline1.add(currentTime);
        timeline2.add(currentTime);
        timeline3.add(currentTime);

        
        if(Queue1.contains(workingProcess)) {
            changeQueue1(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        else if(Queue2.contains(workingProcess)) {
            changeQueue2(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        else if(Queue3.contains(workingProcess)) {
            changeQueue3(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        
    }
    
    public static Process createEmptyProcess(Process p) {
        Process newP = new Process("empty", p.getArrivalTime(), p.getBurstTime(), p.getPriority());
        return newP;
    }
    
    public static void changeQueue1(ArrayList<Process> Queue1, ArrayList<Process> Queue2, ArrayList<Process> Queue3, ArrayList<Process> ganttChart1, ArrayList<Process> ganttChart2, ArrayList<Process> ganttChart3, ArrayList<Integer> timeline1, ArrayList<Integer> timeline2, ArrayList<Integer> timeline3, int currentTime, int QTime, Process workingProcess) {
        ArrayList<Process> Queue = new ArrayList<Process>();
        while (Queue1.size() != 0 && Queue1.get(0).getArrivalTime() <= currentTime) {
            if(Queue.size() > 0) {
                Collections.sort(Queue, new CompareByPriority());
                if(Queue.get(0).getPriority() < Queue1.get(0).getPriority()) {
                    workingProcess = Queue.get(0);
                    Queue.remove(workingProcess);
                }
                else {
                    workingProcess = Queue1.get(0);
                    Queue1.remove(workingProcess);
                }
            } else {
                workingProcess = Queue1.get(0);
                Queue1.remove(workingProcess);
            }

            if (workingProcess.getBurstTime() <= QTime) {
                Process empty = createEmptyProcess(workingProcess);
                ganttChart1.add(workingProcess);
                ganttChart2.add(empty);
                ganttChart3.add(empty);
                currentTime += workingProcess.getBurstTime();
                timeline1.add(currentTime);
                timeline2.add(currentTime);
                timeline3.add(currentTime); 

            } else {
                Process executedProcess = new Process();
                Process leftProcess = new Process();
                
                executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                executedProcess.setPriority(workingProcess.getPriority());
                executedProcess.setProcessNum(workingProcess.getProcessNum());
                executedProcess.setBurstTime(QTime);
                
                Process empty = createEmptyProcess(executedProcess);
                ganttChart2.add(empty);
                ganttChart3.add(empty);
                ganttChart1.add(executedProcess);
                currentTime = currentTime + QTime;
                timeline1.add(currentTime);
                timeline2.add(currentTime);
                timeline3.add(currentTime);

                leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                leftProcess.setPriority(workingProcess.getPriority());
                leftProcess.setProcessNum(workingProcess.getProcessNum());
                leftProcess.setBurstTime(workingProcess.getBurstTime() - QTime);
                
                if(leftProcess.getBurstTime() > 0) {
                    Queue.add(leftProcess);

                }
                
            }//End of Round Robin 
        }
        
        while(Queue.size() != 0) {
            workingProcess = Queue.get(0);
            
            if (workingProcess.getBurstTime() <= QTime) {
                Process empty = createEmptyProcess(workingProcess);
                ganttChart1.add(workingProcess);
                ganttChart2.add(empty);
                ganttChart3.add(empty);
                currentTime += workingProcess.getBurstTime();
                timeline1.add(currentTime);
                timeline2.add(currentTime);
                timeline3.add(currentTime);
                Queue.remove(workingProcess);

            } else {
                Process executedProcess = new Process();
                Process leftProcess = new Process();
                
                executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                executedProcess.setPriority(workingProcess.getPriority());
                executedProcess.setProcessNum(workingProcess.getProcessNum());
                executedProcess.setBurstTime(QTime);
                
                Process empty = createEmptyProcess(executedProcess);
                ganttChart1.add(executedProcess);
                ganttChart2.add(empty);
                ganttChart3.add(empty);

                currentTime = currentTime + QTime;
                timeline1.add(currentTime);
                timeline2.add(currentTime);
                timeline3.add(currentTime);
                Queue.remove(workingProcess);
    
                leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                leftProcess.setPriority(workingProcess.getPriority());
                leftProcess.setProcessNum(workingProcess.getProcessNum());
                leftProcess.setBurstTime(workingProcess.getBurstTime() - QTime);

                if(leftProcess.getBurstTime() > 0) {
                    Queue.add(leftProcess);
                }
            }//End of Round Robin
            
            if(Queue1.size() != 0 && Queue1.get(0).getArrivalTime() <= currentTime)
                changeQueue1(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        
        changeQueue2(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
    }
    
    public static void changeQueue2(ArrayList<Process> Queue1, ArrayList<Process> Queue2, ArrayList<Process> Queue3, ArrayList<Process> ganttChart1, ArrayList<Process> ganttChart2, ArrayList<Process> ganttChart3, ArrayList<Integer> timeline1, ArrayList<Integer> timeline2, ArrayList<Integer> timeline3, int currentTime, int QTime, Process workingProcess) {
        if(Queue2.size() == 0) {
            changeQueue3(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        else {
            if(Queue2.get(0).getArrivalTime() > currentTime){
                changeQueue3(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
            }
            else {
                if(Queue1.size() == 0) {
                    Collections.sort(Queue2, new CompareByArrivalTime());
                    Iterator<Process> Queue2Iterator = Queue2.iterator();
                    while (Queue2Iterator.hasNext()) {
                        Process p = Queue2Iterator.next();
                        currentTime += p.getBurstTime();
                        
                        Process empty = createEmptyProcess(p);
                        ganttChart1.add(empty);
                        ganttChart3.add(empty);
                        ganttChart2.add(p);
                        
                        timeline1.add(currentTime);
                        timeline2.add(currentTime);
                        timeline3.add(currentTime);
                        Queue2Iterator.remove();
                    }
                    changeQueue3(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
                }
                else {
                    //need cautious of preemptive
                    Process cutLineQueue1 = Queue1.get(0);
                    
                    Iterator<Process> Queue2Iterator = Queue2.iterator();
                    while (Queue2Iterator.hasNext()){ 
                        workingProcess = Queue2Iterator.next();
                    
                        if((currentTime + workingProcess.getBurstTime()) < cutLineQueue1.getArrivalTime()) {
                            Process empty = createEmptyProcess(workingProcess);
                            ganttChart1.add(empty);
                            ganttChart3.add(empty);
                            ganttChart2.add(workingProcess);
                            currentTime += workingProcess.getBurstTime();
                            timeline1.add(currentTime);
                            timeline2.add(currentTime);
                            timeline3.add(currentTime);
                            Queue2Iterator.remove();
                        }
                        else {
                            Process executedProcess = new Process();
                            Process leftProcess = new Process();

                            executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                            executedProcess.setPriority(workingProcess.getPriority());
                            executedProcess.setProcessNum(workingProcess.getProcessNum());
                            executedProcess.setBurstTime(cutLineQueue1.getArrivalTime() - currentTime);

                            Process empty = createEmptyProcess(executedProcess);
                            ganttChart1.add(empty);
                            ganttChart3.add(empty);
                            ganttChart2.add(executedProcess);
                            currentTime = currentTime + executedProcess.getBurstTime();
                            timeline1.add(currentTime);
                            timeline2.add(currentTime);
                            timeline3.add(currentTime);
                            Queue2.remove(workingProcess);

                            leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                            leftProcess.setPriority(workingProcess.getPriority());
                            leftProcess.setProcessNum(workingProcess.getProcessNum());
                            leftProcess.setBurstTime(workingProcess.getBurstTime() - executedProcess.getBurstTime() );
                            if(leftProcess.getBurstTime() > 0) {
                                Queue2.add(leftProcess);
                            }

                            break;
                        }
                    }

                    Collections.sort(Queue2, new CompareByArrivalTime());

                    changeQueue1(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
                }
            }
        }
    }
    
    public static void changeQueue3(ArrayList<Process> Queue1, ArrayList<Process> Queue2, ArrayList<Process> Queue3, ArrayList<Process> ganttChart1, ArrayList<Process> ganttChart2, ArrayList<Process> ganttChart3, ArrayList<Integer> timeline1, ArrayList<Integer> timeline2, ArrayList<Integer> timeline3, int currentTime, int QTime, Process workingProcess) { 
        if((Queue1.size() == 0) && (Queue2.size() == 0)) {
            Collections.sort(Queue3, new CompareByArrivalTime());
            Iterator<Process> Queue3Iterator = Queue3.iterator();
            while (Queue3Iterator.hasNext()) {
                Process p = Queue3Iterator.next();
                currentTime += p.getBurstTime();
                
                Process empty = createEmptyProcess(p);
                ganttChart1.add(empty);
                ganttChart2.add(empty);
                ganttChart3.add(p);
                
                timeline1.add(currentTime);
                timeline2.add(currentTime);
                timeline3.add(currentTime);
                Queue3Iterator.remove();
            }
            System.out.println("");
            System.out.println("************************************************************");
            System.out.println("*               Three-level Queue Scheduling               *");
            System.out.println("************************************************************");
            printGanttChart(ganttChart1, timeline1);
            System.out.println();
            printGanttChart(ganttChart2, timeline2);
            System.out.println();
            printGanttChart(ganttChart3, timeline3);
        }
        else if((Queue1.size() == 0) && (Queue2.size() != 0)){
            //need cautious of preemptive
            Process cutLineQueue2 = Queue2.get(0);
            workingProcess = Queue3.get(0);

            Iterator<Process> Queue3Iterator = Queue3.iterator();
            while (Queue3Iterator.hasNext()){ 
                workingProcess = Queue3Iterator.next();
                if((currentTime + workingProcess.getBurstTime()) < cutLineQueue2.getArrivalTime()) {
                    Process empty = createEmptyProcess(workingProcess);
                    ganttChart1.add(empty);
                    ganttChart2.add(empty);
                    ganttChart3.add(workingProcess);
                    currentTime += workingProcess.getBurstTime();
                    timeline1.add(currentTime);
                    timeline2.add(currentTime);
                    timeline3.add(currentTime);
                    Queue3Iterator.remove();
                }
                else {
                    Process executedProcess = new Process();
                    Process leftProcess = new Process();

                    executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                    executedProcess.setPriority(workingProcess.getPriority());
                    executedProcess.setProcessNum(workingProcess.getProcessNum());
                    executedProcess.setBurstTime(cutLineQueue2.getArrivalTime() - currentTime);

                    Process empty = createEmptyProcess(executedProcess);
                    ganttChart1.add(empty);
                    ganttChart2.add(empty);
                    ganttChart3.add(executedProcess);
                    currentTime = currentTime + executedProcess.getBurstTime();
                    timeline1.add(currentTime);
                    timeline2.add(currentTime);
                    timeline3.add(currentTime);
                    Queue3.remove(workingProcess);

                    leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                    leftProcess.setPriority(workingProcess.getPriority());
                    leftProcess.setProcessNum(workingProcess.getProcessNum());
                    leftProcess.setBurstTime(workingProcess.getBurstTime() - executedProcess.getBurstTime() );
                    if(leftProcess.getBurstTime() > 0) {
                        Queue3.add(leftProcess);
                    }

                    break;
                }

            }
            Collections.sort(Queue3, new CompareByArrivalTime());
            
            changeQueue2(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);

        }
        else if((Queue1.size() != 0) && (Queue2.size() == 0)) {
            Process cutLineQueue1 = Queue1.get(0);

            Iterator<Process> Queue3Iterator = Queue3.iterator();
            while (Queue3Iterator.hasNext()){ 
                workingProcess = Queue3Iterator.next();
                if((currentTime + workingProcess.getBurstTime()) < cutLineQueue1.getArrivalTime()) {
                    Process empty = createEmptyProcess(workingProcess);
                    ganttChart1.add(empty);
                    ganttChart2.add(empty);
                    ganttChart3.add(workingProcess);
                    currentTime += workingProcess.getBurstTime();
                    timeline1.add(currentTime);
                    timeline2.add(currentTime);
                    timeline3.add(currentTime);
                    Queue3Iterator.remove();
                }
                else {
                    Process executedProcess = new Process();
                    Process leftProcess = new Process();

                    executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                    executedProcess.setPriority(workingProcess.getPriority());
                    executedProcess.setProcessNum(workingProcess.getProcessNum());
                    executedProcess.setBurstTime(cutLineQueue1.getArrivalTime() - currentTime);

                    Process empty = createEmptyProcess(executedProcess);
                    ganttChart1.add(empty);
                    ganttChart2.add(empty);
                    ganttChart3.add(executedProcess);
                    currentTime = currentTime + executedProcess.getBurstTime();
                    timeline1.add(currentTime);
                    timeline2.add(currentTime);
                    timeline3.add(currentTime);
                    Queue3.remove(workingProcess);

                    leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                    leftProcess.setPriority(workingProcess.getPriority());
                    leftProcess.setProcessNum(workingProcess.getProcessNum());
                    leftProcess.setBurstTime(workingProcess.getBurstTime() - executedProcess.getBurstTime() );
                    if(leftProcess.getBurstTime() > 0) {
                        Queue3.add(leftProcess);
                    }

                    break;
                }
            }
            Collections.sort(Queue3, new CompareByArrivalTime());

            changeQueue1(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
        }
        else if((Queue1.size() != 0) && (Queue2.size() != 0)) {
            Process compare1 = Queue1.get(0);
            Process compare2 = Queue2.get(0);

            Process min = compare1;
            if(min.getArrivalTime() > compare2.getArrivalTime()) {
                min = compare2;
            }

            if(min == compare1) {
                Process cutLineQueue1 = Queue1.get(0);
                workingProcess = Queue3.get(0);

                Iterator<Process> Queue3Iterator = Queue3.iterator();
                while (Queue3Iterator.hasNext()){ 
                    workingProcess = Queue3Iterator.next();
                    if((currentTime + workingProcess.getBurstTime()) < cutLineQueue1.getArrivalTime()) {
                        Process empty = createEmptyProcess(workingProcess);
                        ganttChart1.add(empty);
                        ganttChart2.add(empty);
                        ganttChart3.add(workingProcess);
                        currentTime += workingProcess.getBurstTime();
                        timeline1.add(currentTime);
                        timeline2.add(currentTime);
                        timeline3.add(currentTime);
                        Queue3Iterator.remove();
                    }
                    else {

                        Process executedProcess = new Process();
                        Process leftProcess = new Process();

                        executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                        executedProcess.setPriority(workingProcess.getPriority());
                        executedProcess.setProcessNum(workingProcess.getProcessNum());
                        executedProcess.setBurstTime(cutLineQueue1.getArrivalTime() - currentTime);

                        Process empty = createEmptyProcess(executedProcess);
                        ganttChart1.add(empty);
                        ganttChart2.add(empty);
                        ganttChart3.add(executedProcess);
                        currentTime = currentTime + executedProcess.getBurstTime();
                        timeline1.add(currentTime);
                        timeline2.add(currentTime);
                        timeline3.add(currentTime);
                        Queue3.remove(workingProcess);

                        leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                        leftProcess.setPriority(workingProcess.getPriority());
                        leftProcess.setProcessNum(workingProcess.getProcessNum());
                        leftProcess.setBurstTime(workingProcess.getBurstTime() - executedProcess.getBurstTime() );
                        if(leftProcess.getBurstTime() > 0) {
                            Queue3.add(leftProcess);
                        }

                        break;
                    }
                }
                Collections.sort(Queue3, new CompareByArrivalTime());
                
                changeQueue1(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
            }
            else if(min == compare2) {
                Process cutLineQueue2 = Queue2.get(0);
                workingProcess = Queue3.get(0);

                Iterator<Process> Queue3Iterator = Queue3.iterator();
                while (Queue3Iterator.hasNext()){ 
                    workingProcess = Queue3Iterator.next();

                    if((currentTime + workingProcess.getBurstTime()) < cutLineQueue2.getArrivalTime()) {
                        Process empty = createEmptyProcess(workingProcess);
                        ganttChart1.add(empty);
                        ganttChart2.add(empty);
                        ganttChart3.add(workingProcess);
                        currentTime += workingProcess.getBurstTime();
                        timeline1.add(currentTime);
                        timeline2.add(currentTime);
                        timeline3.add(currentTime);
                        Queue3Iterator.remove();
                    }
                    else {
                        Process executedProcess = new Process();
                        Process leftProcess = new Process();

                        executedProcess.setArrivalTime(workingProcess.getArrivalTime());
                        executedProcess.setPriority(workingProcess.getPriority());
                        executedProcess.setProcessNum(workingProcess.getProcessNum());
                        executedProcess.setBurstTime(cutLineQueue2.getArrivalTime() - currentTime);

                        Process empty = createEmptyProcess(executedProcess);
                        ganttChart1.add(empty);
                        ganttChart2.add(empty);
                        ganttChart3.add(executedProcess);
                        currentTime = currentTime + executedProcess.getBurstTime();
                        timeline1.add(currentTime);
                        timeline2.add(currentTime);
                        timeline3.add(currentTime);
                        Queue3.remove(workingProcess);

                        leftProcess.setArrivalTime(workingProcess.getArrivalTime());
                        leftProcess.setPriority(workingProcess.getPriority());
                        leftProcess.setProcessNum(workingProcess.getProcessNum());
                        leftProcess.setBurstTime(workingProcess.getBurstTime() - executedProcess.getBurstTime() );
                        if(leftProcess.getBurstTime() > 0) {
                            Queue3.add(leftProcess);
                        }

                        break;
                    }
                }
                Collections.sort(Queue3, new CompareByArrivalTime());

                changeQueue2(Queue1, Queue2, Queue3, ganttChart1, ganttChart2, ganttChart3, timeline1, timeline2, timeline3, currentTime, QTime, workingProcess);
            }
        }
    }
}//end of class

