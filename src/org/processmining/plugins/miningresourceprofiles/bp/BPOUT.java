package org.processmining.plugins.miningresourceprofiles.bp;

import java.util.Vector;

import org.rosuda.JRI.Rengine;

public class BPOUT{
	
	public Vector<Chunk> chunks = new Vector<Chunk>();
	public String task = "";
	public String resource = "";
	public Vector<Chunk> chunksOriginal = new Vector<Chunk>();
	public FilterBP filter = new FilterBP();
	
	public InputParametersBP ip = new InputParametersBP();
	public String BPInfo = "";
	public String periodicity = "";
	public BPTS bpts = new BPTS();
	public Rengine re = null;
	public String label = "";

	public BPOUT() {
	} 
	
	
	
}




