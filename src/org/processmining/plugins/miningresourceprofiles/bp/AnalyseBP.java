package org.processmining.plugins.miningresourceprofiles.bp;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.rosuda.JRI.Rengine;


public class AnalyseBP{
	
	public AnalyseBP() {} 
	
	public BPTS getTimes(InputParametersBP ip, BPTS bpts)
	{
		bpts.times.clear();
		
		for (int i=0; i<ip.numberOfSlots; i++)
		{
			Date next = new Date(ip.startTime+i*ip.slotSize);
			bpts.times.add(next);
		}
		
		return bpts;
	}
	
	public BPTS getTimesGS(InputParametersBP ip, BPTS bpts)
	{
		bpts.timesGS.clear();
		
		for (int i=0; i<ip.numberOfSlotsGS; i++)
		{
			Date next = new Date(ip.startTimeGS+i*ip.slotSizeGS);
			bpts.timesGS.add(next);
		}
		
		return bpts;
	}
	
	public BPTS getTimesBPWT(InputParametersBP ip, BPTS bpts)
	{
		bpts.times.clear();
		
		for (int i=0; i<ip.numberOfSlots; i++)
		{
			Date next = new Date(ip.startTime+i*ip.slotSize);
			Date nextEnd = new Date(ip.startTime+(i+1)*ip.slotSize);
			
			if(isWT(next,bpts))
			{
				bpts.times.add(next);
			}
			else
			{
				if(isWT(nextEnd,bpts))
					bpts.times.add(next);
			}
			
		}
		
		return bpts;
	}
	
	public BPTS getTimesGSWT(InputParametersBP ip, BPTS bpts)
	{
		bpts.timesGS.clear();
		
		for (int i=0; i<ip.numberOfSlotsGS; i++)
		{
			Date next = new Date(ip.startTimeGS+i*ip.slotSizeGS);
			Date nextEnd = new Date(ip.startTimeGS+(i+1)*ip.slotSizeGS);
			
			if(isWT(next,bpts))
			{
				bpts.timesGS.add(next);
			}
			else
			{
				if(isWT(nextEnd,bpts))
					bpts.timesGS.add(next);
			}
			
		}
		
		return bpts;
	}
	
	public Boolean isWT(Date d, BPTS bpts)
	{
		boolean iswt = true;
		
		for(int j=0; j<bpts.nwt.size(); j++)
		{
			if(d.after(bpts.nwt.elementAt(j).chunkStart) && d.before(bpts.nwt.elementAt(j).chunkEnd))
				iswt = false;
		}
		
		return iswt;
	}
	
	public BPTS getTimesWT(InputParametersBP ip, BPTS bpts)
	{
		for (int i=0; i<ip.numberOfSlotsWT; i++)
		{
			Date next = new Date(ip.startTimeWT+i*ip.slotSizeWT);
			bpts.timesWT.add(next);
		}
		
		return bpts;
	}
	
	public BPTS getTaskTS(XLog log, InputParametersBP ip, BPTS bpts)
	{
		bpts.taskTS.clear();
		
		
		for (int i=0; i<bpts.times.size(); i++)
			bpts.taskTS.add(0);
	
		
	Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				String resource = null;
				
				if(ip.logHasResources)
				resource = e.getAttributes().get("org:resource").toString();
				
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				//A
				if(!ip.resource && !ip.caseData)
				{
					if(type.equals(ip.eventType) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				//AR
				if(ip.resource && !ip.caseData)
				{
					if(type.equals(ip.eventType) && resource.equals(ip.currentResource) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				boolean addEvent = true;
				
				if(ip.caseData)
					for(String key : ip.bpData.keySet())
					{
						XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
						
						if(dataAttr != null)
						{
							if(!ip.bpData.get(key).contains(dataAttr.toString()))
								addEvent = false;
						}
						else
						{
							addEvent = false;
						}
					}
		
				
				//AC
				if(!ip.resource && ip.caseData && addEvent)
				{
					if(type.equals(ip.eventType) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				//ACR
				if(ip.resource && ip.caseData && addEvent)
				{
					if(type.equals(ip.eventType) && resource.equals(ip.currentResource) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				

			}
		}
		
	
		for(Long t: times)
		{
			int slot = getSlot(ip, t, bpts);
			
			if(slot > -1)
				bpts.taskTS.setElementAt(bpts.taskTS.elementAt(slot)+1, slot);
		}
		
		return bpts;
		
	}
	
	public boolean checkResourceTask(XLog log, InputParametersBP ip)
	{
		
	boolean result = false;
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if(ip.currentActivity.equals(task) && ip.currentResource.equals(resource) && ip.eventType.equals(type))		
				{
					result = true;
					break;
				}
			}
			
			if(result)
				break;
		}
		
		
		
		return result;
		
	}
	
	
	public BPTS getTaskTSGS(XLog log, InputParametersBP ip, BPTS bpts)
	{
		bpts.taskTSGS.clear();
		
		for (int i=0; i<bpts.timesGS.size(); i++)
			bpts.taskTSGS.add(0);
	
		
	Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				//A
				if(!ip.resource && !ip.caseData)
				{
					if(type.equals(ip.eventType) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				//AR
				if(ip.resource && !ip.caseData)
				{
					if(type.equals(ip.eventType) && resource.equals(ip.currentResource) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				boolean addEvent = true;
				
				if(ip.caseData)
					for(String key : ip.bpData.keySet())
					{
						XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
						
						if(dataAttr != null)
						{
							if(!ip.bpData.get(key).contains(dataAttr.toString()))
								addEvent = false;
						}
						else
						{
							addEvent = false;
						}
					}
		
				
				//AC
				if(!ip.resource && ip.caseData && addEvent)
				{
					if(type.equals(ip.eventType) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				
				//ACR
				if(ip.resource && ip.caseData && addEvent)
				{
					if(type.equals(ip.eventType) && resource.equals(ip.currentResource) && task.equals(ip.currentActivity))
						times.add(time.getValueMillis());
				}
				

			}
		}
		
		
		for(Long t: times)
		{
			int slot = getSlotGS(ip, t, bpts);
			
			if(slot > -1)
				bpts.taskTSGS.setElementAt(bpts.taskTSGS.elementAt(slot)+1, slot);
		}
		
		String out = bpts.taskTSGS.toString();
		out = out.replace("[", "");
		out = out.replace("]", "");
		out = out.replace(" ", "");
			
		return bpts;
		
	}
	

	
	public BPTS getWTTS(XLog log, InputParametersBP ip, BPTS bpts)
	{
		bpts.WTTS.clear();
		
		for (int i=0; i<ip.numberOfSlotsWT; i++)
			bpts.WTTS.add(0);
		
	Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				
				if(ip.resource && e.getAttributes().get("org:resource") != null)
				{
					String resource = e.getAttributes().get("org:resource").toString();
					if(resource.equals(ip.currentResource))
						times.add(time.getValueMillis());
				}
				
				if(!ip.resource)
				{
					times.add(time.getValueMillis());
				}	
			}
		}
		
		for(Long t: times)
		{
			int slot = getSlotWT(ip, t);
			bpts.WTTS.setElementAt(bpts.WTTS.elementAt(slot)+1, slot);
		}
		
		return bpts;
		
	}

	public BPTS getOtherTS(XLog log, InputParametersBP ip, BPTS bpts, int taskIndex, int resourceIndex)
	{
		
		for (int i=0; i<ip.numberOfSlots; i++)
			bpts.otherTS.add(0);
		
	Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				String resource = e.getAttributes().get("org:resource").toString();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if(type.equals(ip.eventType) && resource.equals(ip.bpResource.elementAt(resourceIndex)) && !task.equals(ip.bpActivity.elementAt(taskIndex)))
					times.add(time.getValueMillis());
		
			}
		}
		
		for(Long t: times)
		{
			int slot = getSlotV1(ip, t);
			bpts.otherTS.setElementAt(bpts.otherTS.elementAt(slot)+1, slot);
			
		}
		
		return bpts;
	}
	
	
	public InputParametersBP checkLog(XLog log, InputParametersBP ipbp)
	{
		
		boolean logOK = true;
		boolean resourcesOK = true;
		
		for (XTrace t : log) 
		{
			
			if(t.getAttributes().get("concept:name") == null)
				logOK = false;
			
			for (XEvent e : t) 
			{
				
				if(e.getAttributes().get("time:timestamp") == null)
					logOK = false;
				
				if(e.getAttributes().get("org:resource") == null)
					{
						//logOK = false; 
						resourcesOK = false;
					}
				
				if(e.getAttributes().get("concept:name") == null)
					logOK = false;
				
				if(e.getAttributes().get("lifecycle:transition") == null)
					logOK = false;
				
			}
		}
		
		ipbp.logOK = logOK;
		ipbp.logHasResources = resourcesOK;
		
		return ipbp;
	}

	
	public int getSlotV1(InputParametersBP ip, Long time) //for all time including nwt
	{
		int slot = (int) ((time - ip.startTime)/ip.slotSize);
		return slot;
	}
	
	public int getSlot(InputParametersBP ip, Long time, BPTS bpts) 
	{
		int slot = -1;
		Date cd = new Date(time);
		
		for(int i=0; i<bpts.times.size(); i++)
		{
			Date slotStart = bpts.times.elementAt(i);
			Date slotEnd = new Date(slotStart.getTime()+ip.slotSize);
			
			if((cd.after(slotStart) || cd.equals(slotStart) && cd.before(slotEnd)))
				slot = i;
		}
		
		return slot;
	}
	
	public int getSlotGS(InputParametersBP ip, Long time, BPTS bpts) 
	{
		int slot = -1;
		Date cd = new Date(time);
		
		for(int i=0; i<bpts.timesGS.size(); i++)
		{
			Date slotStart = bpts.timesGS.elementAt(i);
			Date slotEnd = new Date(slotStart.getTime()+ip.slotSizeGS);
			
			if((cd.after(slotStart) || cd.equals(slotStart) && cd.before(slotEnd)))
				slot = i;
		}
		
		return slot;
	}
	
	public int getSlotWT(InputParametersBP ip, Long time)
	{
		int slot = (int) ((time - ip.startTimeWT)/ip.slotSizeWT);
		return slot;
	}
	
	public Long getSlotStartTime(InputParametersBP ip, int slot)
	{
		Long time = ip.startTime + slot*ip.slotSize;
		return time;
	}


	public Vector<Integer> getCP(Rengine re, InputParametersBP ip, String ts)
	{
		Vector<Integer> changePoints = new Vector<Integer>();
		
		re.eval("library(cpm)");
		String y = "";
		String cpmType = ip.cpmType; 
		String ARL0 = ip.ARL0; 
		String startup = ip.startup; 
		
		ts = ts.replace("[", "");
		ts = ts.replace("]", "");
		ts = ts.replace(" ", "");
		
		y = "y <- c("+ts+")"; 
		re.eval(y);
		re.eval("res <- processStream(y, cpmType = \"" + cpmType +"\", ARL0 = " + ARL0 + ", startup = " + startup +")");
		double[] cp = re.eval("res$changePoints").asDoubleArray();
		
		for(int i=0; i<cp.length; i++)
			changePoints.add((int) cp[i]);
	
		return changePoints;
	}
	
    public InputParametersBP getThreshold(InputParametersBP ip, BPTS bpts)
    {
    	ip.batchTaskThreshold = bpts.gsMean + 2*bpts.gsStDev;
    	
    	return ip;
    }
    
    public InputParametersBP getChunkMeanThreshold(InputParametersBP ip, Vector<Chunk> chunks)
    {
    	Double sum = 0.0;
    	int count = 0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(!chunks.elementAt(i).isOutlier)
    		{
    		sum += chunks.elementAt(i).taskDensity;
    		count++;
    		}
    	}
    	
    	if(count > 0)
    		ip.batchTaskThreshold = sum/count;
     	
    	return ip;
    }
    
    public Double getPartBP(Vector<Chunk> chunks)
    {
    	Double partBP = 0.0;
    	Integer taskBP = 0;
    	Integer taskAll = 0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		taskAll += chunks.elementAt(i).taskEvents;
    		
    		if(chunks.elementAt(i).isBP)
    			taskBP += chunks.elementAt(i).taskEvents;
    	}
    	
    	partBP = (double)taskBP/taskAll;
    	
    	return partBP;
    }
    
    public Double getMeanBPDistance(Vector<Chunk> chunks)
    {
    	Double dist = 0.0;
    	Integer count = 0;
    	Double meanDist = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			dist += chunks.elementAt(i).distanceFromMean;
    			count++;
    		}
    	}
    	
    	meanDist = dist/(double)count;
    	
    	return meanDist;
    }
    
    public Double getMeanBatchSize(Vector<Chunk> chunks)
    {
    	Double batchSize = 0.0;
    	Integer count = 0;
    	Double meanBatchSize = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			batchSize += chunks.elementAt(i).taskEvents;
    			count++;
    		}
    	}
    	
    	meanBatchSize = batchSize/(double)count;
    	
    	return meanBatchSize;
    }
    
    public Double getMeanBPCases(Vector<Chunk> chunks)
    {
    	Double batchSize = 0.0;
    	Integer count = 0;
    	Double meanBatchSize = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			batchSize += chunks.elementAt(i).uniqueCases;
    			count++;
    		}
    	}
    	
    	meanBatchSize = batchSize/(double)count;
    	
    	return meanBatchSize;
    } 
    
    public Double getMeanInterruptions(Vector<Chunk> chunks)
    {
    	Double interruptions = 0.0;
    	Integer count = 0;
    	Double meanInterruptions = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			interruptions += chunks.elementAt(i).otherEvents;
    			count++;
    		}
    	}
    	
    	meanInterruptions = interruptions/(double)count;
    	
    	return meanInterruptions;
    }
    
    public Double getMeanInterruptionsPart(Vector<Chunk> chunks)
    {
    	Double interruptionsPart = 0.0;
    	Integer count = 0;
    	Double meanInterruptionsPart = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			interruptionsPart += (double)chunks.elementAt(i).otherEvents/(double)chunks.elementAt(i).taskEvents;
    			count++;
    		}
    	}
    	
    	meanInterruptionsPart = interruptionsPart/(double)count;
    	
    	return meanInterruptionsPart;
    }

    
    public Double getMeanDensity(Vector<Chunk> chunks)
    {
    	Double batchDensity = 0.0;
    	Integer count = 0;
    	Double meanBatchDensity = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			batchDensity += chunks.elementAt(i).taskDensity;
    			count++;
    		}
    	}
    	
    	meanBatchDensity = batchDensity/(double)count;
    	
    	return meanBatchDensity;
    }
    
    public Double getMeanNBPDistance(Vector<Chunk> chunks)
    {
    	Double dist = 0.0;
    	Integer count = 0;
    	Double meanDist = 0.0;
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(!chunks.elementAt(i).isBP)
    		{
    			dist += chunks.elementAt(i).distanceFromMean;
    			count++;
    		}
    	}
    	
    	meanDist = dist/(double)count;
    	
    	return meanDist;
    }
    
    //Consider only WT in durations
    public Vector<Double> getBPDurationStatWT(Vector<Chunk> chunks)
    {
    	
    	Vector<Double> stat = new Vector<Double>();
    	Vector<Long> bpDur = new Vector<Long>();
    	Vector<Long> nbpDur = new Vector<Long>();
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		if(chunks.elementAt(i).isBP)
    		{
    			bpDur.add(chunks.elementAt(i).wtChunkDuration/3600000);
    		}
    		else
    		{
    			nbpDur.add(chunks.elementAt(i).wtChunkDuration/3600000);
    		}
    	}
    	
    	stat.add(getMean(bpDur));
    	stat.add(getSTD(bpDur));
    	stat.add(getMean(nbpDur));
    	stat.add(getSTD(nbpDur));
    	
     	return stat;
    }
    
    //Consider chunk start and end times in durations
    public Vector<Double> getBPDurationStat(Vector<Chunk> chunks, InputParametersBP ip)
    {
    	
    	Vector<Double> stat = new Vector<Double>();
    	Vector<Long> bpDur = new Vector<Long>();
    	Vector<Long> nbpDur = new Vector<Long>();
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		
    		long chunkDuration = (chunks.elementAt(i).chunkEnd.getTime() - chunks.elementAt(i).chunkStart.getTime())/ip.timeUnit;
    		
    		if(chunks.elementAt(i).isBP)
    		{
    			bpDur.add(chunkDuration);
    		}
    		else
    		{
    			nbpDur.add(chunkDuration);
    		}
    	}
    	
    	stat.add(getMean(bpDur));
    	stat.add(getSTD(bpDur));
    	stat.add(getMean(nbpDur));
    	stat.add(getSTD(nbpDur));
    	
     	return stat;
    }

    //Consider chunkDurationEnd - chunkDurationStart
    public Vector<Double> getBPDurationStatDT(Vector<Chunk> chunks, InputParametersBP ip)
    {
    	
    	Vector<Double> stat = new Vector<Double>();
    	Vector<Double> bpDur = new Vector<Double>();
    	Vector<Double> nbpDur = new Vector<Double>();
    	
    	for(int i=0; i<chunks.size(); i++)
    	{
    		
    		double chunkDuration = (double)(chunks.elementAt(i).chunkDurationEnd.getTime() - chunks.elementAt(i).chunkDurationStart.getTime())/(double)ip.timeUnit;
    		
    		if(chunks.elementAt(i).isBP)
    		{
    			bpDur.add(chunkDuration);
    		}
    		else
    		{
    			nbpDur.add(chunkDuration);
    		}
    	}
    	
    	stat.add(getMeanDouble(bpDur));
    	stat.add(getSTDDouble(bpDur));
    	stat.add(getMeanDouble(nbpDur));
    	stat.add(getSTDDouble(nbpDur));
    	
     	return stat;
    }

 
    
    public Vector<Integer> getbptsSeasonalityV1(InputParametersBP ip, Vector<Chunk>chunks, BPTS bptsWT)
    {
    	Vector<Integer> bpts = new Vector<Integer>();
    	Vector<Chunk> wt = bptsWT.wt;
    	Vector<Chunk> nwt = bptsWT.nwt;
    	
     	for(int i=0; i<ip.numberOfSlotsS; i++)
    	{
     		Date slotStart = new Date(ip.startTimeS + i*ip.slotSizeS);
    		Date slotEnd = new Date(ip.startTimeS + (i+1)*ip.slotSizeS);
    		
    		boolean isBatch = false;
    		boolean isWT = true;
    		
    		for(int j=0; j<chunks.size(); j++)
    		{
    			if(chunks.elementAt(j).isBP)
        		{
    				
    			Date chunkStart = null;
    			Date chunkEnd = null;
    			
    			if(!ip.considerWorkingTime)
    			{
    				chunkStart = chunks.elementAt(j).chunkStart;
    				chunkEnd = chunks.elementAt(j).chunkEnd;
    			}
    			else
    			{
    				chunkStart = chunks.elementAt(j).wtChunkStart;
    				chunkEnd = chunks.elementAt(j).wtChunkEnd;
    			}
    		
    			
    			if(slotStart.equals(chunkStart) || slotStart.after(chunkStart) && slotStart.before(chunkEnd) ||
    					slotEnd.equals(chunkEnd) ||	slotEnd.after(chunkStart) && slotEnd.before(chunkEnd) ||
    					slotStart.before(chunkStart) && slotEnd.after(chunkEnd))
    				{
    					isBatch = true;
    					break;
    				}
        		}
        	}
    		
    
    		if (ip.considerWorkingTime)
    		{
    			for(int j=0; j<nwt.size(); j++)
    			{
    				Date nwtStart = nwt.elementAt(j).chunkStart;
    				Date nwtEnd = nwt.elementAt(j).chunkEnd;
    			
     				if((slotStart.equals(nwtStart) || slotStart.after(nwtStart)) && 
    						(slotEnd.before(nwtEnd) || slotEnd.equals(nwtEnd)))
    					{
    						isWT = false;
    						break;
    					}
    			}
    		}

    		if (ip.considerWorkingTime)
    		{
    			if(isBatch && isWT)
    				bpts.add(1);
    			else
    				bpts.add(0);
    		}
    		else
    		{
    			if(isBatch)
    				bpts.add(1);
    			else
    				bpts.add(0);
    		}
    	}
    	
    	return bpts;
    }
    

    // V2 consider first and last event times
    public Vector<Integer> getbptsSeasonality(InputParametersBP ip, Vector<Chunk>chunks)
    {
    	Vector<Integer> bpts = new Vector<Integer>();
    	
    	for(int i=0; i<ip.numberOfSlotsS; i++)
    	{
     		Date slotStart = new Date(ip.startTimeS + i*ip.slotSizeS);
    		Date slotEnd = new Date(ip.startTimeS + (i+1)*ip.slotSizeS);
    		
    		boolean isBatch = false;
    		
    		for(int j=0; j<chunks.size(); j++)
    		{
    			if(!ip.considerOutliers && chunks.elementAt(j).isBP || ip.considerOutliers && chunks.elementAt(j).isOutlier)
        		{
    				
    			Date batchStart = chunks.elementAt(j).chunkFirstEventTime;
    			Date batchEnd = chunks.elementAt(j).chunkLastEventTime;
    			
    			if(slotStart.equals(batchStart) || slotStart.after(batchStart) && slotStart.before(batchEnd) ||
    					slotEnd.equals(batchEnd) ||	slotEnd.after(batchStart) && slotEnd.before(batchEnd) ||
    					slotStart.before(batchStart) && slotEnd.after(batchEnd))
    				{
    					isBatch = true;
    					break;
    				}
        		}
        	}
    		
    			if(isBatch)
    				bpts.add(1);
    			else
    				bpts.add(0);
    		
    	}
    	
    	return bpts;
    }
    
 
    public Vector<Integer> getbptsSeasonalityBatchStartsV1(InputParametersBP ip, Vector<Chunk>chunks, BPTS bptsWT)
    {
    	Vector<Integer> bpts = new Vector<Integer>();
    	Vector<Chunk> wt = bptsWT.wt;
    	Vector<Chunk> nwt = bptsWT.nwt;
    	
    	for(int i=0; i<ip.numberOfSlotsS; i++)
    	{
    		
    		Date slotStart = new Date(ip.startTimeS + i*ip.slotSizeS);
    		Date slotEnd = new Date(ip.startTimeS + (i+1)*ip.slotSizeS);
    		
    		boolean isBatch = false;
    		
    		for(int j=0; j<chunks.size(); j++)
    		{
    			if(chunks.elementAt(j).isBP)
        		{
    			Date chunkStart = null;
    			
    			if(ip.considerWorkingTime)
    				chunkStart = chunks.elementAt(j).wtChunkStart;
    			else
    				chunkStart = chunks.elementAt(j).chunkStart;
    			
   			if(chunkStart.equals(slotStart) || chunkStart.after(slotStart) && chunkStart.before(slotEnd))
    				{
    					isBatch = true;
    					break;
    				}
        		}
        	}
    		
   			if(isBatch)
    				bpts.add(1);
    			else
    				bpts.add(0);
    		}
    	
    	return bpts;
    }
    
    
    //V2 Using first event times
    public Vector<Integer> getbptsSeasonalityBatchStarts(InputParametersBP ip, Vector<Chunk>chunks)
    {
    	Vector<Integer> bpts = new Vector<Integer>();
     	
    	for(int i=0; i<ip.numberOfSlotsS; i++)
    	{
    		
    		Date slotStart = new Date(ip.startTimeS + i*ip.slotSizeS);
    		Date slotEnd = new Date(ip.startTimeS + (i+1)*ip.slotSizeS);
    		
    		boolean isBatch = false;
     		
    		for(int j=0; j<chunks.size(); j++)
    		{
    			if(!ip.considerOutliers && chunks.elementAt(j).isBP || ip.considerOutliers && chunks.elementAt(j).isOutlier)
        		{
    			Date batchStart = chunks.elementAt(j).chunkFirstEventTime;
    			
      			if(batchStart.equals(slotStart) || batchStart.after(slotStart) && batchStart.before(slotEnd))
    				{
    					isBatch = true;
    					break;
    				}
        		}
        	}
    		
   			if(isBatch)
    				bpts.add(1);
    			else
    				bpts.add(0);
    		}
    	
    	return bpts;
    }
    
   
    public InputParametersBP setSeasonalityTS(InputParametersBP ip, long slotSize)
    {
     	
     	Date date = new Date(ip.startTime);
     	
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
            
       Date newdate = calendar.getTime();
       
       ip.startTimeS = newdate.getTime();
       ip.slotSizeS = slotSize; 
     	
       Long logDur = ip.slotSize*ip.numberOfSlots; 
       ip.numberOfSlotsS = logDur/ip.slotSizeS;
    	
     	return ip;
    }
    
    public Vector<Double> getCorrelations(Rengine re, InputParametersBP ip, Vector<Integer> tsv)
   {
	    
    	String ts = tsv.toString();
    	
    	Vector<Double> correlations = new Vector<Double>();
		re.eval("library(forecast)");
		
		String y = "";
		
		ts = ts.replace("[", "");
		ts = ts.replace("]", "");
		ts = ts.replace(" ", "");
		
		y = "y <- c("+ts+")"; 
		re.eval(y);
		
		Integer maxLag = tsv.size()/2;
				
		re.eval("ts <- ts(y, frequency = 8760, start = c(1970))");
		re.eval("ts2 <- diff(ts, lag=1)"); 
		re.eval("res <- Acf(ts2,lag.max="+maxLag+",plot=FALSE)");
			
		double[] cor = re.eval("res$acf").asDoubleArray();
		
		for(int i=0; i<cor.length; i++)
			correlations.add(cor[i]);
		
	   return correlations;
   }

    public Vector<Double> getMaxCor(Vector<Double> correlations)
    {
	   Vector<Double> maxCor = new  Vector<Double>();
	   
	   Double maxC = -10.0;
	   Double maxI = -1.0;
	   
	   for(int i=1; i<correlations.size(); i++)
	   {
		   if(correlations.elementAt(i) > maxC)
		   {
			   maxC = correlations.elementAt(i);
			   maxI = (double) i;
		   }
	   }
	   
	   maxCor.add(maxI);
	   maxCor.add(maxC);
	   
	   return maxCor;
    }


    public HashMap<Integer,Vector<Double>> getRepetitions(Vector<Integer> bpts, Integer period, int numberOfBatches)
    {
    	HashMap<Integer,Vector<Double>> repetitions = new HashMap<Integer,Vector<Double>>();
    	HashMap<Integer,Integer> bp = new HashMap<Integer,Integer>();
    	HashMap<Integer,Integer> nbp = new HashMap<Integer,Integer>();
    	
    	for(int i=1; i<=period; i++)
    	{
    		bp.put(i, 0);
    		nbp.put(i, 0);
    	}
    	
    	for(int i=0; i<bpts.size(); i++)
    	{
    		int mod = (int) ((i+1) % period);
    		int lag;
    		
    		if(mod == 0)
    			lag = period;
    		else
    			lag = mod;
    		
    		if(bpts.elementAt(i) == 1)
    			bp.put(lag, bp.get(lag)+1);
    		else
    			nbp.put(lag, nbp.get(lag)+1);
    	}
    	
        for(int i=1; i<period+1; i++)
        {
        	Vector<Double> scores = new Vector<Double>();
        	
        	// % of periods with BP
        	Double periodScore = (double) (bp.get(i)/(double) (bp.get(i)+nbp.get(i)));
        	
        	// % of batches active during period
        	Double batchScore = (double) (bp.get(i)/(double) numberOfBatches);
        	
        	Double combinedScore = periodScore * batchScore;
        	
        	scores.add(combinedScore);
        	scores.add(batchScore);
        	scores.add(periodScore);
        	
        	repetitions.put(i, scores);
        }
    	
    	return repetitions;
    }
    
    public HashMap<Integer,Vector<Double>> getLargeRepetitions(HashMap<Integer,Vector<Double>> rep, InputParametersBP ip)
    {
    	HashMap<Integer,Vector<Double>> largeRep = new HashMap<Integer,Vector<Double>>();
    	Set<Integer> keysS = new HashSet<Integer>();
    	keysS = rep.keySet();
    	
    	for(Integer key=1; key<=keysS.size(); key++)
    	{
    		if(rep.get(key).elementAt(0) >= ip.periodicityRepetitionPeriodThreshold)
    			largeRep.put(key, rep.get(key));
    	}
    	
     	return largeRep;
    }
    
	
	Double getMean(Vector<Long> numbers)
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
	
	Double getMeanDouble(Vector<Double> numbers)
	{
		double mean = 0;
		double sum = 0;
		
		for(int i=0; i<numbers.size(); i++)		
		{
			sum += numbers.elementAt(i);
		}
		
		mean = sum/numbers.size();

	return mean;

	}

	
	Double getSTD(Vector<Long> numbers)
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
	
	Double getSTDDouble(Vector<Double> numbers)
	{
		double sd = 0;
		double average = getMeanDouble(numbers);
		
		for (int i = 0; i < numbers.size(); i++)
		{
		    sd += Math.pow((numbers.elementAt(i)-average),2)/(numbers.size()-1);
		}
		double standardDeviation = Math.sqrt(sd);
		
		return standardDeviation;
	}

	
	public XLog removeLoops(XLog log, InputParametersBP ip)
	{
		XLogImpl logCopy = (XLogImpl) log.clone();
		
		logCopy.removeAll(logCopy);
		
		
		for (XTrace t : log) 
		{
			XTraceImpl traceCopy = (XTraceImpl) t.clone();
			
			traceCopy.removeAll(traceCopy);
			
			HashMap<String,Vector<Date>> taskTimes = new HashMap<String,Vector<Date>>();
			Set<String> tasks = new HashSet<String>();
			HashMap<String,Date> lastTaskTimes = new HashMap<String,Date>();
			Set<String> addedTasks = new HashSet<String>();
			
			for (XEvent e : t) 
			{
				
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				
				if(type.equals(ip.eventType))
				{
					tasks.add(task);
					if(taskTimes.get(task) == null)
					{
						Vector<Date> times = new Vector<Date>();
						times.add(eventTime);
						taskTimes.put(task, times);
					}
					else
					{	
						taskTimes.get(task).add(eventTime);
					}
				}
			}
			
			for(String a: tasks)
			{
				Collections.sort(taskTimes.get(a));
			}
			
			for(String a: tasks)
			{
				lastTaskTimes.put(a,taskTimes.get(a).elementAt(taskTimes.get(a).size()-1));
			}
			
			
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				Date eventTime = time.getValue();
				String task = e.getAttributes().get("concept:name").toString();
				String type = e.getAttributes().get("lifecycle:transition").toString();
				
				if(type.equals(ip.eventType))
				{
					if(!addedTasks.contains(task))
					{
						Date eTime = lastTaskTimes.get(task);
						if(eTime.equals(eventTime))
						{
							traceCopy.add(e);
							addedTasks.add(task);
							
						}
					}
				}
			}
		
			
		logCopy.add(traceCopy);
			
		}
		
		
		return logCopy;
	}
	
	public Vector<Chunk> detectOutliers (Rengine re, InputParametersBP ip, Vector<Chunk> chunks) throws Exception
	{
		
		String ts = "";
		
		for(int i=0; i<chunks.size()-1; i++)
			ts += chunks.elementAt(i).taskDensity + ",";
		
		ts += chunks.elementAt(chunks.size()-1).taskDensity;
		
		Vector<Integer> out = getOutliers(re, ip, ts);
		
		for(int i=0; i<out.size(); i++)
			chunks.elementAt(out.elementAt(i)).isOutlier = true;
		
		return chunks;
	}
	

	public Vector<Integer> getOutliers(Rengine re, InputParametersBP ip, String ts) throws Exception
	{
		re.eval("library(extremevalues)");
		String y = "";
		String method = ip.method;
			
		y = "y <- c("+ts+")"; 
		re.eval(y);
		re.eval("res <- getOutliers(y, method = \"" + method +"\")");
		double[] outr = re.eval("res$iRight").asDoubleArray();
		
		Vector<Integer> out = new Vector<Integer>();
		
		if (outr != null) 
			for(int i=0; i<outr.length; i++)
				out.add((int) (outr[i] - 1));
				
		
		return out;

	}
	
	public Vector<Chunk> mergeChunks(Vector<Chunk> chunks, InputParametersBP ip)
	{
		Vector<Chunk> mergedChunks = new Vector<Chunk>();
		Vector<Chunk> temp = new Vector<Chunk>();
		
		int c = 1;
			
		while(true)
		{
			temp.removeAllElements();
			
			if(chunks.size() > 0)
			{
				temp.add(chunks.elementAt(0));
				chunks.remove(0);
			}
			else
				break;
						
			int counter = 0;
			
			for(int i=0; i<chunks.size(); i++)
			{
				if(chunks.elementAt(i).isBP == temp.elementAt(0).isBP)
				{
					temp.add(chunks.elementAt(i));
					counter ++;
				}
				else
					break;
			}
			
			for(int i=0; i<counter; i++)
				chunks.remove(0);
			
			if(temp.size() == 1)
				mergedChunks.add(temp.elementAt(0));
			else
			{
				Chunk mergedChunk = new Chunk();
				
				mergedChunk.chunkStart = temp.elementAt(0).chunkStart;
				mergedChunk.chunkEnd = temp.elementAt(temp.size()-1).chunkEnd;
				
				mergedChunk.chunkFirstEventTime = temp.elementAt(0).chunkFirstEventTime;
				mergedChunk.chunkLastEventTime = temp.elementAt(temp.size()-1).chunkLastEventTime;
				
				mergedChunk.isBP = temp.elementAt(0).isBP;
				
				mergedChunk.taskEvents = 0;
				mergedChunk.otherEvents = 0;
				mergedChunk.wtChunkDuration = (long) 0;
				mergedChunk.taskDensity = 0.0;
				mergedChunk.distanceFromMean = 0.0;
				
				for(int i=0; i<temp.size(); i++)
				{
					
					if(temp.elementAt(i).isOutlier)
						mergedChunk.isOutlier = true;
					
					mergedChunk.taskEvents += temp.elementAt(i).taskEvents;
					mergedChunk.otherEvents += temp.elementAt(i).otherEvents;
					mergedChunk.wtChunkDuration += temp.elementAt(i).wtChunkDuration;
					mergedChunk.taskDensity += temp.elementAt(i).taskDensity;
					mergedChunk.distanceFromMean += temp.elementAt(i).distanceFromMean;
					mergedChunk.caseIDs.addAll(temp.elementAt(i).caseIDs);
					mergedChunk.chunkResources.addAll(temp.elementAt(i).chunkResources);
					
					for (String key : temp.elementAt(i).otherTasks.keySet())
					{
						if(mergedChunk.otherTasks.get(key) == null)
						{
							mergedChunk.otherTasks.put(key, temp.elementAt(i).otherTasks.get(key));
						}
						else
						{
							Integer count = mergedChunk.otherTasks.get(key) + temp.elementAt(i).otherTasks.get(key);
							mergedChunk.otherTasks.put(key,count);
						}
					}
				}
				
				mergedChunk.taskDensity = mergedChunk.taskDensity/temp.size();
				mergedChunk.distanceFromMean = mergedChunk.distanceFromMean/temp.size();
				
				Set<String> uCaseIDs = new HashSet<String>();
				uCaseIDs.addAll(mergedChunk.caseIDs);
				mergedChunk.uniqueCases = uCaseIDs.size();
				
				//TODO
				//version where batch size = number of events
				
				mergedChunk.numberOfCases = mergedChunk.uniqueCases;
				
				if(ip.bsEvents)
				{
					mergedChunk.uniqueCases = mergedChunk.taskEvents;
				}
				
				mergedChunks.add(mergedChunk);
			}
					
		}
		
		return mergedChunks;
	}
	
	public Vector<Chunk> getDurationStartEndTimes(Vector<Chunk> chunks)
	{
		//For NBP chunks
		if(chunks.size() == 1)
		{
			chunks.elementAt(0).chunkDurationStart = chunks.elementAt(0).chunkStart;
			chunks.elementAt(0).chunkDurationEnd = chunks.elementAt(0).chunkEnd;
		}
		else
		{
			//first chunk
			if(chunks.elementAt(0).isBP)
				chunks.elementAt(0).chunkDurationStart = chunks.elementAt(0).chunkFirstEventTime;
			else
				chunks.elementAt(0).chunkDurationStart = chunks.elementAt(0).chunkStart;
			
			if(chunks.elementAt(1).isBP)
				chunks.elementAt(0).chunkDurationEnd = chunks.elementAt(1).chunkFirstEventTime;
			else
				chunks.elementAt(0).chunkDurationEnd = chunks.elementAt(1).chunkStart;
			
			//last chunk
			if(chunks.elementAt(chunks.size()-1).isBP)
				chunks.elementAt(chunks.size()-1).chunkDurationEnd = chunks.elementAt(chunks.size()-1).chunkLastEventTime;
			else
				chunks.elementAt(chunks.size()-1).chunkDurationEnd = chunks.elementAt(chunks.size()-1).chunkEnd;
			
			if(chunks.elementAt(chunks.size()-2).isBP)
				chunks.elementAt(chunks.size()-1).chunkDurationStart = chunks.elementAt(chunks.size()-2).chunkLastEventTime;
			else
				chunks.elementAt(chunks.size()-1).chunkDurationStart = chunks.elementAt(chunks.size()-2).chunkEnd;
			
			//other chunks
			for(int i=1; i<chunks.size()-1; i++)
			{
				Chunk prev = chunks.elementAt(i-1);
				Chunk next = chunks.elementAt(i+1);
				
				if(prev.isBP)
					chunks.elementAt(i).chunkDurationStart = prev.chunkLastEventTime;
				else
					chunks.elementAt(i).chunkDurationStart = prev.chunkEnd;
				
				if(next.isBP)
					chunks.elementAt(i).chunkDurationEnd = next.chunkFirstEventTime;
				else
					chunks.elementAt(i).chunkDurationEnd = next.chunkStart;
			}
				
		}
		
		for(int i=0; i<chunks.size(); i++)
		{
			if(chunks.elementAt(i).isBP)
			{
				chunks.elementAt(i).chunkDurationStart = chunks.elementAt(i).chunkFirstEventTime;
				chunks.elementAt(i).chunkDurationEnd = chunks.elementAt(i).chunkLastEventTime;
			}
		}
		
		return chunks;
	}
	

	public BPOUT bpAnalysis(Vector<Chunk> chunks, BPTS bpts, InputParametersBP ip, Rengine re, FilterBP filter)
	{
		AnalyseBP abp = new AnalyseBP();
		String bpInfo = "";
		String periodicity = "";
		BPOUT bpout = new BPOUT();
		bpout.task = ip.currentActivity;
		bpout.resource = ip.currentResource;
		
		Integer numberOfOutliers = 0;
		int numberOfBatches = 0;
		
		
		bpout.chunksOriginal.addAll(chunks);
		bpout.re = re;
		bpout.bpts = bpts;
		bpout.filter = filter;
		
////////////////////Labelling chunks as BP/NBP////////////////////////////////////////////////////////////

		
		for(int i=0; i<chunks.size(); i++)
		{
			chunks.elementAt(i).isBP = false;
			
			Boolean interruptionsOK = false;
			
			if(!filter.checkInterruptions)
				interruptionsOK = true;
			
			if(filter.checkInterruptions && chunks.elementAt(i).otherPart <= filter.interruptionTolerance)
				interruptionsOK = true;
			
			if(chunks.elementAt(i).distanceFromMean >= filter.minDistFromMean && 
					interruptionsOK && chunks.elementAt(i).uniqueCases >= filter.minBatchSize &&
					chunks.elementAt(i).taskDensity >= filter.minEvDensity && 
					chunks.elementAt(i).taskDensity <= filter.maxEvDensity &&
					chunks.elementAt(i).duration >= filter.minDur &&
					chunks.elementAt(i).duration <= filter.maxDur)
			chunks.elementAt(i).isBP = true;
			
		}
		
		
		if(filter.mergeChunks)
		{
			chunks = abp.mergeChunks(chunks,ip);
		
			for(int i=0; i<chunks.size(); i++)
			{
				chunks.elementAt(i).duration = (double) (chunks.elementAt(i).chunkEnd.getTime() - chunks.elementAt(i).chunkStart.getTime()) / (double)ip.timeUnit;
			
				if(chunks.elementAt(i).duration > filter.maxDur)
					chunks.elementAt(i).isBP = false;
				
				}
		}
		
		
		
/////////////////////////////////////////////////////////////////////////////////

		for(int i=0; i<chunks.size(); i++)
		{
			if(chunks.elementAt(i).isOutlier)
				numberOfOutliers++;
			
			if(chunks.elementAt(i).isBP)
				numberOfBatches++;
		}
				
		Double bpScore = abp.getPartBP(chunks);
		
		if(chunks.size()>0)
			chunks = abp.getDurationStartEndTimes(chunks);
		
		
		Double meanBPDist = abp.getMeanBPDistance(chunks); 
		Double meanNBPDist = abp.getMeanNBPDistance(chunks); 
		Double meanBPCases = abp.getMeanBPCases(chunks); 
		Double meanInterruptionsPart = abp.getMeanInterruptionsPart(chunks); 
		Double meanDensity = abp.getMeanDensity(chunks); 
		Vector<Double> distStatDT = abp.getBPDurationStatDT(chunks,ip); //Chunk chunkDurationEnd - chunkDurationStart
			
		bpInfo = "<html><h3>Batch Processing Analysis</h3>";
		bpInfo += "BP score: " + String.format("%.3f", bpScore) + "<br>";
		bpInfo += "Number of batches: " + numberOfBatches + "<br>";
		bpInfo += "Mean batch size: " + String.format("%.3f", meanBPCases) + "<br>";
		bpInfo += "Mean interruption ratio (%): " + String.format("%.3f", meanInterruptionsPart*100) + "<br>";
		bpInfo += "Mean batch event density (per selected time unit): " + String.format("%.3f", meanDensity) + "<br>";
		bpInfo += "Mean event density ratio: <br>";
		bpInfo += "BP periods: " + String.format("%.3f", meanBPDist) + "<br>";
		bpInfo += "NBP periods: " + String.format("%.3f", meanNBPDist) + "<br>";
		
		bpInfo += "Durations (selected time unit): <br>";
		bpInfo += "BP periods: "+String.format("%.4f",distStatDT.elementAt(0)) + " (mean), " +
				String.format("%.4f",distStatDT.elementAt(1)) + " (STD)<br>NBP periods: " +
				String.format("%.4f",distStatDT.elementAt(2)) + " (mean), " +
				String.format("%.4f",distStatDT.elementAt(3)) +" (STD)";
		
		bpInfo += "</html>";
		
		
		//Seasonality-------------------------------------------------------------------------
		
		if(ip.detectSeasonality)
		{
		
		//1. Get BP TS for a given time unit, out: Vector<Integer>
		
		Vector<Integer> bptsSeasonality = new Vector<Integer>();
		
		long bestSlot = 0;
		double bestCorrelation = -10.0;
		int bestPeriod = 0;
		Vector<Integer> bestBPTS = new Vector<Integer>();
		
		
		for(int i=0; i<ip.slotSizesS.size(); i++)
		{
		
		ip = abp.setSeasonalityTS(ip, ip.slotSizesS.elementAt(i));
		
		if(ip.periodicityConsiderBatchStart)
			bptsSeasonality = abp.getbptsSeasonalityBatchStarts(ip,chunks); //only batch starts
		else					
			bptsSeasonality	= abp.getbptsSeasonality(ip,chunks); //all batch
		
			
		//2. Get autocorrelations, out: HashMap<Integer,Double> (lag,correlation)
		Vector<Double> correlations = abp.getCorrelations(re, ip, bptsSeasonality);
		

		Vector<Double> maxCor = abp.getMaxCor(correlations);
		
		Integer period = (int) Math.round(maxCor.elementAt(0));
		
		if(maxCor.elementAt(1) > bestCorrelation)
		{
			bestSlot = ip.slotSizeS;
			bestCorrelation = maxCor.elementAt(1);
			bestPeriod = period;
			bestBPTS = bptsSeasonality;
		}
		
		}
		
		
		//4. Explain seasonality					
		HashMap<Integer,Vector<Double>> repetitions = new HashMap<Integer,Vector<Double>>();
		
		repetitions = abp.getRepetitions(bestBPTS,bestPeriod,numberOfBatches);					
		
		HashMap<Integer,Vector<Double>> largeRep = abp.getLargeRepetitions(repetitions, ip);
		
		
		String reps = "";
		
		Vector<Integer> keys = new Vector<Integer>();
		keys.addAll(largeRep.keySet());
		Collections.sort(keys);
		
		for(Integer key: keys)
		{
			reps += "\r\n"+ key + ": " + String.format("%.2f", largeRep.get(key).elementAt(0)) + ", " + 
					String.format("%.2f", largeRep.get(key).elementAt(1)) + ", " + 
					String.format("%.2f", largeRep.get(key).elementAt(2));
			
		}
		
		String slot = "";
		
		if(bestSlot/3600000 == 1)
			slot = " hour/s";
		
		if(bestSlot/3600000 == 24)
			slot = " day/s";
		
		if(bestSlot/3600000 == 168)
			slot = " week/s";
		
		periodicity = "<html><h3>Periodic pattern: </h3><br>";
		periodicity +=  "Start time: " + new Date(ip.startTimeS) + "<br>";
		
		double bestCor = 0.0;
		if(bestCorrelation > -10.0)
			bestCor = bestCorrelation;
			
		periodicity +=  "Repetition period: " + bestPeriod + slot + " (correlation: " +String.format("%.3f", bestCor)+")" + "<br>";
		
		if(keys.size() > 0)
			periodicity +=  "Repeated on " + slot +": " + reps;
		
		periodicity += "</html>";
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////
			
		bpout.chunks = chunks;
		bpout.ip = ip;
		bpout.BPInfo = bpInfo;
		bpout.periodicity = periodicity;
				
		return bpout;
	}


public void generateTS(InputParametersBP ip, BPTS bpts, DefineBPparameters defBP, AnalyseBP abp, VisualiseBP vbp, XLog logP, Rengine re) throws Exception
{
	bpts = abp.getTaskTS(logP, ip, bpts);
}
	

	public BPOUT findBP(InputParametersBP ip, BPTS bpts, DefineBPparameters defBP, AnalyseBP abp, VisualiseBP vbp, XLog logP, Rengine re) throws Exception
	{
	
		Vector<Chunk> chunks = new Vector<Chunk>();
		
	
		if(ip.showTSCheckBox)
			ip = defBP.defineTSParamsBP(ip, logP, bpts); //user can change ts size
		else
			ip = defBP.defineTSParamsBPNoInput(ip, logP, bpts); // ts -> median
		
			
		if(ip.slotSize > -1)
		{
		
		
		if(!ip.considerWorkingTime)
			bpts = abp.getTimes(ip, bpts);
		else
			bpts = abp.getTimesBPWT(ip, bpts);
		
		bpts = abp.getTaskTS(logP, ip, bpts);
		
	
		bpts.taskCP = abp.getCP(re, ip, bpts.taskTS.toString());
		
		
		if(ip.resource)	
		{
			chunks = bpts.getChunksAR(ip,logP);
			chunks = bpts.getChunksAROther(ip,logP,chunks);
		}
		else	
		{
	
			chunks = bpts.getChunksA(ip,logP);
			
			if(ip.logHasResources)
				chunks = bpts.getChunksAOther(ip,logP,chunks);
			
		}
		
		
		
	if(chunks.size() > 0)
	{
		if(ip.detectOutliers && chunks.size() >= ip.minNumberOfChunksForOutliers)
			chunks = abp.detectOutliers(re, ip, chunks);
		
		chunks = bpts.getChunksOtherPart(chunks);
		chunks = bpts.getChunkNumberOfCases(chunks);
		
		if(ip.considerWorkingTime)
			chunks = bpts.getWTChunkInfo(chunks);
		
	
		//V2, threshold: mean of chunk means
		ip = abp.getChunkMeanThreshold(ip, chunks);
		
		for(int i=0; i<chunks.size(); i++)
		{
			chunks.elementAt(i).distanceFromMean = chunks.elementAt(i).taskDensity/ip.batchTaskThreshold;
			chunks.elementAt(i).duration = (double) (chunks.elementAt(i).chunkEnd.getTime() - chunks.elementAt(i).chunkStart.getTime()) / (double)ip.timeUnit;
		}
		
		
		ip = defBP.getBPparametersMinDistance(ip,chunks,vbp.plotMinDistances(ip, chunks));
		
		
		Double cutOffthreshold = ip.batchTaskThreshold * ip.minDistanceFromMean;
		
		//TODO
			for(int i=0; i<chunks.size(); i++)
			{
				chunks.elementAt(i).numberOfCases = chunks.elementAt(i).uniqueCases;
				
				//version where batch size = number of events
				if(ip.bsEvents)
				{
				chunks.elementAt(i).uniqueCases = chunks.elementAt(i).taskEvents;
				}
			}
		
		
		//Labelling chunks as BP/NBP
		for(int i=0; i<chunks.size(); i++)
		{
				
			if(ip.checkInterruptions)
			{
				if(chunks.elementAt(i).taskDensity >= cutOffthreshold && 
						chunks.elementAt(i).otherPart <= ip.interruptionsTolerance &&
								chunks.elementAt(i).uniqueCases >= ip.minNumberOfCases)
										chunks.elementAt(i).isBP = true;
			}
			else
			{
				if(chunks.elementAt(i).taskDensity >= cutOffthreshold && chunks.elementAt(i).uniqueCases >= ip.minNumberOfCases)
						chunks.elementAt(i).isBP = true;
			}
		}
		
	}
		else
	{
		System.out.println("No CP detected");
		//bpts.storeBPInfo("No CP detected", "0 - no CP - " + ip.currentActivity + " - " + ip.currentResource);
	}
	}
		else
		{
			if(ip.slotSize == -1)
			{
				System.out.println("Zero time between events");
				//bpts.storeBPInfo("Zero time between events", "0 - zero time between events - " + ip.currentActivity + " - " + ip.currentResource);
			}
			
			if(ip.slotSize == -2)
			{			
				System.out.println("No or few events");
				//bpts.storeBPInfo("No or few events", "0 - no or few events - " + ip.currentActivity + " - " + ip.currentResource);
			}
			}


//Initial filter values
FilterBP filter = new FilterBP();
filter.checkInterruptions = ip.checkInterruptions;
filter.interruptionTolerance = ip.interruptionsTolerance;
filter.minBatchSize = ip.minNumberOfCases;
filter.minDistFromMean = ip.minDistanceFromMean;
filter.mergeChunks = ip.mergeChunks;


return abp.bpAnalysis(chunks, bpts,  ip,  re, filter);
}

}




