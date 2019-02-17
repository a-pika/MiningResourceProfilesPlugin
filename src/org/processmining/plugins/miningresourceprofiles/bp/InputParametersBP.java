package org.processmining.plugins.miningresourceprofiles.bp;

import java.util.HashMap;
import java.util.Vector;

public class InputParametersBP{
	
	public long timeUnit;
	public int decNum; 
	public String cpmType;
	public String ARL0;
	public String startup;
	public String eventType;
	
	public long startTime;
	public long slotSize;
	public long numberOfSlots;
	
	public long startTimeWT;
	public long slotSizeWT;
	public long numberOfSlotsWT;
	
	public boolean showTSCheckBox;
	
	//script
	public String logName;
	public long slotSizeScript;
	
	public Vector<String> bpResource = new Vector<String>();
	public Vector<String> bpActivity = new Vector<String>();
	
	public String currentActivity = "";
	public String currentResource = "";
	public Boolean resource; //false - a/ac; true - ar/acr
	public Boolean caseData; //false - a/ar; true - ac/acr
	
	//WT
	public Boolean considerWorkingTime; 
	public Integer bptsSlotType; // 1:min; 2:median; 3:mean 4:25%
	public Integer bptsSlotTypeWT; // 1:min; 2:median; 3:mean 4:25%

	public HashMap<String,Vector<String>> bpData = new HashMap<String,Vector<String>>(); //attribute name - values (any)
	
	//Global statistics
	public long startTimeGS;
	public long slotSizeGS;
	public long numberOfSlotsGS;
	public Double batchTaskThreshold;
	public Double interruptionsTolerance;
	public Boolean checkInterruptions;
	public Double minDistanceFromMean;
	public Double minDensity;
	public Double slotSizeEventDistancePercentile;
	
	//Seasonality
	public boolean detectSeasonality;
	public long startTimeS;
	public long slotSizeS;
	public Vector<Long> slotSizesS = new Vector<Long>(); //slot sizes to be considered in seasonality detection
	public long numberOfSlotsS;
	public boolean periodicityConsiderBatchStart;
	public Double periodicityRepetitionPeriodThreshold;
	public boolean considerOutliers;
	
	//Loops
	public Boolean preprocessLog;
	public Boolean ignoreCaseIDs;
	public Double caseUniqueness;
	public Boolean checkNumberOfCases;
	public Integer minNumberOfCases;
	
	//Outliers
	public Boolean detectOutliers;
	public String method;
	public Integer minNumberOfChunksForOutliers;
	
	//Merge BP/NBP chunks
	public Boolean mergeChunks;
	
	public InputParametersBP() {
		
		detectSeasonality = true;
		showTSCheckBox = false;
		
		//script
		logName = "CoSeLoG-WABO1-task-resource-1hr";
		slotSizeScript = 3600000; //1 hour
		
		//loops
		preprocessLog = false;
		checkNumberOfCases = true;
		minNumberOfCases = 2;
		
		//outliers
		detectOutliers = true;
		minNumberOfChunksForOutliers = 6;
		
		considerWorkingTime = false;
		mergeChunks = true;
		
		considerOutliers = true;
		periodicityConsiderBatchStart = true;
		periodicityRepetitionPeriodThreshold = 0.5;
		
		bptsSlotType = 2; // 1:min; 2:median; 3:mean; 4: given %
		bptsSlotTypeWT = 2; // 1:min; 2:median; 3:mean 
		slotSizeEventDistancePercentile = 0.75;
		
		//liberal
		minDistanceFromMean = 1.0;
		checkInterruptions = true;
		interruptionsTolerance = 0.34;
		
		//centrist
		//minDistanceFromMean = 1.5;
		//checkInterruptions = true;
		//interruptionsTolerance = 0.3;
		
		//conservative
		//minDistanceFromMean = 1.8;
		//checkInterruptions = true;
		//interruptionsTolerance = 0.0;
		
		
		minDensity = 0.00;
		caseUniqueness = 0.5;
		ignoreCaseIDs = false;
		
		slotSizesS.add((long) 3600000); 		// 1 hour
		slotSizesS.add((long) 24*3600000); 		// 1 day
		slotSizesS.add((long) 7*24*3600000); 	// 1 week
				
		slotSizeS = 3600000; //1 hour: 3600000
		timeUnit = 3600000; //1 hour
		slotSizeGS = 3600000; //1 hour
					
		method = "I";
		decNum = 2;
		cpmType = "Mann-Whitney";
		ARL0 = "370"; //370
		startup = "1";
		eventType = "complete";
		
		resource = false; //false - a/ac; true - ar/acr
		caseData = false; //false - a/ar; true - ac/acr
	} 
	
}




