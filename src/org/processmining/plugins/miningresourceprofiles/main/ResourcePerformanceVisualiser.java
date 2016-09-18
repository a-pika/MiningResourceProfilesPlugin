package org.processmining.plugins.miningresourceprofiles.main;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import com.fluxicon.slickerbox.components.HeaderBar;

public class ResourcePerformanceVisualiser {
		@Plugin(
			name = "Mining Resource Profiles", 
			parameterLabels = { "Event log"}, 
			returnLabels = {"Resource Analysis Results"}, 
			returnTypes = {HeaderBar.class}, 
			userAccessible = true, 
			help = ""
		)
		@Visualizer
	    public HeaderBar RAVisualizer(PluginContext context, HeaderBar result) {
	        return result;
	    }
}