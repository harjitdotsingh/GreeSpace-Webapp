package com.mycompany.sampleProject.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import flexjson.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class EPADataServlet extends HttpServlet {
    String JsonReturnValueForIphone = "";
    public static String appTypeiPhone = "iPhone";
    public static String appTypeWeb = "Web";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String lat = null;
        String longitude = null; 
        String[] zipcodeArray = null;
        String zipCode = null;
        String radius = "";
        String maxresults = "";
        String appType = "iPhone"; 
        zipCode = req.getParameter("zipcode");

        String statCode = req.getParameter("state");
        lat = req.getParameter("lat");
        longitude = req.getParameter("long");
        radius = req.getParameter("radius");
        if (radius == null) {
            radius = "15";
        }
        appType = req.getParameter("appType");
        if (appType == null) {
            appType = appTypeiPhone;
        } else {
            appType = appTypeWeb;
        }

        maxresults = req.getParameter("results");
        if (maxresults == null) {

            maxresults = "50";
        }
        if (zipCode != null) {
            zipcodeArray = zipCode.split(" ");
        }
        String jsonString = null;
        if (longitude != null && lat != null) {
            if (appType.equalsIgnoreCase(appTypeiPhone)) {
                System.out.println("Start of JSON Call:"
                        + new Date().toString());
                jsonString = searchAndReturnDatainJSONFormat(longitude, lat,
                        radius, maxresults);
            } else {
            	 System.out.println("Start of JSON Call For Web:"
                         + new Date().toString());
                jsonString = searchAndReturnDatainJSONFormatForWebApp(longitude, lat,
                        radius, maxresults);
            }
            jsonString = JsonReturnValueForIphone;
        }
        if (zipcodeArray != null && zipcodeArray.length > 1) {
            jsonString = searchAndReturnDatainJSONFormat(zipcodeArray, statCode);
        } else if (zipCode != null) {
            jsonString = searchAndReturnDatainJSONFormat(zipCode, statCode,
                    null);
        } else if (statCode != null) {
            jsonString = searchAndReturnDatainJSONFormat(null, statCode, null);
        }
        resp.setContentType("text\txt");
        if (jsonString != null) {
            out.println(jsonString);
        }
        out.flush();
    }

    @SuppressWarnings("unchecked")
    private String searchAndReturnDatainJSONFormat(String zipCode,
            String statCode, String[] latlong) {
        String query = "";
        String retValue = "[";
        PersistenceManager pm = PMF.get().getPersistenceManager();
        if (zipCode != null && zipCode.length() > 0) {
        	query="SELECT FROM  com.mycompany.sampleProject.server.EPAToxicDataBean " +
                    "where LocationZIPCode.startsWith(tradeName) " +
                    "parameters String tradeName";

            /*query = "select from " + EPAToxicDataBean.class.getName()
                    + " where LocationZIPCode.startsWith('" + zipCode + "')";
                    */
        }
        if (statCode != null && statCode.length() > 0) {
            query = "select from " + EPAToxicDataBean.class.getName()
                    + " where LocationAddressStateCode =='" + statCode + "'";
        }
        // Adding this here to keep the compiler happy
        @SuppressWarnings("cast")
        List<EPAToxicDataBean> xmlDataList = (List<EPAToxicDataBean>) pm.newQuery(query).execute(zipCode);
        int beanCounter = 1;
        if (xmlDataList != null) {
            for (EPAToxicDataBean rec : xmlDataList) {
                org.json.JSONObject jsonObject = new org.json.JSONObject(rec);
                retValue += jsonObject.toString();
                if (xmlDataList.size() > 1 && beanCounter != xmlDataList.size()) {
                    retValue += ",";
                }
                ++beanCounter;
            }
            retValue += "]";

        }
        return retValue;
    }

    private String searchAndReturnDatainJSONFormat(String zipCodeArray[],
            String stateCode) {
        String query = "";
        String retValue = "[";
        PersistenceManager pm = PMF.get().getPersistenceManager();
        for (String zipCode : zipCodeArray) {
            if (zipCode != null && zipCode.length() > 0) {
                query = "select from " + EPAToxicDataBean.class.getName()
                        + " where LocationZIPCode =='" + zipCode + "'";
            }
            // Adding this here to keep the compiler happy
            @SuppressWarnings("unchecked") 
            List<EPAToxicDataBean> xmlDataList = (List<EPAToxicDataBean>) pm
                    .newQuery(query).execute();
            int beanCounter = 1;
            if (xmlDataList != null) {
                for (EPAToxicDataBean rec : xmlDataList) {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(
                            rec);
                    retValue += jsonObject.toString();
                    if (xmlDataList.size() > 1
                            && beanCounter != xmlDataList.size()) {
                        retValue += ",";
                    }
                    ++beanCounter;
                }

            }
            retValue += "]";
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    private String searchAndReturnDatainJSONFormatForWebApp(String sLong, String sLat,
            String radius, String result) {
        String query = "";
        String retValue = "";
        PersistenceManager pm = PMF.get().getPersistenceManager();

        StringBuffer sb = new StringBuffer(100);
        sb.append("http://ws.geonames.org/findNearbyPostalCodesJSON?lat=");
        sb.append(sLat);
        sb.append("&lng=");
        sb.append(sLong);
        sb.append("&radius=");
        sb.append(radius);
        sb.append("&maxRows=100");
        //sb.append("&username=lafeer");

        String url = sb.toString();
        System.out.println("URL:" + sb);
        String JSONResult = callServletToGetData(url);
        String zipCodeArray[] = null;

        Integer iResult = new Integer(result);

        String state = parseJSONDatatoGetState(JSONResult);
        zipCodeArray = parseJSONData(JSONResult);
        ArrayList beanList = new ArrayList();
        List<EPAToxicDataBean> xmlDataList = null;

        try {
            if (zipCodeArray != null) {
                int resultCounter = 0;
                System.out.println("Startime" + new Date());
                HashMap containsMap = new HashMap();
                for (String zipCode : zipCodeArray) {
                	
                	
                	if (resultCounter  >= iResult) {
                		break;
                	}
                	query="SELECT FROM  com.mycompany.sampleProject.server.EPAToxicDataBean " +
                    "where LocationZIPCode.startsWith(tradeName) " +
                    "parameters String tradeName";
                    System.out.println("Executing Query:" + query);
                    xmlDataList = (List<EPAToxicDataBean>) pm.newQuery(query).execute(zipCode);

                    if (xmlDataList != null) {

                        for (EPAToxicDataBean rec : xmlDataList) {
                            if (resultCounter < iResult) {
                            	if (!containsMap.containsKey(rec.getFacilityregistryId())) {
                                beanList.add(rec);
                                ++resultCounter;
                                containsMap.put(rec.getFacilityregistryId(), 0);
                                System.out.println("ResultSize time:"
                                        + beanList.size());
                            	} else{
                            		continue;
                            	}

                            } else {
                                break;
                            }
                        }

                    }
                }
            }
            if (beanList.size() > 0) {

                StringBuffer retValuBuffer = new StringBuffer(8000);
                retValuBuffer.append("[");
                int beanCounter = 0;
                for (int i = 0; i < beanList.size(); ++i) {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(
                            beanList.get(i));
                    retValuBuffer.append(jsonObject.toString());
                    if ((beanCounter != beanList.size()-1) &&  beanList.size() >1) {
                        retValuBuffer.append(",");
                    }
                    ++beanCounter;
                }
                retValuBuffer.append("]");

                retValue = retValuBuffer.toString();

            }
        } catch (Exception exec) {
            System.out.println("Error" + exec.getMessage());
            System.out.println("Error at time" + new Date());
        } finally {
            StringBuffer retValuBuffer = new StringBuffer(8000);
            retValuBuffer.append("[");
            int beanCounter = 0;
            for (int i = 0; i < beanList.size(); ++i) {
                org.json.JSONObject jsonObject = new org.json.JSONObject(
                        beanList.get(i));
                retValuBuffer.append(jsonObject.toString());
                if ((beanCounter != beanList.size()-1) &&  beanList.size() >1) {
                    retValuBuffer.append(",");
                }
                ++beanCounter;
            }
            retValuBuffer.append("]");
            JsonReturnValueForIphone = retValuBuffer.toString();
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    private String searchAndReturnDatainJSONFormat(String sLong,
            String sLat, String radius, String result) {
        String query = "";
        String retValue = "";
        PersistenceManager pm = PMF.get().getPersistenceManager();

        StringBuffer sb = new StringBuffer(100);
        sb.append("http://ws.geonames.net/findNearbyPostalCodesJSON?lat=");
        sb.append(sLat);
        sb.append("&lng=");
        sb.append(sLong);
        sb.append("&radius=");
        sb.append(radius);
        sb.append("&maxRows=100");
        sb.append("&username=lafeer");

        String url = sb.toString();
        System.out.println("URL:" + sb);
        String JSONResult = callServletToGetData(url);
        String zipCodeArray[] = null;

        Integer iResult = new Integer(result);

        String state = parseJSONDatatoGetState(JSONResult);
        zipCodeArray = parseJSONData(JSONResult);
        ArrayList beanList = new ArrayList();
        HashMap containsMap = new HashMap();
        List<EPAToxicDataBean> xmlDataList = null;
        try {
            if (zipCodeArray != null) {
                int resultCounter = 0;
                System.out.println("Startime" + new Date());
                for (String zipCode : zipCodeArray) {
                	
                	
                	if (resultCounter  >= iResult) {
                		break;
                	}
                	query = "LocationZIPCode =='" + zipCode + "'";
                    System.out.println("Executing Query:" + query);
                    xmlDataList = (List<EPAToxicDataBean>) pm.newQuery(
                            EPAToxicDataBean.class, query).execute();

                    if (xmlDataList != null) {

                        for (EPAToxicDataBean rec : xmlDataList) {
                            if (resultCounter < iResult) {
                            	if (!containsMap.containsKey(rec.getFacilityregistryId())) {
                                beanList.add(rec);
                                containsMap.put(rec.getFacilityregistryId(),0);
                                ++resultCounter;
                                System.out.println("ResultSize time:"
                                        + beanList.size());
                            	} else {
                            		continue;
                            	}
                            } else {
                                break;
                            }
                        }

                    }
                }
            }
            if (beanList.size() > 0) {
                retValue = new JSONSerializer().include("*").serialize(
                        "EPAData", beanList);

            }
        } catch (Exception exec) {
            System.out.println("Error" + exec.getMessage());
            System.out.println("Error at time" + new Date());
        } finally {
            JsonReturnValueForIphone = new JSONSerializer().include("*")
                    .serialize("EPAData", beanList);
        }
        return retValue;
    }

    private String[] parseJSONData(String result) {
        String zipCodeArray[] = null;
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray arrayList = (JSONArray) jsonObj.get("postalCodes");
            zipCodeArray = new String[arrayList.length()];
            String zipCode = null;
            int iActualCount = 0;
            for (int iIndexCount = 0; iIndexCount <= arrayList.length(); ++iIndexCount) {
                try {
                    zipCode = (String) ((JSONObject) arrayList.get(iIndexCount))
                            .get("postalCode");
                    zipCodeArray[iActualCount] = zipCode;
                    ++iActualCount;

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        return zipCodeArray;
    }

    private boolean zipCodeContained(String zipCode, String[] zipCodeArray) {
        boolean bisContained = false;
        for (int i = 0; i < zipCodeArray.length; ++i) {
            if (zipCode.indexOf(zipCodeArray[i]) > 0) {
                bisContained = true;
                break;
            }
        }
        return bisContained;
    }

    private String parseJSONDatatoGetState(String result) {
        String returnResult = null;
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray arrayList = (JSONArray) jsonObj.get("postalCodes");
            String zipCode = null;
            if (arrayList != null && arrayList.length() > 0) {
                returnResult = (String) ((JSONObject) arrayList.get(0))
                        .get("adminCode1");
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        return returnResult;
    }

    public static String callServletToGetData(String servletURL) {
        String returnResult = "";
        BufferedReader rd = null;
        try {
            URL url = new URL(servletURL);
            URLConnection conn = url.openConnection();
            // conn.setDoOutput(true);

            // Get the response
            try {
             rd = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            }catch (IOException ie) {
            	System.out.println("Error: " + ie);
            }
            String line;
            while ((line = rd.readLine()) != null) {
                returnResult += line;
            }
            rd.close();
        } catch (Exception E) {
            E.printStackTrace();
        }
        return returnResult;
    }

}
