package org.processmining.plugins.miningresourceprofiles.bp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

public class VisualiseBP{
	
	public VisualiseBP() {} 
	
		public ChartPanel plotBars(InputParametersBP ip, Vector<Chunk> chunks, String chartType) throws Exception
		{
			
			//Create data set
	        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
	        final String series1 = "Not batch";
	        final String series2 = "Batch";
	        final String series3 = "Batch-Outlier";
	   
	        for (int i=0; i<chunks.size(); i++)
	        {
	        	Double val = 0.0;
	        	
	        	//////////////////////////////////////////////////
	        	
	        	if(chartType.equals("Distance from the mean ED"))
	        		val = chunks.elementAt(i).distanceFromMean;
	        	
	        	if(chartType.equals("Duration"))
	        		val = chunks.elementAt(i).duration;
	        	
	        	if(chartType.equals("Event Density"))
	        		val = chunks.elementAt(i).taskDensity;
	        	
	        	if(chartType.equals("Size"))
	        		val = (double) chunks.elementAt(i).uniqueCases;
	        	
	        	if(chartType.equals("Interruptions"))
	        		val = chunks.elementAt(i).otherPart*100;
	        	
	        	//////////////////////////////////////////////////
	        	
	        	Date start = chunks.elementAt(i).chunkStart;
	        	Date end = chunks.elementAt(i).chunkEnd;
	        	String time = start.toString() + " - " + end.toString();
	        	Boolean isBatch = chunks.elementAt(i).isBP;
	        	Boolean isOutlier = chunks.elementAt(i).isOutlier;
	        	
	        	if(!isBatch)
	        		dataset.addValue(val, series1, time);
	        	
	        	if(isBatch && !isOutlier)
	        		dataset.addValue(val, series2, time);
	        	
	        	if(isBatch && isOutlier)
	        		dataset.addValue(val, series3, time);
	        }
	        
	     // create the chart...
	        final JFreeChart chart = ChartFactory.createBarChart(
	            "",         // chart title
	            "",               // domain axis label
	            "",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );

	  
	        // set the background color for the chart...
	        chart.setBackgroundPaint(Color.white);

	        // get a reference to the plot for further customisation...
	        final CategoryPlot plot = chart.getCategoryPlot();
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        
	        // disable bar outlines...
	        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        
	        // set up gradient paints for series...
	        final GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.blue, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        final GradientPaint gp1 = new GradientPaint(
	            0.0f, 0.0f, Color.green, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        final GradientPaint gp2 = new GradientPaint(
	            0.0f, 0.0f, Color.red, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);
	        renderer.setSeriesPaint(2, gp2);

	        final CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setVisible(false);
	        
			return(new ChartPanel(chart));
			
		}
		
		public ChartPanel plotMinDistances(InputParametersBP ip, Vector<Chunk> chunks) throws Exception
		{
			
			//Create data set
	        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        final String series1 = "Chunk distances from the mean event density";
	    
	        for (int i=0; i<chunks.size(); i++)
	        {
	        	Double val = 0.0;
	        	val = chunks.elementAt(i).distanceFromMean;
	         	
	        	Date start = chunks.elementAt(i).chunkStart;
	        	Date end = chunks.elementAt(i).chunkEnd;
	        	String time = start.toString() + " - " + end.toString();
	         	dataset.addValue(val, series1, time);
	        }
	        
	     // create the chart...
	        final JFreeChart chart = ChartFactory.createBarChart(
	            "",         // chart title
	            "",               // domain axis label
	            "",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );

	        // set the background color for the chart...
	        chart.setBackgroundPaint(Color.white);

	        // get a reference to the plot for further customisation...
	        final CategoryPlot plot = chart.getCategoryPlot();
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        
	        // disable bar outlines...
	        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        
	        // set up gradient paints for series...
	        final GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.blue, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        final GradientPaint gp1 = new GradientPaint(
	            0.0f, 0.0f, Color.green, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        final GradientPaint gp2 = new GradientPaint(
	            0.0f, 0.0f, Color.red, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);
	        renderer.setSeriesPaint(2, gp2);

	        final CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setVisible(false);
	        
	        domainAxis.setCategoryLabelPositions(
	            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
	        );
		
			return(new ChartPanel(chart));
	
		}
		
	
	public TimeSeries createTS(Vector<Integer> ts_values, BPTS bpts)
	{
		TimeSeries ts = null;
		Vector<Date> ts_times = bpts.times;
		ts = new TimeSeries("");
		
		for(int i=0; i<ts_values.size(); i++)
			ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));

		return ts;
	}
	
	public TimeSeries createNONBPTSv1(BPTS bpts)
	{
		Vector<Integer> ts_values = bpts.taskTS;
		TimeSeries ts = null;
		Vector<Date> ts_times = bpts.times;
		ts = new TimeSeries("Not batch");
		
		for(int i=0; i<ts_values.size(); i++)
			if(ts_values.elementAt(i) > 0)
			ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));

		return ts;
	}
	
	public TimeSeries createNONBPTS(Vector<Chunk> chunks, BPTS bpts)
	{
		Vector<Integer> ts_values = bpts.taskTS;
		TimeSeries ts = null;
		Vector<Date> ts_times = bpts.times;
		ts = new TimeSeries("Not batch");
		
		for(int i=0; i<ts_values.size(); i++)
			for(int j=0; j<chunks.size(); j++)
			{
				if((ts_times.elementAt(i).equals(chunks.elementAt(j).chunkStart) || 
						ts_times.elementAt(i).after(chunks.elementAt(j).chunkStart) 
								&& ts_times.elementAt(i).before(chunks.elementAt(j).chunkEnd)) && 
								!chunks.elementAt(j).isBP && ts_values.elementAt(i) > 0)				
										ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));
			}
			

		return ts;
	}
	
	public TimeSeries createBPTS(Vector<Chunk> chunks, BPTS bpts)
	{
		Vector<Integer> ts_values = bpts.taskTS;
		TimeSeries ts = null;
		Vector<Date> ts_times = bpts.times;
		ts = new TimeSeries("Batch");
		
		for(int i=0; i<ts_values.size(); i++)
			for(int j=0; j<chunks.size(); j++)
			{
				if((ts_times.elementAt(i).equals(chunks.elementAt(j).chunkStart) || 
						ts_times.elementAt(i).after(chunks.elementAt(j).chunkStart) 
								&& ts_times.elementAt(i).before(chunks.elementAt(j).chunkEnd)) && 
								chunks.elementAt(j).isBP && !chunks.elementAt(j).isOutlier && ts_values.elementAt(i) > 0)				
										ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));
			}
			

		return ts;
	}
	
	public TimeSeries createBPoutTS(Vector<Chunk> chunks, BPTS bpts)
	{
		Vector<Integer> ts_values = bpts.taskTS;
		TimeSeries ts = null;
		Vector<Date> ts_times = bpts.times;
		ts = new TimeSeries("Batch-outlier");
		
		for(int i=0; i<ts_values.size(); i++)
			for(int j=0; j<chunks.size(); j++)
			{
				if((ts_times.elementAt(i).equals(chunks.elementAt(j).chunkStart) || 
						ts_times.elementAt(i).after(chunks.elementAt(j).chunkStart) 
								&& ts_times.elementAt(i).before(chunks.elementAt(j).chunkEnd)) && 
								chunks.elementAt(j).isBP && chunks.elementAt(j).isOutlier && ts_values.elementAt(i) > 0)				
										ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));
			}
			

		return ts;
	}
	
	public TimeSeries createCPTS(Vector<Integer> ts_values, BPTS bpts, InputParametersBP ip)
	{
		TimeSeries ts = null;
		Vector<Date> ts_times = new Vector<Date>();
		AnalyseBP abp = new AnalyseBP();
		
		for(int i=0; i<ts_values.size(); i++)
			ts_times.add(new Date(abp.getSlotStartTime(ip, ts_values.elementAt(i))));
		
		ts = new TimeSeries("");
		
		for(int i=0; i<ts_values.size(); i++)
			ts.add(new Second(ts_times.elementAt(i)),0);

		return ts;
	}
	
	@SuppressWarnings("deprecation")
	public ChartPanel plotTS(InputParametersBP ip, TimeSeries ts, TimeSeries cp, String titleIn) throws Exception
	{
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		ts_dataset.addSeries(ts);
		
		TimeSeriesCollection cp_dataset = new TimeSeriesCollection();
		cp_dataset.addSeries(cp);
		
		String title = titleIn;
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"Events", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		XYPlot xyplot = chart.getXYPlot();
	
		int i=0;
		
			xyplot.setDataset(i,cp_dataset);
			XYLineAndShapeRenderer rr0 = new XYLineAndShapeRenderer();
			rr0.setSeriesLinesVisible(0, false);
			rr0.setSeriesShapesVisible(0, true);
			rr0.setPaint(Color.MAGENTA);//yellow
			rr0.setSeriesShape(0,ShapeUtilities.createUpTriangle(6.0f));
			chart.getXYPlot().setRenderer(i++,rr0);
			
			xyplot.setDataset(i,ts_dataset);
			XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
			rr2.setSeriesLinesVisible(0, true);
			rr2.setSeriesShapesVisible(0, false);
			rr2.setPaint(Color.blue);
			chart.getXYPlot().setRenderer(i++,rr2);
		return(new ChartPanel(chart));
	}
	
	
	@SuppressWarnings("deprecation")
	public ChartPanel plotBPTS(TimeSeries nonbpts, TimeSeries bpts, TimeSeries bpoutts) throws Exception
	{
		TimeSeriesCollection nonbpts_dataset = new TimeSeriesCollection();
		nonbpts_dataset.addSeries(nonbpts);
		
		TimeSeriesCollection bpts_dataset = new TimeSeriesCollection();
		bpts_dataset.addSeries(bpts);
		
		TimeSeriesCollection bpoutts_dataset = new TimeSeriesCollection();
		bpoutts_dataset.addSeries(bpoutts);
		
		String title = "";
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"Events", // y-axis Label
				nonbpts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		XYPlot xyplot = chart.getXYPlot();
		
		int i=0;
	
		if(bpoutts.getItemCount() > 0)
		{
			xyplot.setDataset(i,bpoutts_dataset);
			XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
			rr1.setSeriesLinesVisible(0, false);
			rr1.setSeriesShapesVisible(0, true);
			rr1.setPaint(Color.red);
			rr1.setSeriesShape(0,ShapeUtilities.createUpTriangle(4.0f));
			chart.getXYPlot().setRenderer(i++,rr1);
		}
		
		if(bpts.getItemCount() > 0)
		{
			xyplot.setDataset(i,bpts_dataset);
			XYLineAndShapeRenderer rr0 = new XYLineAndShapeRenderer();
			rr0.setSeriesLinesVisible(0, false);
			rr0.setSeriesShapesVisible(0, true);
			rr0.setPaint(Color.green);
			rr0.setSeriesShape(1,ShapeUtilities.createUpTriangle(4.0f));
			chart.getXYPlot().setRenderer(i++,rr0);
		}
			
		xyplot.setDataset(i,nonbpts_dataset);
		XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
		rr2.setSeriesLinesVisible(0, false);
		rr2.setSeriesShapesVisible(0, true);
		rr2.setPaint(Color.blue);
		rr2.setSeriesShape(2,ShapeUtilities.createUpTriangle(4.0f));
		chart.getXYPlot().setRenderer(i++,rr2);
	
		return(new ChartPanel(chart));
	}

	public void visualizeBP(final HeaderBar mainPane, final GridBagConstraints cMain, final int widthMenu, final Vector<BPOUT> bpouts) throws Exception
	{
		
		VisualiseBP visRes = new VisualiseBP();
		final AnalyseBP abp = new AnalyseBP();
		
		int imgWBP = (int) (widthMenu * 4.5);
		int imgHBP = (int) (widthMenu * 0.8);
		cMain.anchor = GridBagConstraints.WEST;
		
		final FilterBP filter = bpouts.elementAt(0).filter;
		
		Set<Integer> batchSizes = new HashSet<Integer>();
		Set<Double> allInterruptions = new HashSet<Double>();
		Set<Double> distFromMean = new HashSet<Double>();
		Set<Double> durations = new HashSet<Double>();
		Set<Double> eventDensities = new HashSet<Double>();
		
		Set<Double> minDurations = new HashSet<Double>();
		Set<Double> minEventDensities = new HashSet<Double>();
		
		Set<Double> maxDurations = new HashSet<Double>();
		Set<Double> maxEventDensities = new HashSet<Double>();
		
		for(int i=0; i<bpouts.size(); i++)
		{   
			Vector<Chunk> chunks = bpouts.elementAt(i).chunksOriginal;
			
			for(int j=0; j<chunks.size(); j++)
			{
				Chunk ch = chunks.elementAt(j);
				
				if(ch.uniqueCases>=2) batchSizes.add(ch.uniqueCases);
				if(ch.otherPart>=0) allInterruptions.add(ch.otherPart);
				distFromMean.add(ch.distanceFromMean);
				durations.add(ch.duration);
				eventDensities.add(ch.taskDensity);
				
			}
		}
		
		batchSizes.remove(filter.minBatchSize);
		allInterruptions.remove(filter.interruptionTolerance);
		distFromMean.remove(filter.minDistFromMean);
		minEventDensities.addAll(eventDensities);
		minEventDensities.remove(filter.minEvDensity);
		maxEventDensities.addAll(eventDensities);
		maxEventDensities.remove(filter.maxEvDensity);
		minDurations.addAll(durations);
		minDurations.remove(filter.minDur);
		maxDurations.addAll(durations);
		maxDurations.remove(filter.maxDur);
		
		Vector<Integer> bs = new Vector<Integer>();
		bs.addAll(batchSizes);
		Collections.sort(bs);
		
		Vector<Double> dFromMean = new Vector<Double>();
		dFromMean.addAll(distFromMean);
		Collections.sort(dFromMean);
		
		Vector<Double> inter = new Vector<Double>();
		inter.addAll(allInterruptions);
		Collections.sort(inter);
		
		Vector<Double> minD = new Vector<Double>();
		minD.addAll(minDurations);
		Collections.sort(minD);
		
		Vector<Double> maxD = new Vector<Double>();
		maxD.addAll(maxDurations);
		Collections.sort(maxD);
		
		Vector<Double> minED = new Vector<Double>();
		minED.addAll(minEventDensities);
		Collections.sort(minED);
		
		Vector<Double> maxED = new Vector<Double>();
		maxED.addAll(maxEventDensities);
		Collections.sort(maxED);
		
		int x = 100;
		int y = 10;
		
		//top - configuration
		//Labels
		
		JLabel chartInfoLabel = new JLabel("<html><h10>Chart information:</h10></html>");
		chartInfoLabel.setForeground(UISettings.TextLight_COLOR);
		chartInfoLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 0;
		cMain.gridy = 0;
		mainPane.add(chartInfoLabel, cMain);
		
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<String>();
		model1.addElement(filter.chartInfo);
		if(!filter.chartInfo.equals("Distance from the mean ED")) model1.addElement("Distance from the mean ED");
		if(!filter.chartInfo.equals("Duration")) model1.addElement("Duration");
		//if(!filter.chartInfo.equals("Event density")) model1.addElement("Event density");
		if(!filter.chartInfo.equals("Size")) model1.addElement("Size");
		//if(!filter.chartInfo.equals("Interruptions")) model1.addElement("Interruptions");
		final ProMComboBox<String> BPinfo = new ProMComboBox<String>(model1);
	    BPinfo.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 0;
		cMain.gridy = 1;
		mainPane.add(BPinfo, cMain);
		
		JLabel interruptionsLabel = new JLabel("<html><h10>Interruptions tolerance:</h10></html>");
		interruptionsLabel.setForeground(UISettings.TextLight_COLOR);
		interruptionsLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 0;
		cMain.gridy = 2;
		mainPane.add(interruptionsLabel, cMain);
		
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<String>();
		
		if(!filter.checkInterruptions) 
			model2.addElement("Unlimited");
		else
		{
			Double tol = filter.interruptionTolerance*100;
			model2.addElement(tol.toString()+"%");
		}
		for(int i=0; i<inter.size(); i++)
		{
			Double tol = inter.elementAt(i)*100;
			model2.addElement(tol.toString()+"%");
		}
		
		if(filter.checkInterruptions) 
			model2.addElement("Unlimited");
		
		final ProMComboBox<String> interruptions = new ProMComboBox<String>(model2);
		interruptions.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 0;
		cMain.gridy = 3;
		mainPane.add(interruptions, cMain);
		
		
		JLabel batchSizeLabel = new JLabel("<html><h10>Min. number of cases:</h10></html>");
		batchSizeLabel.setForeground(UISettings.TextLight_COLOR);
		batchSizeLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 1;
		cMain.gridy = 0;
		mainPane.add(batchSizeLabel, cMain);
		
		DefaultComboBoxModel<String> model3 = new DefaultComboBoxModel<String>();
		
		model3.addElement(filter.minBatchSize.toString());
		for(int i=0; i<bs.size(); i++)
			model3.addElement(bs.elementAt(i).toString());
		
		
		final ProMComboBox<String> batchSize = new ProMComboBox<String>(model3);
		batchSize.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 1;
		cMain.gridy = 1;
		mainPane.add(batchSize, cMain);


		JLabel distanceLabel = new JLabel("<html><h10>Min. distance from the mean event density:</h10></html>");
		distanceLabel.setForeground(UISettings.TextLight_COLOR);
		distanceLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 1;
		cMain.gridy = 2;
		mainPane.add(distanceLabel, cMain);
		
		DefaultComboBoxModel<String> model4 = new DefaultComboBoxModel<String>();
		model4.addElement(filter.minDistFromMean.toString());
		for(int i=0; i<dFromMean.size(); i++)
			model4.addElement(dFromMean.elementAt(i).toString());
	
	
		final ProMComboBox<String> distanceFromMean = new ProMComboBox<String>(model4);
		distanceFromMean.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 1;
		cMain.gridy = 3;
		mainPane.add(distanceFromMean, cMain);
		
		
		JLabel minBatchDurLabel = new JLabel("<html><h10>Min. duration:</h10></html>");
		minBatchDurLabel.setForeground(UISettings.TextLight_COLOR);
		minBatchDurLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 2;
		cMain.gridy = 0;
		mainPane.add(minBatchDurLabel, cMain);
		
		DefaultComboBoxModel<String> model5 = new DefaultComboBoxModel<String>();
		
		model5.addElement(filter.minDur.toString());
		for(int i=0; i<minD.size(); i++)
			model5.addElement(minD.elementAt(i).toString());
	
		
		final ProMComboBox<String> minDur = new ProMComboBox<String>(model5);
		minDur.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 2;
		cMain.gridy = 1;
		mainPane.add(minDur, cMain);
		
		JLabel maxBatchDurLabel = new JLabel("<html><h10>Max. duration:</h10></html>");
		maxBatchDurLabel.setForeground(UISettings.TextLight_COLOR);
		maxBatchDurLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 2;
		cMain.gridy = 2;
		mainPane.add(maxBatchDurLabel, cMain);
		
		DefaultComboBoxModel<String> model6 = new DefaultComboBoxModel<String>();
		if(filter.maxDur == 99999999999999999999999999999999999999999999999999.0)
			model6.addElement("Unlimited");
		else
			model6.addElement(filter.maxDur.toString());
		
		for(int i=0; i<maxD.size(); i++)
			model6.addElement(maxD.elementAt(i).toString());
	
		
		final ProMComboBox<String> maxDur = new ProMComboBox<String>(model6);
		maxDur.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 2;
		cMain.gridy = 3;
		mainPane.add(maxDur, cMain);

		JLabel minDensityLabel = new JLabel("<html><h10>Min. event density:</h10></html>");
		minDensityLabel.setForeground(UISettings.TextLight_COLOR);
		minDensityLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 3;
		cMain.gridy = 0;
		mainPane.add(minDensityLabel, cMain);
		
		DefaultComboBoxModel<String> model7 = new DefaultComboBoxModel<String>();
		
		model7.addElement(filter.minEvDensity.toString());
		
		for(int i=0; i<minED.size(); i++)
			model7.addElement(minED.elementAt(i).toString());
	
		
		final ProMComboBox<String> minDensity = new ProMComboBox<String>(model7);
		minDensity.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 3;
		cMain.gridy = 1;
		mainPane.add(minDensity, cMain);

		JLabel maxDensityLabel = new JLabel("<html><h10>Max. event density:</h10></html>");
		maxDensityLabel.setForeground(UISettings.TextLight_COLOR);
		maxDensityLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y;       
		cMain.gridx = 3;
		cMain.gridy = 2;
		mainPane.add(maxDensityLabel, cMain);
		
		DefaultComboBoxModel<String> model8 = new DefaultComboBoxModel<String>();
		
		if(filter.maxEvDensity == 99999999999999999999999999999999999999999999999999.0)
			model8.addElement("Unlimited");
		else
			model8.addElement(filter.maxEvDensity.toString());
		
		for(int i=0; i<maxED.size(); i++)
			model8.addElement(maxED.elementAt(i).toString());
		
		final ProMComboBox<String> maxDensity = new ProMComboBox<String>(model8);
		maxDensity.setForeground(UISettings.TextLight_COLOR);
	    cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = x;
		cMain.ipady = y; 
		cMain.gridx = 3;
		cMain.gridy = 3;
		mainPane.add(maxDensity, cMain);
		
		final JCheckBox mergeBox = new JCheckBox("Merge similar chunks");
		mergeBox.setBackground(Color.BLACK);
		mergeBox.setForeground(UISettings.TextLight_COLOR);
		mergeBox.setSelected(filter.mergeChunks);
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = 50;
		cMain.ipady = y;       
		cMain.gridx = 4;
		cMain.gridy = 1;
		mainPane.add(mergeBox, cMain);
		
		//Buttons
		
		SlickerButton updateButton = new SlickerButton("Update");
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = 50;
		cMain.ipady = y;       
		cMain.gridx = 4;
		cMain.gridy = 2;
		mainPane.add(updateButton, cMain);
		
		//////////////////////////////// Update action listener ///////////////////////////////////////////
		
		updateButton.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	mainPane.removeAll(); 
	            		mainPane.updateUI();
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            		
	            		//Collect selected values
	            		
	            		String BPinfoVal = BPinfo.getSelectedItem().toString();
	            		String interruptionsVal = interruptions.getSelectedItem().toString();
	            		String batchSizeVal = batchSize.getSelectedItem().toString();
	            		String distanceFromMeanVal = distanceFromMean.getSelectedItem().toString();
	            		String minDurVal = minDur.getSelectedItem().toString();
	            		String maxDurVal = maxDur.getSelectedItem().toString();
	            		String minDensityVal = minDensity.getSelectedItem().toString();
	            		String maxDensityVal = maxDensity.getSelectedItem().toString();
	            		Boolean mergeBoxVal = mergeBox.isSelected();
	            		
	            		//Update filter
	            		
	            		filter.chartInfo = BPinfoVal;
	            		
	            		if(interruptionsVal.equals("Unlimited"))
	            		{
	            			filter.checkInterruptions = false;
	            			filter.interruptionTolerance = 100.0;
	            			
	            		}
	            		else
	            		{
	            			filter.checkInterruptions = true;
	            			String inVal = interruptionsVal.substring(0, interruptionsVal.length()-1);
	            			filter.interruptionTolerance = Double.parseDouble(inVal)/100;
	            			
	            		}
	            		
	            		filter.minBatchSize = Integer.parseInt(batchSizeVal);
	            		filter.minDistFromMean = Double.parseDouble(distanceFromMeanVal);
	            		filter.minDur = Double.parseDouble(minDurVal);
	            		
	            		if(maxDurVal.equals("Unlimited"))
	            			filter.maxDur = 99999999999999999999999999999999999999999999999999.0;
	            		else	
	            			filter.maxDur = Double.parseDouble(maxDurVal);
	            		
	            		filter.minEvDensity = Double.parseDouble(minDensityVal);
	            		
	            		if(maxDensityVal.equals("Unlimited"))
	            			filter.maxEvDensity = 99999999999999999999999999999999999999999999999999.0;
	            		else
	            			filter.maxEvDensity = Double.parseDouble(maxDensityVal);
	            		
	            		filter.mergeChunks = mergeBoxVal;
	            		
	            		
	            		filter.FilterPrint();
	            		
	             		Vector<BPOUT> bpOUTsUpdated = new Vector<BPOUT>();
	            		
	            		for(int i=0; i<bpouts.size(); i++)
	            		{
	            			BPOUT b = bpouts.elementAt(i);
	            			BPOUT newBPOUT = abp.bpAnalysis(b.chunksOriginal, b.bpts, b.ip, b.re, filter);
	            			newBPOUT.task = bpouts.elementAt(i).task;
	            			newBPOUT.resource = bpouts.elementAt(i).resource;
	            			
	            			bpOUTsUpdated.add(newBPOUT);
	            		}
	            		
	            		visualizeBP(mainPane, cMain, widthMenu, bpOUTsUpdated);
	                	   	
	                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		SlickerButton saveButton = new SlickerButton("Save");
		cMain.gridwidth = 1;
		cMain.gridheight = 1;
		cMain.ipadx = 50;
		cMain.ipady = y;       
		cMain.gridx = 4;
		cMain.gridy = 3;
		mainPane.add(saveButton, cMain);
		
		//
		
		saveButton.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	
	                	final String EXTENSION = ".csv";
	     
	                  	 JFileChooser fileChooser = new JFileChooser();
	                  	 int status = fileChooser.showSaveDialog(new JFrame());

	                	 if (status == JFileChooser.APPROVE_OPTION) {
	                	 File selectedFile = fileChooser.getSelectedFile();

	                	        try {
	                	            String fileName = selectedFile.getCanonicalPath();
	                	            
	                	            for(int i=0; i<bpouts.size(); i++)
	                	            {
	                	               String file = fileName +"_"+bpouts.elementAt(i).task+"_"+bpouts.elementAt(i).resource + EXTENSION;
	                	               bpouts.elementAt(i).bpts.storeChunksPublic(bpouts.elementAt(i).chunks, file);
	                	            }
	                	        } catch (IOException e1) {
	                	        	JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	           	                         JOptionPane.ERROR_MESSAGE);
	                	        }
	                	    

	                	    
	                	}
	                	
	                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );
		
		//middle - charts 
		int c = 0;
		for(int i=0; i<bpouts.size(); i++)
		{
		BPOUT bpout = bpouts.elementAt(i);
		c++;
		int count = i*4;
		
		cMain.anchor = GridBagConstraints.NORTH;
		JLabel inputLabel = new JLabel(bpout.label);
		inputLabel.setForeground(UISettings.TextLight_COLOR);
		//inputLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 5;
		cMain.gridheight = 1;
		cMain.ipadx = 50;
		cMain.ipady = y;       
		cMain.gridx = 0;
		cMain.gridy = 5+count;
		mainPane.add(inputLabel, cMain);
		
		cMain.gridwidth = 5;
		cMain.gridheight = 1;
		cMain.ipadx = imgWBP;
		cMain.ipady = imgHBP;       
		cMain.gridx = 0;
		cMain.gridy = 6+count;
			
		
		mainPane.add(visRes.plotBars(bpout.ip, bpout.chunks, filter.chartInfo), cMain);
		
		//middle - ts chart 
		cMain.gridwidth = 5;
		cMain.gridheight = 1;
		cMain.ipadx = imgWBP;
		cMain.ipady = imgHBP;       
		cMain.gridx = 0;
		cMain.gridy = 7+count;
		
		TimeSeries nobpTS = visRes.createNONBPTS(bpout.chunks, bpout.bpts);
		TimeSeries bpTS = visRes.createBPTS(bpout.chunks, bpout.bpts);
		TimeSeries bpoutTS = visRes.createBPoutTS(bpout.chunks, bpout.bpts);
		
		mainPane.add(visRes.plotBPTS(nobpTS, bpTS, bpoutTS), cMain);
		
		//bottom - analysis
		cMain.anchor = GridBagConstraints.NORTH;
		JLabel bpInfoLabel = new JLabel(bpout.BPInfo);
		bpInfoLabel.setForeground(UISettings.TextLight_COLOR);
		bpInfoLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 2;
		cMain.gridheight = 1;
		cMain.ipadx = 200;
		cMain.ipady = y;       
		cMain.gridx = 0;
		cMain.gridy = 8+count;
		mainPane.add(bpInfoLabel, cMain);
		
		JLabel seasonLabel = new JLabel(bpout.periodicity);
		seasonLabel.setForeground(UISettings.TextLight_COLOR);
		seasonLabel.setHorizontalAlignment(JLabel.LEFT);
		cMain.gridwidth = 3;
		cMain.gridheight = 1;
		cMain.ipadx = 200;
		cMain.ipady = y;       
		cMain.gridx = 2;
		cMain.gridy = 8+count;
		mainPane.add(seasonLabel, cMain);
		
		}
		
		c = (int) (c*3.3);
		int add = 250;
		
		mainPane.setPreferredSize(new Dimension(imgWBP, imgHBP*c+add));
		mainPane.revalidate();
		mainPane.repaint();
		
		mainPane.setSize(new Dimension(imgWBP, imgHBP*c+add));
	    final BufferedImage bi2 = new BufferedImage(mainPane.getWidth(), mainPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
		mainPane.paintAll(bi2.getGraphics());

		
	}
		
	
}




