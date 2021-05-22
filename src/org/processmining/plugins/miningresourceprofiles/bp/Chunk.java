package org.processmining.plugins.miningresourceprofiles.bp;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Chunk{
	
	public Vector<String> batchIDs = new Vector<String>();
	
	public Date chunkStart = null;
	public Date chunkEnd = null;
	
	public Date chunkFirstEventTime = null;
	public Date chunkLastEventTime = null;
	public Double duration = null;
	
	public Date chunkDurationStart = null;
	public Date chunkDurationEnd = null;
	
	public Date wtChunkStart = null;
	public Date wtChunkEnd = null;
	public Long wtChunkDuration = (long) 0;
	
	public Integer wtEvents = 0;
	public Set<String> chunkResources = new HashSet<String>();
	public HashMap <String, Date> resourceStart = new HashMap<String, Date>();
	public HashMap <String, Date> resourceEnd = new HashMap<String, Date>();
	public Vector<String> caseIDs = new Vector<String>();
	public HashMap <String, Integer> otherTasks = new HashMap<String, Integer>();
	
	public Integer taskEvents = 0;
	public Integer otherEvents = 0;
	public Integer uniqueCases = 0; // is equal to taskEvents - version where batch size = number of events
	public Integer numberOfCases = 0; //used in saved .csv file
	
	public Double taskDensity = null; //number of events per time unit
	public Double otherPart = null; // otherEvents/taskEvents
	public Double distanceFromMean = null; //taskDensity/global mean
	
	public Double caseUniqueness;
	
	public Boolean isBP = false;
	public Boolean isOutlier = false;
	
	//for global stat
	public Double globalMeanDensity = null;
	public Double globalStDevDensity = null;
	
	//not used
	public Double otherDensity = null; //number of events per time unit
	public Double taskOverallDensity = null; //average TS value
	public Double otherOverallDensity = null; //average TS value
	
	public Chunk() {
	} 
	
	public void printChunkv1()
	{
		System.out.println("---");
		System.out.println(chunkStart);
		System.out.println(wtChunkStart);
		System.out.println(chunkEnd);
		System.out.println(wtChunkEnd);
		System.out.println(chunkFirstEventTime);
		System.out.println(chunkLastEventTime);
		
		for(String r: chunkResources)
		{
			System.out.println(r + ": " + resourceStart.get(r) + " - " + resourceEnd.get(r));
		}
		
		System.out.println("Duration: " + wtChunkDuration/3600000);
		System.out.println(taskEvents);
		System.out.println(otherEvents);
		System.out.println(taskDensity);
		System.out.println(otherPart);
		System.out.println(caseUniqueness);
		System.out.println(distanceFromMean);
		System.out.println("Is batch: " + isBP);
		System.out.println("Is batch: " + caseIDs);
	}
	
	public void printChunk()
	{
		
		
		System.out.println("-----");
		System.out.println(duration);
		System.out.println(taskDensity);
		System.out.println(otherPart);
		System.out.println(uniqueCases);
		System.out.println(distanceFromMean);
		System.out.println(isBP);

	}
	
	public void printSE()
	{
		System.out.println(this.chunkDurationStart + " --- " + this.chunkDurationEnd);
	}
	
	public void printWTChunk()
	{
		System.out.println("---");
		System.out.println(chunkStart);
		System.out.println(chunkEnd);
		System.out.println(wtEvents);
	}
	
}




