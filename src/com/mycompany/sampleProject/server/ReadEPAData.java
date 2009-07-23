package com.mycompany.sampleProject.server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.jdo.PersistenceManager;


import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.common.collect.Synchronized;
import com.sun.swing.internal.plaf.synth.resources.synth;


public class ReadEPAData {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	private static int RowCount = 0;
	DataInputStream buffInput ;
	BufferedReader br ;
	public ReadEPAData() {
		try {
		 buffInput = new DataInputStream(new FileInputStream("output-new-2-2.txt"));
		 br = new BufferedReader(new InputStreamReader(buffInput));
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	 public  synchronized void readFileDataAndLoad() throws Exception{
		//pm= PMF.get().getPersistenceManager();
		
		try {
		String str ="";
		int count = 0;
		int EndRowCount = 0;
		int currentLine =0;
		
		 while ( (str = br.readLine())!= null) {
			/*
			if (count < RowCount) {
				str = br.readLine();
				++count;
				continue;
			}
			*/
			String [] data = str.split("[|]",0);
			EPAToxicDataBean bean = new EPAToxicDataBean();
			System.out.println("Processing Line:" + ++currentLine );
			bean.setData(data);
			saveToDataStore(bean);
			if (currentLine == 200) {
				RowCount+= 200;
				break;
			}
			
		 }
		} finally {
			//cleanupResources();
			System.out.println("Current RowCount:" + RowCount );
		}
	}
	
	private void saveToDataStore(EPAToxicDataBean bean) {

		try {
			//deleteDataIfPresent(pm,bean.getFacilityregistryId());
			pm.makePersistent(bean);

		} catch (Exception E) {
			System.out.println("Error while Saving:" + E.getMessage());
		}

	}
	private void cleanupResources() {
		pm.close();
	}
	
	
	private void deleteDataIfPresent(PersistenceManager pm,
			String FacilityregistryId) {

		//pm = PMF.get().getPersistenceManager();

		String query = "select from " + EPAToxicDataBean.class.getName()
				+ " where FacilityregistryId =='" + FacilityregistryId + "'";
		List<EPAToxicDataBean> xmlDataList = (List<EPAToxicDataBean>) pm.newQuery(query)
				.execute();
			for (EPAToxicDataBean rec : xmlDataList) {
				pm.deletePersistent(rec);
			}

	}


}
