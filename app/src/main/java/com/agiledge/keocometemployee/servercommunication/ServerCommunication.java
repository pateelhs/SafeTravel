//package com.agiledge.keocometemployee.servercommunication;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.json.JSONObject;
//
//import android.os.AsyncTask;
//
//import com.agiledge.keocometemployee.constants.CommenSettings;
//
//public class ServerCommunication extends AsyncTask<String, Void, String> {
//	private static String resultFromserver;
//	private static JSONObject jobj;
//	public ServerCommunication(JSONObject obj)
//	{
//		this.jobj=obj;
//	}
//DataDownloadListener datadown;
//public void setDataDownloadListen(DataDownloadListener datadown)
//{
//	this.datadown=datadown;
//}
//
//	       protected String doInBackground(String... urls) {
//
//
//	    	   resultFromserver = POST(jobj);
//				return resultFromserver;
//	        }
//
//	        protected void onPostExecute(String result) {
//	        	if(result!=null)
//
//	        	{
//	        		datadown.dataSuccess(result);
//	        	}
//	        	else
//	        	{
//	        		datadown.datafail();
//	        	}
//	   }
//	        public static String POST(JSONObject jdto){
//		        InputStream inputStream = null;
//		        String result = "";
//		        try {
//
//		            HttpPost httpPost = new HttpPost(CommenSettings.serverAddress);
//		            String json = "";
//		            json = jdto.toString();
//		            StringEntity se = new StringEntity(json);
//		            int timeout=9000,timeoutsocket=15000;
//		            httpPost.setHeader("Content-type", "application/json");
//		            httpPost.setEntity(se);
//		            HttpParams httpparameters=new BasicHttpParams();
//		            HttpConnectionParams.setConnectionTimeout(httpparameters, timeout);
//		            HttpConnectionParams.setSoTimeout(httpparameters, timeoutsocket);
//		            HttpClient httpclient = new DefaultHttpClient(httpparameters);
//		            HttpResponse httpResponse = httpclient.execute(httpPost);
//		            HttpEntity httpentity=httpResponse.getEntity();
//		            if(httpentity!=null)
//		            {
//		            inputStream = httpentity.getContent();
//		            if(inputStream != null)
//		            {
//		                result = convertInputStreamToString(inputStream);
//		            }
//
//		            }
//		            else
//		                result = "Did not work!";
//
//
//		        } catch (Exception e) {
//
//		        }
//		        return result;
//		    }
//		private static String convertInputStreamToString(InputStream inputStream) {
//			String result = "";
//	    	try{
//	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//	        String line = "";
//	        while((line = bufferedReader.readLine()) != null)
//
//	            result += line;
//
//	        inputStream.close();
//	        bufferedReader.close();
//	    	}catch(Exception e)
//	    	{
//	    		e.printStackTrace();
//	    	}
//
//	        return result;
//	    }
//		public static interface DataDownloadListener
//		{
//			void dataSuccess(String result);
//			void datafail();
//		}
//
//	}
//
