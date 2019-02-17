package org.processmining.plugins.miningresourceprofiles.bp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class BPTS{
	
	public Vector<Date> times = new Vector<Date>(); //time slot start times
	public Vector<Integer> taskTS = new Vector<Integer>();
	public Vector<Integer> otherTS = new Vector<Integer>();
	public Vector<Integer> taskCP = new Vector<Integer>();
	public Vector<Integer> otherCP = new Vector<Integer>();
	
	
	//WT
	public Vector<Date> timesWT = new Vector<Date>(); //time slot start times
	public Vector<Integer> WTTS = new Vector<Integer>();
	public Vector<Integer> WTCP = new Vector<Integer>();
	
	public Vector<Chunk> wt = new Vector<Chunk>();
	public Vector<Chunk> nwt = new Vector<Chunk>();
	
	//GS
	public Vector<Date> timesGS = new Vector<Date>(); //time slot start times
	public Vector<Integer> taskTSGS = new Vector<Integer>();
	public Double gsMean;
	public Double gsMedian;
	public Double gsStDev;	
	
	public BPTS() {
	} 
	
	public Vector<Chunk> getWT (Vector<Chunk> chunks)
	{
		Vector<Chunk> wtChunks = new Vector<Chunk>();
		
		for(int i=0; i<chunks.size(); i++)
		{
			if(chunks.elementAt(i).wtEvents > 0)
				wtChunks.add(chunks.elementAt(i));
		}
		
		return wtChunks;
	}
	
	public Vector<Chunk> getNWT (Vector<Chunk> chunks)
	{
		Vector<Chunk> nwtChunks = new Vector<Chunk>();
		
		for(int i=0; i<chunks.size(); i++)
		{
			if(chunks.elementAt(i).wtEvents == 0)
				nwtChunks.add(chunks.elementAt(i));
		}
		
		return nwtChunks;
	}

	
	public Vector<Chunk> getChunks(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(taskCP.size()>0)
		{	
		Chunk firstChunk = new Chunk();
		
		firstChunk.chunkStart = times.elementAt(0);
		firstChunk.chunkEnd = times.elementAt(taskCP.elementAt(0));
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
						eventTime.before(firstChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
							firstChunk.taskEvents++;
			
				if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
						eventTime.before(firstChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
							firstChunk.otherEvents++;
			
			}
		}
		
		Double firstChunkHours = (double) ((firstChunk.chunkEnd.getTime() - firstChunk.chunkStart.getTime())/3600000);
		firstChunk.taskDensity = firstChunk.taskEvents/firstChunkHours;
		firstChunk.otherDensity = firstChunk.otherEvents/firstChunkHours;
		
		chunks.add(firstChunk);
		
		for (int i=0; i<taskCP.size()-1; i++)
		{
			Chunk chunk = new Chunk();
			chunk.chunkStart = times.elementAt(taskCP.elementAt(i));
			chunk.chunkEnd = times.elementAt(taskCP.elementAt(i+1));
			
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
					
					if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd) && type.equals(ip.eventType) && 
							resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
								chunk.taskEvents++;
				
					if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd) && type.equals(ip.eventType) && 
							resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
								chunk.otherEvents++;
				
				}
			}
			
			Double chunkHours = (double) ((chunk.chunkEnd.getTime() - chunk.chunkStart.getTime())/3600000);
			chunk.taskDensity = chunk.taskEvents/chunkHours;
			chunk.otherDensity = chunk.otherEvents/chunkHours;
			
			
			chunks.add(chunk);
		}
		
		Chunk lastChunk = new Chunk();
		lastChunk.chunkStart = times.elementAt(taskCP.elementAt(taskCP.size()-1));
		
		Long endDateL = times.elementAt(times.size()-1).getTime() + (times.elementAt(times.size()-1).getTime()-times.elementAt(times.size()-2).getTime());
		Date endDate = new Date(endDateL);
		
		lastChunk.chunkEnd = endDate;
		
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
						eventTime.before(lastChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
						lastChunk.taskEvents++;
			
				if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
						eventTime.before(lastChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
						lastChunk.otherEvents++;
			
			}
		}
		
		Double lastChunkHours = (double) ((lastChunk.chunkEnd.getTime() - lastChunk.chunkStart.getTime())/3600000);
		lastChunk.taskDensity = lastChunk.taskEvents/lastChunkHours;
		lastChunk.otherDensity = lastChunk.otherEvents/lastChunkHours;
		
		
		chunks.add(lastChunk);
		
		// Overall density
		
		Double overallHours = (double) ((lastChunk.chunkEnd.getTime() - firstChunk.chunkStart.getTime())/3600000);
		int tasks = 0;
		int other = 0;
		
		for(int i=0; i<chunks.size(); i++)
		{
			tasks += chunks.elementAt(i).taskEvents;
			other += chunks.elementAt(i).otherEvents;
		}
		
		Double taskDensity = tasks/overallHours;
		Double otherDensity = other/overallHours;
		
		for(int i=0; i<chunks.size(); i++)
		{
			chunks.elementAt(i).taskOverallDensity = taskDensity;
			chunks.elementAt(i).otherOverallDensity = otherDensity;
		}
		}

		return chunks;
	}
	
	
	public Vector<Chunk> getChunksAV1(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(taskCP.size()>0)
		{	
		Chunk firstChunk = new Chunk();
		
		firstChunk.chunkStart = times.elementAt(0);
		firstChunk.chunkEnd = times.elementAt(taskCP.elementAt(0));
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
						eventTime.before(firstChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
							firstChunk.taskEvents++;
			
				if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
						eventTime.before(firstChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
							firstChunk.otherEvents++;
			
			}
		}
		
		Double firstChunkHours = (double) ((firstChunk.chunkEnd.getTime() - firstChunk.chunkStart.getTime())/3600000);
		firstChunk.taskDensity = firstChunk.taskEvents/firstChunkHours;
		firstChunk.otherDensity = firstChunk.otherEvents/firstChunkHours;
		
		chunks.add(firstChunk);
		
		for (int i=0; i<taskCP.size()-1; i++)
		{
			Chunk chunk = new Chunk();
			chunk.chunkStart = times.elementAt(taskCP.elementAt(i));
			chunk.chunkEnd = times.elementAt(taskCP.elementAt(i+1));
			
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
					
					if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd) && type.equals(ip.eventType) && 
							resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
								chunk.taskEvents++;
				
					if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd) && type.equals(ip.eventType) && 
							resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
								chunk.otherEvents++;
				
				}
			}
			
			Double chunkHours = (double) ((chunk.chunkEnd.getTime() - chunk.chunkStart.getTime())/3600000);
			chunk.taskDensity = chunk.taskEvents/chunkHours;
			chunk.otherDensity = chunk.otherEvents/chunkHours;
			
			
			chunks.add(chunk);
		}
		
		Chunk lastChunk = new Chunk();
		lastChunk.chunkStart = times.elementAt(taskCP.elementAt(taskCP.size()-1));
		
		Long endDateL = times.elementAt(times.size()-1).getTime() + (times.elementAt(times.size()-1).getTime()-times.elementAt(times.size()-2).getTime());
		Date endDate = new Date(endDateL);
		
		lastChunk.chunkEnd = endDate;
		
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
						eventTime.before(lastChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && task.equals(ip.bpActivity.elementAt(0)))
						lastChunk.taskEvents++;
			
				if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
						eventTime.before(lastChunk.chunkEnd) && type.equals(ip.eventType) && 
						resource.equals(ip.bpResource.elementAt(0)) && !task.equals(ip.bpActivity.elementAt(0)))
						lastChunk.otherEvents++;
			
			}
		}
		
		Double lastChunkHours = (double) ((lastChunk.chunkEnd.getTime() - lastChunk.chunkStart.getTime())/3600000);
		lastChunk.taskDensity = lastChunk.taskEvents/lastChunkHours;
		lastChunk.otherDensity = lastChunk.otherEvents/lastChunkHours;
		
		
		chunks.add(lastChunk);
		
		// Overall density
		
		Double overallHours = (double) ((lastChunk.chunkEnd.getTime() - firstChunk.chunkStart.getTime())/3600000);
		int tasks = 0;
		int other = 0;
		
		for(int i=0; i<chunks.size(); i++)
		{
			tasks += chunks.elementAt(i).taskEvents;
			other += chunks.elementAt(i).otherEvents;
		}
		
		Double taskDensity = tasks/overallHours;
		Double otherDensity = other/overallHours;
		
		for(int i=0; i<chunks.size(); i++)
		{
			chunks.elementAt(i).taskOverallDensity = taskDensity;
			chunks.elementAt(i).otherOverallDensity = otherDensity;
		}
		}

		return chunks;
	}
	
	
	
	public Vector<Chunk> getChunksARV1(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(taskCP.size()>0)
		{	
		Chunk firstChunk = new Chunk();
		
		firstChunk.chunkStart = times.elementAt(0);
		firstChunk.chunkEnd = times.elementAt(taskCP.elementAt(0));
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				boolean correctData = true;
				
				if(ip.caseData)
					for(String key : ip.bpData.keySet())
					{
						XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
						
						if(dataAttr != null)
						{
							if(!ip.bpData.get(key).contains(dataAttr.toString()))
								correctData = false;
						}
						else
						{
							correctData = false;
						}
					}
	
				Boolean correctEvent = type.equals(ip.eventType) && resource.equals(ip.currentResource) && 
						(eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
						eventTime.before(firstChunk.chunkEnd);
				
				if(!ip.caseData) //AR
				{
					if(correctEvent && task.equals(ip.currentActivity))
								firstChunk.taskEvents++;
				
					if(correctEvent && !task.equals(ip.currentActivity))
								firstChunk.otherEvents++;
				}
				
				if(ip.caseData) //ACR
				{
					if(correctEvent && task.equals(ip.currentActivity) && correctData)
						firstChunk.taskEvents++;
		
					if(correctEvent && (!task.equals(ip.currentActivity) || !correctData))
						firstChunk.otherEvents++;
				}
			
			}
		}
		
		Double firstChunkNumberOfTimeUnits = (double) ((firstChunk.chunkEnd.getTime() - firstChunk.chunkStart.getTime())/ip.timeUnit);
		firstChunk.taskDensity = firstChunk.taskEvents/firstChunkNumberOfTimeUnits;
		firstChunk.otherDensity = firstChunk.otherEvents/firstChunkNumberOfTimeUnits;
		
		chunks.add(firstChunk);
		
		
		
		for (int i=0; i<taskCP.size()-1; i++)
		{
			Chunk chunk = new Chunk();
			chunk.chunkStart = times.elementAt(taskCP.elementAt(i));
			chunk.chunkEnd = times.elementAt(taskCP.elementAt(i+1));
			
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
						
					boolean correctData = true;
					
					if(ip.caseData)
						for(String key : ip.bpData.keySet())
						{
							XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
							
							if(dataAttr != null)
							{
								if(!ip.bpData.get(key).contains(dataAttr.toString()))
									correctData = false;
							}
							else
							{
								correctData = false;
							}
						}
		
					Boolean correctEvent = type.equals(ip.eventType) && resource.equals(ip.currentResource) && 
							(eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd);
					
					if(!ip.caseData) //AR
					{
						if(correctEvent && task.equals(ip.currentActivity))
							chunk.taskEvents++;
					
						if(correctEvent && !task.equals(ip.currentActivity))
							chunk.otherEvents++;
					}
					
					if(ip.caseData) //ACR
					{
						if(correctEvent && task.equals(ip.currentActivity) && correctData)
							chunk.taskEvents++;
			
						if(correctEvent && (!task.equals(ip.currentActivity) || !correctData))
							chunk.otherEvents++;
					}
				}
			}
			
			Double chunkNumberOfTimeUnits = (double) ((chunk.chunkEnd.getTime() - chunk.chunkStart.getTime())/ip.timeUnit);
			chunk.taskDensity = chunk.taskEvents/chunkNumberOfTimeUnits;
			chunk.otherDensity = chunk.otherEvents/chunkNumberOfTimeUnits;
				
			chunks.add(chunk);
		}
		
		Chunk lastChunk = new Chunk();
		lastChunk.chunkStart = times.elementAt(taskCP.elementAt(taskCP.size()-1));
		
		Long endDateL = times.elementAt(times.size()-1).getTime() + (times.elementAt(times.size()-1).getTime()-times.elementAt(times.size()-2).getTime());
		Date endDate = new Date(endDateL);
		
		lastChunk.chunkEnd = endDate;
		
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				boolean correctData = true;
				
				if(ip.caseData)
					for(String key : ip.bpData.keySet())
					{
						XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
						
						if(dataAttr != null)
						{
							if(!ip.bpData.get(key).contains(dataAttr.toString()))
								correctData = false;
						}
						else
						{
							correctData = false;
						}
					}
	
				Boolean correctEvent = type.equals(ip.eventType) && resource.equals(ip.currentResource) && 
						(eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
						eventTime.before(lastChunk.chunkEnd);
				
				if(!ip.caseData) //AR
				{
					if(correctEvent && task.equals(ip.currentActivity))
						lastChunk.taskEvents++;
				
					if(correctEvent && !task.equals(ip.currentActivity))
						lastChunk.otherEvents++;
				}
				
				if(ip.caseData) //ACR
				{
					if(correctEvent && task.equals(ip.currentActivity) && correctData)
						lastChunk.taskEvents++;
		
					if(correctEvent && (!task.equals(ip.currentActivity) || !correctData))
						lastChunk.otherEvents++;
				}
				
			}
		}
		
		Double lastChunkNumberOfTimeUnits= (double) ((lastChunk.chunkEnd.getTime() - lastChunk.chunkStart.getTime())/ip.timeUnit);
		lastChunk.taskDensity = lastChunk.taskEvents/lastChunkNumberOfTimeUnits;
		lastChunk.otherDensity = lastChunk.otherEvents/lastChunkNumberOfTimeUnits;
		
		chunks.add(lastChunk);
		
		}

		return chunks;
	}
	
	
	
	public Vector<Chunk> getChunksAR(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(taskCP.size()>0)
		{	
			Chunk firstChunk = new Chunk();
			
			firstChunk.chunkStart = times.elementAt(0);
			firstChunk.chunkEnd = times.elementAt(taskCP.elementAt(0));
			
			chunks.add(firstChunk);
			
				for (int i=0; i<taskCP.size()-1; i++)
				{
					Chunk chunk = new Chunk();
					chunk.chunkStart = times.elementAt(taskCP.elementAt(i));
					chunk.chunkEnd = times.elementAt(taskCP.elementAt(i+1));
					chunks.add(chunk);
				}
			
			Chunk lastChunk = new Chunk();
			lastChunk.chunkStart = times.elementAt(taskCP.elementAt(taskCP.size()-1));
			
			Long endDateL = times.elementAt(times.size()-1).getTime() + (times.elementAt(times.size()-1).getTime()-times.elementAt(times.size()-2).getTime());
			Date endDate = new Date(endDateL);
			
			lastChunk.chunkEnd = endDate;
			
			chunks.add(lastChunk);
		}
		
		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			chunk.chunkResources.add(ip.currentResource);
			
			for (XTrace t : log) 
			{
				String caseID = t.getAttributes().get("concept:name").toString();
				
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
						
					boolean correctData = true;
					
					if(ip.caseData)
						for(String key : ip.bpData.keySet())
						{
							XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
							
							if(dataAttr != null)
							{
								if(!ip.bpData.get(key).contains(dataAttr.toString()))
									correctData = false;
							}
							else
							{
								correctData = false;
							}
						}
		
					Boolean correctEvent = type.equals(ip.eventType) && resource.equals(ip.currentResource) && 
							(eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd);
					
					if(!ip.caseData) //AR
					{
						if(correctEvent && task.equals(ip.currentActivity))
							{
								chunk.taskEvents++;
								chunk.caseIDs.add(caseID);
								
								if(chunk.chunkFirstEventTime == null)
									chunk.chunkFirstEventTime = eventTime;
								else
									if(eventTime.before(chunk.chunkFirstEventTime))
										chunk.chunkFirstEventTime = eventTime;
								
								if(chunk.chunkLastEventTime == null)
									chunk.chunkLastEventTime = eventTime;
								else
									if(eventTime.after(chunk.chunkLastEventTime))
										chunk.chunkLastEventTime = eventTime;

							}
					
						}
					
					if(ip.caseData) //ACR
					{
						if(correctEvent && task.equals(ip.currentActivity) && correctData)
							{
								chunk.taskEvents++;
								chunk.caseIDs.add(caseID);
								
								if(chunk.chunkFirstEventTime == null)
									chunk.chunkFirstEventTime = eventTime;
								else
									if(eventTime.before(chunk.chunkFirstEventTime))
										chunk.chunkFirstEventTime = eventTime;
								
								if(chunk.chunkLastEventTime == null)
									chunk.chunkLastEventTime = eventTime;
								else
									if(eventTime.after(chunk.chunkLastEventTime))
										chunk.chunkLastEventTime = eventTime;

								
							}
			
					}
				}
			}
			
			Long chunkDuration = chunk.chunkEnd.getTime() - chunk.chunkStart.getTime();
			
			
			if(ip.considerWorkingTime)
			{
				for(int j=0; j<nwt.size(); j++)
				{
					
					if((nwt.elementAt(j).chunkStart.after(chunk.chunkStart) || nwt.elementAt(j).chunkStart.equals(chunk.chunkStart)) 
						&& (nwt.elementAt(j).chunkEnd.before(chunk.chunkEnd) || nwt.elementAt(j).chunkEnd.equals(chunk.chunkEnd)))
						{
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkStart) && nwt.elementAt(j).chunkEnd.after(chunk.chunkStart))
						{
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - chunk.chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkEnd) && nwt.elementAt(j).chunkEnd.after(chunk.chunkEnd))
						{
							Long nwtChunkDur = chunk.chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
				}
			}
			
			Double chunkNumberOfTimeUnits = (double) chunkDuration/(double) ip.timeUnit;
			chunk.taskDensity = chunk.taskEvents/chunkNumberOfTimeUnits;
			
			chunks.set(i, chunk);
			
		}

		return chunks;
	}
	
	public BPTS getOverallStat(InputParametersBP ip, XLog log, Vector<Chunk> chunks, BPTS bpts)
	{
		AnalyseBP abp = new AnalyseBP();
		
		Long logDuration = ip.slotSizeWT*ip.numberOfSlotsWT;
		ip.startTimeGS = ip.startTimeWT;
		ip.numberOfSlotsGS = logDuration/ip.slotSizeGS+1;
		
		if(!ip.considerWorkingTime) //WT+NWT
		{
			bpts = abp.getTimesGS(ip, bpts);
		}
		else //WT
		{
			bpts = abp.getTimesGSWT(ip, bpts);
		}
		
		
		bpts = abp.getTaskTSGS(log, ip, bpts);
		
		bpts.gsMean = getMean(bpts.taskTSGS);
		bpts.gsStDev = getStDev(bpts.taskTSGS);
			
				
		return bpts;
		
		
		
	}

	public Vector<Chunk> getChunksA(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(taskCP.size()>0)
		{	
			Chunk firstChunk = new Chunk();
			
			firstChunk.chunkStart = times.elementAt(0);
			firstChunk.chunkEnd = times.elementAt(taskCP.elementAt(0));
			
			chunks.add(firstChunk);
			
				for (int i=0; i<taskCP.size()-1; i++)
				{
					Chunk chunk = new Chunk();
					chunk.chunkStart = times.elementAt(taskCP.elementAt(i));
					chunk.chunkEnd = times.elementAt(taskCP.elementAt(i+1));
					chunks.add(chunk);
				}
			
			Chunk lastChunk = new Chunk();
			lastChunk.chunkStart = times.elementAt(taskCP.elementAt(taskCP.size()-1));
			
			Long endDateL = times.elementAt(times.size()-1).getTime() + (times.elementAt(times.size()-1).getTime()-times.elementAt(times.size()-2).getTime());
			Date endDate = new Date(endDateL);
			
			lastChunk.chunkEnd = endDate;
			
			chunks.add(lastChunk);
		}
		
		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			
			for (XTrace t : log) 
			{
				String caseID = t.getAttributes().get("concept:name").toString();
				
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
						
					boolean correctData = true;
					
					if(ip.caseData)
						for(String key : ip.bpData.keySet())
						{
							XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
							
							if(dataAttr != null)
							{
								if(!ip.bpData.get(key).contains(dataAttr.toString()))
									correctData = false;
							}
							else
							{
								correctData = false;
							}
						}
		
					Boolean correctEvent = type.equals(ip.eventType) && 
							(eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
							eventTime.before(chunk.chunkEnd);
					
					if(!ip.caseData && correctEvent && task.equals(ip.currentActivity)) //A
					{
						chunk.taskEvents++;
						chunk.chunkResources.add(resource);		
						
						Date resourceStart = chunk.resourceStart.get(resource);
						Date resourceEnd = chunk.resourceEnd.get(resource);
						if(resourceStart == null)
							chunk.resourceStart.put(resource, eventTime);
						else
							if(eventTime.before(resourceStart))
								chunk.resourceStart.put(resource, eventTime);
						
						if(resourceEnd == null)
							chunk.resourceEnd.put(resource, eventTime);
						else
							if(eventTime.after(resourceEnd))
								chunk.resourceEnd.put(resource, eventTime);
						
						if(chunk.chunkFirstEventTime == null)
							chunk.chunkFirstEventTime = eventTime;
						else
							if(eventTime.before(chunk.chunkFirstEventTime))
								chunk.chunkFirstEventTime = eventTime;
						
						if(chunk.chunkLastEventTime == null)
							chunk.chunkLastEventTime = eventTime;
						else
							if(eventTime.after(chunk.chunkLastEventTime))
								chunk.chunkLastEventTime = eventTime;
			
						
						chunk.caseIDs.add(caseID);
					}
					
					if(ip.caseData && correctEvent && task.equals(ip.currentActivity) && correctData) //AC
					{
						chunk.taskEvents++;
						chunk.chunkResources.add(resource);
						
						Date resourceStart = chunk.resourceStart.get(resource);
						Date resourceEnd = chunk.resourceEnd.get(resource);
						if(resourceStart == null)
							chunk.resourceStart.put(resource, eventTime);
						else
							if(eventTime.before(resourceStart))
								chunk.resourceStart.put(resource, eventTime);
						
						if(resourceEnd == null)
							chunk.resourceEnd.put(resource, eventTime);
						else
							if(eventTime.after(resourceEnd))
								chunk.resourceEnd.put(resource, eventTime);
		
						if(chunk.chunkFirstEventTime == null)
							chunk.chunkFirstEventTime = eventTime;
						else
							if(eventTime.before(chunk.chunkFirstEventTime))
								chunk.chunkFirstEventTime = eventTime;
						
						if(chunk.chunkLastEventTime == null)
							chunk.chunkLastEventTime = eventTime;
						else
							if(eventTime.after(chunk.chunkLastEventTime))
								chunk.chunkLastEventTime = eventTime;
								
						chunk.caseIDs.add(caseID);
					}
				}
			}
			
			Long chunkDuration = chunk.chunkEnd.getTime() - chunk.chunkStart.getTime();
			
			if(ip.considerWorkingTime)
			{
				for(int j=0; j<nwt.size(); j++)
				{
					
					if((nwt.elementAt(j).chunkStart.after(chunk.chunkStart) || nwt.elementAt(j).chunkStart.equals(chunk.chunkStart)) 
						&& (nwt.elementAt(j).chunkEnd.before(chunk.chunkEnd) || nwt.elementAt(j).chunkEnd.equals(chunk.chunkEnd)))
						{
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkStart) && nwt.elementAt(j).chunkEnd.after(chunk.chunkStart))
						{
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - chunk.chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkEnd) && nwt.elementAt(j).chunkEnd.after(chunk.chunkEnd))
						{
							Long nwtChunkDur = chunk.chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
				}
			}
			
			Double chunkNumberOfTimeUnits = (double) chunkDuration/(double)ip.timeUnit;
			chunk.taskDensity = chunk.taskEvents/chunkNumberOfTimeUnits;
			
			chunks.set(i, chunk);
			
		}
		
		return chunks;
	}
	
	public Vector<Chunk> getChunksAOther(InputParametersBP ip, XLog log, Vector<Chunk> chunks)
	{

		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
						
					boolean correctData = true;
					
					if(ip.caseData)
						for(String key : ip.bpData.keySet())
						{
							XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
							
							if(dataAttr != null)
							{
								if(!ip.bpData.get(key).contains(dataAttr.toString()))
									correctData = false;
							}
							else
							{
								correctData = false;
							}
						}
		
					Boolean resourceInChunk = false;
					
					if(chunk.chunkResources.contains(resource))
						resourceInChunk = true;
					
			
					//V2: resource start and end in the chunk
					Boolean correctEvent = type.equals(ip.eventType) && resourceInChunk &&
							(eventTime.after(chunk.resourceStart.get(resource)) || eventTime.equals(chunk.resourceStart.get(resource))) && 
							(eventTime.before(chunk.resourceEnd.get(resource)) || eventTime.equals(chunk.resourceEnd.get(resource)));
					
					if(!ip.caseData && correctEvent && !task.equals(ip.currentActivity)) //A
					{
						chunk.otherEvents++;
						
						if(chunk.otherTasks.get(task) == null)
						{
							chunk.otherTasks.put(task, 1);
						}
						else
						{
							Integer count = chunk.otherTasks.get(task) + 1;
							chunk.otherTasks.put(task, count);
						}
					}
					
					if(ip.caseData && correctEvent && (!task.equals(ip.currentActivity) || !correctData)) //AC
					{
						chunk.otherEvents++;		
						
						if(chunk.otherTasks.get(task) == null)
						{
							chunk.otherTasks.put(task, 1);
						}
						else
						{
							Integer count = chunk.otherTasks.get(task) + 1;
							chunk.otherTasks.put(task, count);
						}
					}
				}
			}
			
			Long chunkDuration = chunk.chunkEnd.getTime() - chunk.chunkStart.getTime();
			
			
			if(ip.considerWorkingTime)
			{
				for(int j=0; j<nwt.size(); j++)
				{
						if((nwt.elementAt(j).chunkStart.after(chunk.chunkStart) || nwt.elementAt(j).chunkStart.equals(chunk.chunkStart)) 
						&& (nwt.elementAt(j).chunkEnd.before(chunk.chunkEnd) || nwt.elementAt(j).chunkEnd.equals(chunk.chunkEnd)))
						{
							
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkStart) && nwt.elementAt(j).chunkEnd.after(chunk.chunkStart))
						{
							
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - chunk.chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkEnd) && nwt.elementAt(j).chunkEnd.after(chunk.chunkEnd))
						{
							
							Long nwtChunkDur = chunk.chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
				}
			}
			
			Double chunkNumberOfTimeUnits = (double) (chunkDuration/ip.timeUnit);
			chunk.otherDensity = chunk.otherEvents/chunkNumberOfTimeUnits;
			
			chunks.set(i, chunk);
			
		}
		
		return chunks;
	}
	
	
	public Vector<Chunk> getChunksAROther(InputParametersBP ip, XLog log, Vector<Chunk> chunks)
	{

		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
						
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					String resource = e.getAttributes().get("org:resource").toString();
					String task = e.getAttributes().get("concept:name").toString();
					String type = e.getAttributes().get("lifecycle:transition").toString();
						
					boolean correctData = true;
					
					if(ip.caseData)
						for(String key : ip.bpData.keySet())
						{
							XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
							
							if(dataAttr != null)
							{
								if(!ip.bpData.get(key).contains(dataAttr.toString()))
									correctData = false;
							}
							else
							{
								correctData = false;
							}
						}
		
					Boolean resourceInChunk = false;
					
					if(ip.currentResource.equals(resource))
						resourceInChunk = true;
					
				
					
					//V3: first event start and end in the chunk
					
					if(resourceInChunk && chunk.chunkFirstEventTime != null && chunk.chunkLastEventTime != null)
					{
							Boolean correctEvent = type.equals(ip.eventType) &&  
							(eventTime.after(chunk.chunkFirstEventTime) || eventTime.equals(chunk.chunkFirstEventTime)) && 
							(eventTime.before(chunk.chunkLastEventTime) || eventTime.equals(chunk.chunkLastEventTime));
			
						if(!ip.caseData && correctEvent && !task.equals(ip.currentActivity)) //AR
						{
							chunk.otherEvents++;
							
							if(chunk.otherTasks.get(task) == null)
							{
								chunk.otherTasks.put(task, 1);
							}
							else
							{
								Integer count = chunk.otherTasks.get(task) + 1;
								chunk.otherTasks.put(task, count);
							}
						}
					
						if(ip.caseData && correctEvent && (!task.equals(ip.currentActivity) || !correctData)) //ACR
						{
							chunk.otherEvents++;	
							if(chunk.otherTasks.get(task) == null)
							{
								chunk.otherTasks.put(task, 1);
							}
							else
							{
								Integer count = chunk.otherTasks.get(task) + 1;
								chunk.otherTasks.put(task, count);
							}
						}
					}
					
				}
			}
			
			
			
			Long chunkDuration = chunk.chunkEnd.getTime() - chunk.chunkStart.getTime();
			
			
			if(ip.considerWorkingTime)
			{
				for(int j=0; j<nwt.size(); j++)
				{
					
					if((nwt.elementAt(j).chunkStart.after(chunk.chunkStart) || nwt.elementAt(j).chunkStart.equals(chunk.chunkStart)) 
						&& (nwt.elementAt(j).chunkEnd.before(chunk.chunkEnd) || nwt.elementAt(j).chunkEnd.equals(chunk.chunkEnd)))
						{
							
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkStart) && nwt.elementAt(j).chunkEnd.after(chunk.chunkStart))
						{
							
							Long nwtChunkDur = nwt.elementAt(j).chunkEnd.getTime() - chunk.chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
					
					if(nwt.elementAt(j).chunkStart.before(chunk.chunkEnd) && nwt.elementAt(j).chunkEnd.after(chunk.chunkEnd))
						{
							
							Long nwtChunkDur = chunk.chunkEnd.getTime() - nwt.elementAt(j).chunkStart.getTime();
							chunkDuration = chunkDuration - nwtChunkDur;
						}
				}
			}
			
			Double chunkNumberOfTimeUnits = (double) (chunkDuration/ip.timeUnit);
			chunk.otherDensity = chunk.otherEvents/chunkNumberOfTimeUnits;
			chunks.set(i, chunk);
			
		}
		
		return chunks;
	}
	
	public Vector<Chunk> getChunksOtherPart(Vector<Chunk> chunks)
	{
		for(int i=0; i<chunks.size(); i++)
		{
			if(chunks.elementAt(i).taskEvents != 0)
			chunks.elementAt(i).otherPart = (double) chunks.elementAt(i).otherEvents/ (double) chunks.elementAt(i).taskEvents;
			else
			chunks.elementAt(i).otherPart = -1.0;
		}
		return chunks;
	}
	
	public Vector<Chunk> getChunkNumberOfCases(Vector<Chunk> chunks)
	{
		for(int i=0; i<chunks.size(); i++)
		{
			Set<String> chunkCases = new HashSet<String>();
			chunkCases.addAll(chunks.elementAt(i).caseIDs);
			int uCases = chunkCases.size();
			chunks.elementAt(i).uniqueCases = uCases;
		
		}
		return chunks;
	}
	
	public Vector<Chunk> getChunkCaseUniqueness(Vector<Chunk> chunks)
	{
		for(int i=0; i<chunks.size(); i++)
		{
			Set<String> caseIDs = new HashSet<String>();
			caseIDs.addAll(chunks.elementAt(i).caseIDs);
			Double caseScore = (double) caseIDs.size() / (double) chunks.elementAt(i).caseIDs.size();
			chunks.elementAt(i).caseUniqueness = caseScore;
		}
		
		return chunks;
	}
	
	public Vector<Chunk> getWTChunkInfo(Vector<Chunk> chunks)
	{
		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			chunk.wtChunkStart = chunk.chunkStart;
			chunk.wtChunkEnd = chunk.chunkEnd;
			chunk.wtChunkDuration = (long) 0;
			
			boolean isNWC = false;
			
			for(int j=0; j<nwt.size(); j++)
			{
				Chunk nwtc = nwt.elementAt(j);
				
				if((nwtc.chunkStart.equals(chunk.chunkStart) || nwtc.chunkStart.before(chunk.chunkStart)) &&
						(nwtc.chunkEnd.equals(chunk.chunkEnd) || nwtc.chunkEnd.after(chunk.chunkEnd)))
				{
					isNWC = true;
					break;
				}
				
				if((nwtc.chunkStart.equals(chunk.chunkStart) || nwtc.chunkStart.before(chunk.chunkStart)) &&
						nwtc.chunkEnd.before(chunk.chunkEnd) && nwtc.chunkEnd.after(chunk.chunkStart))
				{
					chunk.wtChunkStart = nwtc.chunkEnd;
				}
				
				if((nwtc.chunkStart.after(chunk.chunkStart) && nwtc.chunkStart.before(chunk.chunkEnd)) &&
						(nwtc.chunkEnd.equals(chunk.chunkEnd) || nwtc.chunkEnd.after(chunk.chunkEnd)))
				{
					chunk.wtChunkEnd = nwtc.chunkStart;
				}
		
				if(!isNWC)
					chunk.wtChunkDuration = chunk.wtChunkEnd.getTime() - chunk.wtChunkStart.getTime();
				
			}
		
		chunks.elementAt(i).wtChunkStart = chunk.wtChunkStart;
		chunks.elementAt(i).wtChunkEnd= chunk.wtChunkEnd;
		chunks.elementAt(i).wtChunkDuration = chunk.wtChunkDuration;
			
		}
		
		//Remove nw time from durations
		
		for(int i=0; i<chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			
			if(chunk.wtChunkDuration > 0)
				for(int j=0; j<nwt.size(); j++)
				{
					Chunk nwtc = nwt.elementAt(j);
					
					if(nwtc.chunkStart.after(chunk.wtChunkStart) && nwtc.chunkEnd.before(chunk.wtChunkEnd))
					{
						Long nwtc_dur = nwtc.chunkEnd.getTime() - nwtc.chunkStart.getTime();
						chunks.elementAt(i).wtChunkDuration = chunks.elementAt(i).wtChunkDuration - nwtc_dur;
					}
				}
		}
		
		return chunks;
	}
	

	public Vector<Chunk> getChunksWT(InputParametersBP ip, XLog log)
	{
		Vector<Chunk> chunks = new Vector<Chunk>();
		
		if(WTCP.size()>0)
		{	
		Chunk firstChunk = new Chunk();
		
		firstChunk.chunkStart = timesWT.elementAt(0);
		firstChunk.chunkEnd = timesWT.elementAt(WTCP.elementAt(0));
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				
				if(ip.resource && e.getAttributes().get("org:resource") != null)
				{
					
					String resource = e.getAttributes().get("org:resource").toString();
					if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
							eventTime.before(firstChunk.chunkEnd) && resource.equals(ip.currentResource))
								firstChunk.wtEvents++;
				}
				
				if(!ip.resource)
				{
					if((eventTime.after(firstChunk.chunkStart) || eventTime.equals(firstChunk.chunkStart)) && 
							eventTime.before(firstChunk.chunkEnd))
								firstChunk.wtEvents++;
				}
			
			}
		}
		
		chunks.add(firstChunk);
		
		for (int i=0; i<WTCP.size()-1; i++)
		{
			Chunk chunk = new Chunk();
			chunk.chunkStart = timesWT.elementAt(WTCP.elementAt(i));
			chunk.chunkEnd = timesWT.elementAt(WTCP.elementAt(i+1));
			
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					
					XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
					Date eventTime = time.getValue();
					
					if(ip.resource && e.getAttributes().get("org:resource") != null)
					{
						String resource = e.getAttributes().get("org:resource").toString();
						if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
								eventTime.before(chunk.chunkEnd) && resource.equals(ip.currentResource))
									chunk.wtEvents++;
					}
					
					if(!ip.resource)
					{
						if((eventTime.after(chunk.chunkStart) || eventTime.equals(chunk.chunkStart)) && 
								eventTime.before(chunk.chunkEnd))
									chunk.wtEvents++;
					}
				
				}
			}
			
			chunks.add(chunk);
		}
		
		Chunk lastChunk = new Chunk();
		lastChunk.chunkStart = timesWT.elementAt(WTCP.elementAt(WTCP.size()-1));
		
		Long endDateL = timesWT.elementAt(timesWT.size()-1).getTime() + (timesWT.elementAt(timesWT.size()-1).getTime()-timesWT.elementAt(timesWT.size()-2).getTime());
		Date endDate = new Date(endDateL);
		
		lastChunk.chunkEnd = endDate;
		
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				
				if(ip.resource && e.getAttributes().get("org:resource") != null)
				{
					String resource = e.getAttributes().get("org:resource").toString();
					if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
							eventTime.before(lastChunk.chunkEnd) && resource.equals(ip.currentResource))
						lastChunk.wtEvents++;
				}
				
				if(!ip.resource)
				{
					if((eventTime.after(lastChunk.chunkStart) || eventTime.equals(lastChunk.chunkStart)) && 
							eventTime.before(lastChunk.chunkEnd))
						lastChunk.wtEvents++;
				}
				
			}
		}
		
		chunks.add(lastChunk);
		
		}
		return chunks;
	}
	

	public void storeChunksV1(Vector<Chunk> chunks, String filename) throws IOException
	{
		String filePath = "C:\\Temp\\"+filename+".csv";
		
		//csv lines
		Vector<String> lines = new Vector<String>();
		String title = "Chunk,chunkStart,chunkEnd,taskEvents,otherEvents,taskDensity,otherDensity,taskOverallDensity,otherOverallDensity\r\n";
		lines.add(title);
		
		for(int i=0; i<chunks.size(); i++)
		{
			String chunkLine =i+1+","+chunks.elementAt(i).chunkStart+","+chunks.elementAt(i).chunkEnd+","+
									chunks.elementAt(i).taskEvents+","+chunks.elementAt(i).otherEvents+","+
									chunks.elementAt(i).taskDensity+","+chunks.elementAt(i).otherDensity+","+
									chunks.elementAt(i).taskOverallDensity+","+chunks.elementAt(i).otherOverallDensity+"\r\n";
			
			lines.add(chunkLine);
			
		}
		
		//write csv lines to file
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, true);
	
		for(int i=0; i<lines.size(); i++)
			fw.write(lines.elementAt(i));
		
		fw.flush();
		fw.close();
	 	
		//return file;
	
	}
	
	
	//V1
	public void storeChunksV1(Vector<Chunk> chunks, String filename, Double score, Double threshold) throws IOException
	{
		String filePath = "C:\\Temp\\"+filename+".csv";
		
		//csv lines
		Vector<String> lines = new Vector<String>();
		String title = "#,Start,End,A,!A,A density,!A/A,batch\r\n";
		lines.add(title);
		
		for(int i=0; i<chunks.size(); i++)
		{
			String chunkLine =i+1+","+chunks.elementAt(i).chunkStart+","+chunks.elementAt(i).chunkEnd+","+
									chunks.elementAt(i).taskEvents+","+chunks.elementAt(i).otherEvents+","+
									chunks.elementAt(i).taskDensity+","+chunks.elementAt(i).otherPart+","+
									chunks.elementAt(i).isBP+"\r\n";
			
			lines.add(chunkLine);
			
		}
		
		lines.add("Overall BP score:,"+score+", threshold:,"+threshold);
		
		//write csv lines to file
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, true);
	
		for(int i=0; i<lines.size(); i++)
			fw.write(lines.elementAt(i));
		
		fw.flush();
		fw.close();
	 	
		//return file;
	
	}
	
	
	public void storeTS(String filename, String line) throws IOException
	{
		String filePath = "C:\\Temp\\"+filename+".csv";
		
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, true);
	
		fw.write(line);
		
		fw.flush();
		fw.close();
	 	
	}
	
	//V2
	public void storeChunksV2(Vector<Chunk> chunks, String filename) throws IOException
	{
		String filePath = "C:\\Temp\\"+filename+".csv";
		
		//csv lines
		Vector<String> lines = new Vector<String>();
		String title = "#,Start,End,Duration (hrs),A,!A,A density,A distance from mean,is Batch,# of unique cases,resources\r\n";
		lines.add(title);
		
		for(int i=0; i<chunks.size(); i++)
		{
			Double dur = (double)(chunks.elementAt(i).chunkDurationEnd.getTime() - chunks.elementAt(i).chunkDurationStart.getTime())/3600000;
			String durS = String.format("%.2f",dur);
			
			String resources = chunks.elementAt(i).chunkResources.toString();
			String res = resources.substring(1, resources.length()-1);
			
			
			String chunkLine =i+1+","+ chunks.elementAt(i).chunkDurationStart +","+ chunks.elementAt(i).chunkDurationEnd +","+
									durS +","+ chunks.elementAt(i).taskEvents +","+ chunks.elementAt(i).otherEvents +","+
									String.format("%.2f",chunks.elementAt(i).taskDensity) +","+ 
									String.format("%.2f",chunks.elementAt(i).distanceFromMean) +","+
									chunks.elementAt(i).isBP +","+ chunks.elementAt(i).uniqueCases +","+
									res+"\r\n";
			
			lines.add(chunkLine);
			
		}
		
		//write csv lines to file
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, true);
	
		for(int i=0; i<lines.size(); i++)
			fw.write(lines.elementAt(i));
		
		fw.flush();
		fw.close();
	 	
	}
	
	//V3
		public void storeChunks(Vector<Chunk> chunks, String filename) throws IOException
		{
			//String filePath = "C:\\Temp\\"+filename+".csv";
			String filePath = filename;
			
			//csv lines
			Vector<String> lines = new Vector<String>();
			String title = "#,Start,End,Duration (hrs),A,!A,A density,A distance from mean,is Batch,is Outlier,# of cases,cases,resources,!A tasks\r\n";
			lines.add(title);
			
			for(int i=0; i<chunks.size(); i++)
			{
				Double dur = (double)(chunks.elementAt(i).chunkDurationEnd.getTime() - chunks.elementAt(i).chunkDurationStart.getTime())/3600000;
				String durS = String.format("%.2f",dur);
				
				String resources = chunks.elementAt(i).chunkResources.toString();
				String res = resources.substring(1, resources.length()-1);
				res = res.replaceAll(",", ";");
				
				String cases = chunks.elementAt(i).caseIDs.toString();
				String c = cases.substring(1, cases.length()-1);
				c = c.replaceAll(",", ";");
				
				String tasks = chunks.elementAt(i).otherTasks.toString();
				String t = tasks.substring(1, tasks.length()-1);
				t = t.replaceAll(",", ";");
				
				
				String chunkLine =i+1+","+ chunks.elementAt(i).chunkDurationStart +","+ chunks.elementAt(i).chunkDurationEnd +","+
										durS +","+ chunks.elementAt(i).taskEvents +","+ chunks.elementAt(i).otherEvents +","+
										String.format("%.2f",chunks.elementAt(i).taskDensity) + "," + 
										String.format("%.2f",chunks.elementAt(i).distanceFromMean) + "," +
										chunks.elementAt(i).isBP +","+ chunks.elementAt(i).isOutlier + "," + 
										chunks.elementAt(i).uniqueCases + "," +
										c + "," + res + "," + t + "\r\n";
				
				lines.add(chunkLine);
				
			}
			
			//write csv lines to file
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file, true);
		
			for(int i=0; i<lines.size(); i++)
				fw.write(lines.elementAt(i));
			
			fw.flush();
			fw.close();
		 	
		}
		
		//V3
				public void storeChunksPublic(Vector<Chunk> chunks, String filename) throws IOException
				{
					//String filePath = "C:\\Temp\\"+filename+".csv";
					String filePath = filename;
					
					//csv lines
					Vector<String> lines = new Vector<String>();
					String title = "#,Start,End,Duration,Task completions,# of interruptions,Task density,Distance from the mean,is Batch,is Outlier,# of cases,cases,resources,interruptions\r\n";
					lines.add(title);
					
					for(int i=0; i<chunks.size(); i++)
					{
						Double dur = (double)(chunks.elementAt(i).chunkDurationEnd.getTime() - chunks.elementAt(i).chunkDurationStart.getTime())/3600000;
						String durS = String.format("%.2f",dur);
						
						String resources = chunks.elementAt(i).chunkResources.toString();
						String res = resources.substring(1, resources.length()-1);
						res = res.replaceAll(",", ";");
						
						String cases = chunks.elementAt(i).caseIDs.toString();
						String c = cases.substring(1, cases.length()-1);
						c = c.replaceAll(",", ";");
						
						String tasks = chunks.elementAt(i).otherTasks.toString();
						String t = tasks.substring(1, tasks.length()-1);
						t = t.replaceAll(",", ";");
						
						
						String chunkLine =i+1+","+ chunks.elementAt(i).chunkDurationStart +","+ chunks.elementAt(i).chunkDurationEnd +","+
												durS +","+ chunks.elementAt(i).taskEvents +","+ chunks.elementAt(i).otherEvents +","+
												String.format("%.2f",chunks.elementAt(i).taskDensity) + "," + 
												String.format("%.2f",chunks.elementAt(i).distanceFromMean) + "," +
												chunks.elementAt(i).isBP +","+ chunks.elementAt(i).isOutlier + "," + 
												chunks.elementAt(i).uniqueCases + "," +
												c + "," + res + "," + t + "\r\n";
						
						lines.add(chunkLine);
						
					}
					
					//write csv lines to file
					File file = new File(filePath);
					FileWriter fw = new FileWriter(file, true);
				
					for(int i=0; i<lines.size(); i++)
						fw.write(lines.elementAt(i));
					
					fw.flush();
					fw.close();
				 	
				}
	
	//Aggregate BP info
		public void storeBPInfo(String info, String filename) throws IOException
		{
			String filePath = "C:\\Temp\\"+filename+".txt";
			
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file, true);
			fw.write(info);
			fw.flush();
			fw.close();
		}

		
	Double getMedian(Vector<Integer> numbers)
	{
		Collections.sort(numbers);
		
		double median;
		
		if (numbers.size() % 2 == 0)
		    median = ((double)numbers.elementAt(numbers.size()/2) + (double)numbers.elementAt(numbers.size()/2 - 1))/2;
		else
		    median = (double)numbers.elementAt(numbers.size()/2);
		
		return median;
	}
	
	Double getMean(Vector<Integer> numbers)
	{
		double mean = 0;
		long sum = 0;
		
		for(int i=0; i<numbers.size(); i++)		
		{
			sum += numbers.elementAt(i);
		}
		
		mean = (double)sum/numbers.size();

	return mean;

	}
	
	Double getStDev(Vector<Integer> numbers)
	{
		double sd = 0;
		double average = getMean(numbers);
		
		for (int i = 0; i < numbers.size(); i++)
		{
		    sd += Math.pow((numbers.elementAt(i)-average),2)/(numbers.size()-1);
		}
		double standardDeviation = Math.sqrt(sd);
		
		return standardDeviation;
	}

}




