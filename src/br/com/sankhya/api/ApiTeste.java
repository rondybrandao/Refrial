package br.com.sankhya.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.util.FinderWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ApiTeste {
	
	public static void main(String[] args) throws Exception {

        //postPedidoTeste();
		post();
    }

	private static void get() {
		//JdbcWrapper jdbcWrapper = null;
		EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
        //jdbcWrapper = dwfFacade.getJdbcWrapper();
		try {
			DynamicVO rotaVO = (DynamicVO) dwfFacade.findEntityByPrimaryKeyAsVO("AD_APIRESP", 1);
			String rota = rotaVO.asString("RESPONSE");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static StringBuilder post() throws ClientProtocolException, IOException {
		String js = login();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("http://refrial.nuvemdatacom.com.br:9586/mge/service.sbr?serviceName=CRUDServiceProvider.saveRecord&mgeSession="+js+"&outputType=json");
		JsonObject jo = (JsonObject) Json.createObjectBuilder()
				.add("serviceName","CRUDServiceProvider.saveRecord")
				.add("requestBody", Json.createObjectBuilder()
						.add("dataSet", Json.createArrayBuilder()
								.add(Json.createObjectBuilder()
										.add("rootEntity", "AD_APIRESP")
										.add("includePresentationFields", "N")
										.add("dataRow", Json.createObjectBuilder()
												.add("localFields", Json.createObjectBuilder()
														.add("RESPONSE", Json.createObjectBuilder()
																.add("current", "data")))))))
				.build();
		
		StringEntity p = new StringEntity(jo.toString());
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
	
		request.setEntity(p);
		
		HttpResponse response = httpClient.execute(request);
		
		if(response != null) {
			InputStream in = response.getEntity().getContent();//Recebe dados da resposta
			StringBuilder textBuilder = new StringBuilder();
	        try (Reader reader = new BufferedReader(new InputStreamReader
	          (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
	            int c = 0;
	            while ((c = reader.read()) != -1) {
	                textBuilder.append((char) c);
	            }
	        }
	        
	        System.out.println(textBuilder);
	        return textBuilder;
		}
		return null;
	}
	
	public static String login() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("http://refrial.nuvemdatacom.com.br:9586/mge/service.sbr?serviceName=MobileLoginSP.login&outputType=json");
		JsonObject jo = (JsonObject) Json.createObjectBuilder()
				.add("serviceName", "MobileLoginSP.login")
				.add("requestBody", Json.createObjectBuilder()
						.add("NOMUSU", Json.createObjectBuilder()
								.add("$", "SUP"))
						.add("INTERNO", Json.createObjectBuilder()
								.add("$", "$R3@L2022"))
						.add("KEEPCONNECTED", Json.createObjectBuilder()
								.add("$", "S"))).build();
		
		StringEntity p = new StringEntity(jo.toString());
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
	
		request.setEntity(p);
		
		HttpResponse response = httpClient.execute(request);
		
		if(response != null) {
			InputStream in = response.getEntity().getContent();//Recebe dados da resposta
			StringBuilder textBuilder = new StringBuilder();
	        try (Reader reader = new BufferedReader(new InputStreamReader
	          (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
	            int c = 0;
	            while ((c = reader.read()) != -1) {
	                textBuilder.append((char) c);
	            }
	        }
	        System.out.println(textBuilder);
	        
	        JsonReader jsonReader = Json.createReader(new StringReader(textBuilder.toString()));
	        JsonObject reply = jsonReader.readObject();
	        System.out.println(reply.get("responseBody"));
	        //String js = reply.get("jsessionid").toString();
	        //return js;
		}
		return null; 
	}
}






