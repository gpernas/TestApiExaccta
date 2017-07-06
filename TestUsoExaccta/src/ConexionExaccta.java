
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConexionExaccta {

	/*final static String corporation = "B91428607";
    final static String admin = "eduardo.barranca@dacartec.com";
    final static String password = "04ea6197f04e85";*/
	
	final static String corporation = "Q3067007I";
    final static String admin = "jasanchez@apc.es";
    final static String password = "gh083thi3gh8hcbc52b2d2acf";
    
	public ConexionExaccta() {

	}

	
	public static void testConection () {
		
		String timestamp = getTimestamp();
		String key = corporation + admin + timestamp + password;
		String token = getMD5(key);
		String hola;
		
		String servicioDatosEmpresa = "https://www.exaccta.com/api/xpens/v1/company";	
		
		String requestServiceDatosEmpresa = "{\"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\"}"; 		
		String jsonRequest = requestServiceDatosEmpresa;		
				
		jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
		jsonRequest = jsonRequest.replace("#ADMIN#", admin);
		jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
		jsonRequest = jsonRequest.replace("#TOKEN#", token);
		
        System.out.println(jsonRequest.toString());        
        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicioDatosEmpresa);                
        System.out.println(jsonResponseStr);
        
	}
	
	public static void main(String[] args) {
		
		ConexionExaccta ce = new ConexionExaccta();
		//ce.testConection();
		
		
		/*String user = "gonzalo.pernas@dacartec.com";
		String refUnitCost = "140040";
		String unitCost = "Viaje A27";
		
		String salida="";*/
		
		//String user = "jasanchez@apc.es,jnvidal@apc.es";
		String user = "asolana@apc.es";
		String refUnitCost = "151003";
		String unitCost = "Viaje A27";
		
		String salida="";
		
		
	//	System.out.println("Crear unidad de coste");
		//salida = ce.createUnitCost (user, refUnitCost, unitCost);
		
		
		//System.out.println("Buscar unidad de coste");
		//salida = ce.searchUnitCost(user, refUnitCost, unitCost);
		//System.out.println("Borrar unidad de coste");
		//salida = ce.deleteUnitCost (refUnitCost);
		salida = ce.listDocuments (user, refUnitCost);
		//salida = ce.createNotaGasto (user, refUnitCost, "2","15-9");
		//String salida = ce.setStatusNotaGasto (user, refHoja, "0");
		String path = "c:/tempImgs/";
		String id_Document = "1-1";
		String tipo = "";
		/*tipo="i";
		salida = ce.getImagenes (user, id_Document, path, tipo);
*/
		tipo="t";
		String salida2 = ce.getImagenes (user, id_Document, path, tipo);
	}
	
	private static String getMD5(String st) {

		MessageDigest md;
		try {

			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(st.getBytes("UTF-8"));

			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < digest.length; ++i) {
				sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String getJSONResponse(String parametros, String servicio) {

		try {

			URL url = new URL(servicio);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Method", "POST");

			// send the request
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(parametros);
			out.flush();
			out.close();

			InputStream inputstream = conn.getInputStream();
			BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
			StringBuffer buffer = new StringBuffer();
			int nextChar = 0;

			while((nextChar = bufferedinputstream.read()) != -1)
			{
				buffer.append((char)nextChar);
			}

			return buffer.toString();

		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}

	}
	
	private static String getTimestamp() {
		
		SimpleDateFormat sdf = new SimpleDateFormat();  		
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss zzz");
		Date now = new Date();
		return sdf.format(now);
		
	}
	
	public String createUnitCost (String user, String refUnitCost, String unitCost) {
		
		try {
			String valRet="";
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlCreateUnitCost = "https://www.exaccta.com/api/xpens/v1/cost_unit/update";
			String servicio = urlCreateUnitCost;
			
			String requestCreateUnitCost = "";
			
			/* Hay que distinguir si ponemos varios usuarios o no*/
			String[] listaUsuarios=user.split(",");
			
			if (listaUsuarios.length > 1)
			{
				requestCreateUnitCost = "{\"count\":1000, \"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"cost_units\": [{\"cost_unit_ref\": \"#REF#\",\"name\": \"#NAME#\",\"imputable\": \"T\",\"employees\": [";
				String tmpNexo="";
				String tmpUser="";
				for (int i=0;i<listaUsuarios.length;i++)
				{
					tmpUser=listaUsuarios[i].trim();
					requestCreateUnitCost =requestCreateUnitCost + tmpNexo + "{\"user\": \"" + tmpUser +"\"}";
					tmpNexo=",";
				}
				requestCreateUnitCost =requestCreateUnitCost +	"]}]}";
			}
			else
				requestCreateUnitCost = "{\"count\":1000, \"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"cost_units\": [{\"cost_unit_ref\": \"#REF#\",\"name\": \"#NAME#\",\"imputable\": \"T\",\"employees\": [{\"user\": \"#USER#\"}]}]}";

			String jsonRequest = requestCreateUnitCost;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
		    jsonRequest = jsonRequest.replace("#REF#",  refUnitCost);
		    jsonRequest = jsonRequest.replace("#NAME#", unitCost);
		    jsonRequest = jsonRequest.replace("#USER#", user);
			System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
	        
	        System.out.println(jsonResponseStr); 
			
	        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
	        
	        JSONArray msg = (JSONArray) jsonResponse.get("cost_units");
	        for (int i=0;i< msg.length();i++)
	        {
	        	JSONObject valUno=msg.getJSONObject(i);
	        	if (valUno.isNull("error"))
	        	{
	        		System.out.println("No hay error");
	        		valRet=valUno.getString("cost_unit_ref");
	        	}
	        	else
	        	{
	        		System.out.println("si hay error");
	        		System.out.println(valUno.get("error").toString());
	        		valRet="";
	        	}
	        		
	        }
	        	
	        return valRet;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	//Search unit cost {
	/*"corporation": "00000230T",
	"user": "admin@empresa.com",
	"timestamp": "2016-01-25 12:20:18 CET",
	"token": "sagasgfdfgsdfgsdfgfg",
	"search_unit_costs":{
	"employees":[
	{
	"user": "empleado2@empresa.com"
	}
	]
	}
	}*/
	
	public String searchUnitCost (String user, String refUnitCost, String unitCost) {
		
		try {
			
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlCreateUnitCost = "https://www.exaccta.com/api/xpens/v1/cost_unit";
			String servicio = urlCreateUnitCost;
						
		    String requestSearchUnitCost = "{\"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"search_unit_costs\": {\"employees\": [{\"user\": \"#USER#\"}]}}";

			String jsonRequest = requestSearchUnitCost;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
		    jsonRequest = jsonRequest.replace("#REF#",  refUnitCost);
		    jsonRequest = jsonRequest.replace("#NAME#", unitCost);
		    jsonRequest = jsonRequest.replace("#USER#", user);
			
	        System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
	        System.out.println(jsonResponseStr); 
			
	        return jsonResponseStr;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	
	public String deleteUnitCost (String refUnitCost) {
		
		try {
			String valRet="";
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlDeleteUnitCost = "https://www.exaccta.com/api/xpens/v1/cost_unit/delete";
			String servicio = urlDeleteUnitCost;
						
		    String requestDeleteUnitCost = "{\"count\":1000, \"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"cost_units\": [{\"cost_unit_ref\": \"#REF#\"}]}";

			String jsonRequest = requestDeleteUnitCost;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
		    jsonRequest = jsonRequest.replace("#REF#",  refUnitCost);
		    System.out.println("Peticion");
	        System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);
	        System.out.println("Respuesta");
	        System.out.println(jsonResponseStr); 
			
	        JSONObject jsonResponse = new JSONObject(jsonResponseStr);
	        
	        JSONArray msg = (JSONArray) jsonResponse.get("cost_units");
	        for (int i=0;i< msg.length();i++)
	        {
	        	JSONObject valUno=msg.getJSONObject(i);
	        	if (valUno.isNull("error"))
	        	{
	        		System.out.println("No hay error");
	        		valRet=valUno.getString("cost_unit_ref");
	        	}
	        	else
	        	{
	        		System.out.println("si hay error");
	        		System.out.println(valUno.get("error").toString());
	        		valRet="";
	        	}
	        		
	        }
	        	
	        return valRet;	
	        
	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	public String listDocuments (String user, String refUnitCost) {
		
		try {
			
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlListDocuments = "https://www.exaccta.com/api/xpens/v1/document";
			String servicio = urlListDocuments;
						
		    //String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"user\": \"#USER#\", \"is_deleted\": \"F\", \"cost_unit_id\": \"#REF#\"}}";
			//String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"user\": \"#USER#\", \"cost_unit_id\": \"#REF#\"}}";
			//String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"user\": \"#USER#\"}}";
			String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"cost_unit_id\": \"#REF#\"}}";

			String jsonRequest = requestListDocuments;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
			jsonRequest = jsonRequest.replace("#USER#",  user);
		    jsonRequest = jsonRequest.replace("#REF#",  refUnitCost);

	        System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
	        System.out.println(jsonResponseStr); 
		
	        return jsonResponseStr;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	public String getDocumentsObject (String user, String refUnitCost, String listaIdDoc) {
		
		try {
			
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlListDocuments = "https://www.exaccta.com/api/xpens/v1/document";
			String servicio = urlListDocuments;
						
		    //String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"user\": \"#USER#\", \"is_deleted\": \"F\", \"cost_unit_id\": \"#REF#\"}}";
			String requestListDocuments = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"search_documents\": {\"user\": \"#USER#\",  \"cost_unit_id\": \"#REF#\"}}";

			String jsonRequest = requestListDocuments;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
			jsonRequest = jsonRequest.replace("#USER#",  user);
		    jsonRequest = jsonRequest.replace("#REF#",  refUnitCost);
      	    
		    //Salida llamada a todos los documentos de una nota de gasto
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
			
	        //Tratamiento para dar un objeto con todos los documentos no eliminados
	        JSONObject jsonRootObject = new JSONObject(jsonResponseStr);
	        JSONArray jData = jsonRootObject.getJSONArray("documents");
	        
	        String docs = "";
	        for(int i = 0; i < jData.length(); ++i) {
	           JSONObject jObj = jData.getJSONObject(i);
	           boolean incorporar=false;
	           
	           // Quitamos las ya asignadas a notas de gasto
	           
	          // if (jObj.isNull("expense_id"))
	        	   //incorporar = true;
	           // QUITAR LOS QUE NO ESTEN EN listaIdDoc
	           //System.out.println(jObj.toString());
	           
	           
	          if (!listaIdDoc.isEmpty())
	          {
	        	  incorporar=false;
	        	  String[] tmpListaDoc=listaIdDoc.split(",");
	        	  for (int j=0;j<tmpListaDoc.length;j++)
	        		  if (tmpListaDoc[j].compareTo(jObj.getString("document_id")) == 0)
	        			  incorporar=true;
	          }
	          if (incorporar)
	          {
		           String document_ref ="{\"document_ref\": \""+jObj.optString("document_ref")+"\"}";
		           if (docs.equals("")) {
		        	   docs = document_ref;
		           } else {
		        	   docs = docs + "," +document_ref;
		           }
	          }
	        } 
	        
	      
	        return docs;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	
	public String createNotaGasto (String user, String refUnitCost, String status,String listaIdDoc) {
		
		try {
			
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlCreateNotaGasto = "https://www.exaccta.com/api/xpens/v1/expense_note/update";
			String servicio = urlCreateNotaGasto;
						
		    //String requestCreateNotaGasto = "{\"count\":1000, \"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"expense_notes\": [{\"user\": \"#USER#\", \"status\": #STATUS#, \"expense_ref\": \"#EXPENSE_REF#\",\"documents\": [#DOCS#]}]}";
			String requestCreateNotaGasto = "{\"count\":1000, \"corporation\": \"#CORPORATION#\",\"user\": \"#ADMIN#\",\"timestamp\": \"#TIMESTAMP#\",\"token\": \"#TOKEN#\",\"expense_notes\": [{\"user\": \"#USER#\", \"status\": #STATUS#,\"documents\": [#DOCS#]}]}";

			String jsonRequest = requestCreateNotaGasto;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
			jsonRequest = jsonRequest.replace("#USER#",  user);
			jsonRequest = jsonRequest.replace("#STATUS#",  status);
			jsonRequest = jsonRequest.replace("#EXPENSE_REF#",  "REFERENCIA INTERNA");
			
			
			//String docs = "{\"document_ref\": \"13-4\"}, {\"document_ref\": \"13-5\"}";
			//String docs = "{\"document_ref\": \"13-4\"}";
			
			String docs = getDocumentsObject (user, refUnitCost,listaIdDoc);
			
		    jsonRequest = jsonRequest.replace("#DOCS#",  docs);

	        System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
	        System.out.println(jsonResponseStr); 
			
	        return jsonResponseStr;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
	
	
	public String setStatusNotaGasto (String user, String refHoja, String status) {
		
		try {
			
			String timestamp = getTimestamp();
			String key = corporation + admin + timestamp + password;
			String token = getMD5(key);
			
			String urlSetStatusNotaGasto = "https://www.exaccta.com/api/xpens/v1/expense_note/update";			
			String servicio = urlSetStatusNotaGasto;
						
		    String requestSetStatusNotaGasto = "{\"count\":1000, \"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"expense_notes\": [{\"user\": \"#USER#\",\"expense_ref\": \"#ID_HOJA#\", \"status\": #STATUS# }]} ";
		    
			String jsonRequest = requestSetStatusNotaGasto;
			
			jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
			jsonRequest = jsonRequest.replace("#ADMIN#", admin);
			jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
			jsonRequest = jsonRequest.replace("#TOKEN#", token);
			
			jsonRequest = jsonRequest.replace("#USER#",  user);
			jsonRequest = jsonRequest.replace("#ID_HOJA#",  refHoja);
			jsonRequest = jsonRequest.replace("#STATUS#",  status);
			
	        System.out.println(jsonRequest.toString());      	        
	        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
	        System.out.println(jsonResponseStr); 
			
	        return jsonResponseStr;	
			
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}

	


public String getImagenes (String user, String id_Document, String path, String tipo) {
	
	try {
		
		String timestamp = getTimestamp();
		String key = corporation + admin + timestamp + password;
		String token = getMD5(key);
		
		String urlGetImagen = "https://www.exaccta.com/api/xpens/v1/document/get_file";			
		String servicio = urlGetImagen;
					
	    String requestGetImagen = "{\"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"files\":[ { \"user\": \"#USER#\", \"type\": \"#TIPO#\", \"document_id\":\"#ID#\" }]} ";
		//String requestGetImagen = "{\"corporation\": \"#CORPORATION#\", \"user\": \"#ADMIN#\", \"timestamp\": \"#TIMESTAMP#\", \"token\": \"#TOKEN#\", \"files\":[ {  \"type\": \"#TIPO#\",\"document_id\":\"#ID#\" }]} ";
	     
		String jsonRequest = requestGetImagen;
		
		jsonRequest = jsonRequest.replace("#CORPORATION#", corporation);
		jsonRequest = jsonRequest.replace("#ADMIN#", admin);
		jsonRequest = jsonRequest.replace("#TIMESTAMP#", timestamp);
		jsonRequest = jsonRequest.replace("#TOKEN#", token);
		
		jsonRequest = jsonRequest.replace("#USER#",  user);
		jsonRequest = jsonRequest.replace("#ID#",  id_Document);
		jsonRequest = jsonRequest.replace("#TIPO#", tipo);
		
        System.out.println(jsonRequest.toString());      	        
        String jsonResponseStr = getJSONResponse (jsonRequest.toString(), servicio);	        
		
      //Tratamiento para recoger las imagenes
        System.out.println(jsonResponseStr.toString());
        JSONObject jsonRootObject = new JSONObject(jsonResponseStr);
        JSONArray jData = jsonRootObject.getJSONArray("files");
        
        String docs = "";
        
        for(int i = 0; i < jData.length(); ++i) {
           JSONObject jObj = jData.getJSONObject(i);	           
           JSONArray jData2 = jObj.getJSONArray("attachments");
           
           for(int j = 0; j < jData2.length(); ++j) {
	           JSONObject jObj2 = jData2.getJSONObject(j);
	           
	           String FileBase64 = jObj2.getString("file").toString();
	           String fileName = jObj2.getString("name");
	           System.out.println(fileName);
	           
	           byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(FileBase64);
	           
	           FileOutputStream fos = new FileOutputStream(new File(path+fileName)); 
	           fos.write(imageBytes); 
	           fos.close();
	           
	           if (docs.equals("")) {
	        	   docs = fileName;
	           } else {
	        	   docs = docs + "," +fileName;
	           }   
	           	           
	        } 
           
        } 

        return docs;
        
		
	} catch(Exception e) {
		e.printStackTrace();
		return "";
	}
	
	
}
}

