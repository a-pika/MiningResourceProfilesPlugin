package org.processmining.plugins.miningresourceprofiles.bp;


public class FilterBP{
	
	public String chartInfo;
	public Boolean checkInterruptions;
	public Double interruptionTolerance;
	public Integer minBatchSize;
	public Double minDistFromMean;
	public Double minDur;
	public Double maxDur;
	public Double minEvDensity;
	public Double maxEvDensity;
	public Boolean mergeChunks;
	
	
	public FilterBP()
	{
		chartInfo = "Distance from the mean ED";
		checkInterruptions = false;
		interruptionTolerance = 0.0;
		minBatchSize = 0;
		minDistFromMean = 0.0;
		mergeChunks = false;
		minDur = 0.0;
		maxDur = 99999999999999999999999999999999999999999999999999.0;
		minEvDensity = 0.0;
		maxEvDensity = 99999999999999999999999999999999999999999999999999.0;
		
	}
	
	public void FilterPrint()
	{
		System.out.println("Filter");
		System.out.println(chartInfo);
		System.out.println(checkInterruptions);
		System.out.println(interruptionTolerance);
		System.out.println(minBatchSize);
		System.out.println(minDistFromMean);
		System.out.println(mergeChunks);
		System.out.println(minDur);
		System.out.println(maxDur);
		System.out.println(minEvDensity);
		System.out.println(maxEvDensity);
		
	}
	
}




