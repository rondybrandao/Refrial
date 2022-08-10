package br.com.sankhya.eventos;

import java.math.BigDecimal;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class OrdemCarga implements EventoProgramavelJava {
	
	public static void main(String[] args) throws Exception {

        //postPedidoTeste();
		postPedido();
    }
	
	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		

	}
	
	/*public void postRouteasy() {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://company.routeasy.com.br/orders/import?api_key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So"))
			    .header("Accept", "application/json")
			    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"orders\":[{\"loads\":[0.8,0,899.49,1],\"location\":{\"address\":{\"route\":\"Rua Bacuri\",\"street_number\":\"89\",\"neighborhood\":\"Vila da Saúde\",\"city\":\"São Paulo\",\"state\":\"SP\",\"country\":\"Brasil\",\"postal_code\":\"05469-030\"},\"headquarter\":\"\",\"code\":\"1850\",\"name\":\"Luke Skywalker\",\"phone\":\"11955044801\",\"email\":\"luke.skywalker@gmail.com\"},\"constraints\":{\"skills\":[\"fragil\"],\"prohibited_vehicles\":[\"5cf5fe2a90d8f250edbea30e\",\"5cf5fe2090d8f250edbea30c\"],\"window_daily\":[{\"start_time\":\"1970-01-01 08:00 GMT-3\",\"end_time\":\"1970-01-01 11:00 GMT-3\"},{\"start_time\":\"1970-01-01 13:00 GMT-3\",\"end_time\":\"1970-01-01 18:00 GMT-3\"}],\"priority\":1000,\"sequence\":20,\"region\":\"SPA\"},\"schedule_date\":[{\"start_date\":\"2021-01-06T03:00:00.000Z\",\"end_date\":\"2021-01-12T03:00:00.000Z\"}],\"items\":[{\"code\":\"caixa25\",\"name\":\"\",\"qty\":52},{\"code\":\"item2\"}],\"site\":\"5f203551ae950f0f3fbb92b4\",\"invoice_number\":\"14552\",\"order_number\":\"00423\",\"shipment_number\":\"552\",\"service_time\":10},{\"loads\":[2,0.2,19.9,1],\"location\":{\"address\":{\"route\":\"Av. Rio Branco\",\"street_number\":\"1186\",\"neighborhood\":\"Centro\",\"city\":\"São Paulo\",\"state\":\"SP\",\"postal_code\":\"\",\"country\":\"Brasil\"},\"headquarter\":\"\",\"code\":\"1410\",\"name\":\"Leia Organa\",\"phone\":\"11922566801\",\"email\":\"leia.organa@gmail.com\"},\"schedule_date\":[{\"start_date\":\"2021-01-06T03:00:00.000Z\"}],\"site\":\"5f203551ae950f0f3fbb92b4\",\"invoice_number\":\"14553\",\"order_number\":\"00424\",\"shipment_number\":\"553\",\"service_time\":15}]}"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
	}*/
	
	public static StringBuilder getVeiculos() throws IOException {
		//URL url = new URL("https://company.routeasy.com.br/orders/import?api_key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So{\\%22orders\\%22:[{\\%22loads\\%22:[0.8,0,899.49,1],\\%22location\\%22:{\\%22address\\%22:{\\%22route\\%22:\\%22Rua%20Bacuri\\%22,\\%22street_number\\%22:\\%2289\\%22,\\%22neighborhood\\%22:\\%22Vila%20da%20Sa%C3%BAde\\%22,\\%22city\\%22:\\%22S%C3%A3o%20Paulo\\%22,\\%22state\\%22:\\%22SP\\%22,\\%22country\\%22:\\%22Brasil\\%22,\\%22postal_code\\%22:\\%2205469-030\\%22},\\%22headquarter\\%22:\\%22\\%22,\\%22code\\%22:\\%221850\\%22,\\%22name\\%22:\\%22Luke%20Skywalker\\%22,\\%22phone\\%22:\\%2211955044801\\%22,\\%22email\\%22:\\%22luke.skywalker@gmail.com\\%22},\\%22constraints\\%22:{\\%22skills\\%22:[\\%22fragil\\%22],\\%22prohibited_vehicles\\%22:[\\%225cf5fe2a90d8f250edbea30e\\%22,\\%225cf5fe2090d8f250edbea30c\\%22],\\%22window_daily\\%22:[{\\%22start_time\\%22:\\%221970-01-01%2008:00%20GMT-3\\%22,\\%22end_time\\%22:\\%221970-01-01%2011:00%20GMT-3\\%22},{\\%22start_time\\%22:\\%221970-01-01%2013:00%20GMT-3\\%22,\\%22end_time\\%22:\\%221970-01-01%2018:00%20GMT-3\\%22}],\\%22priority\\%22:1000,\\%22sequence\\%22:20,\\%22region\\%22:\\%22SPA\\%22},\\%22schedule_date\\%22:[{\\%22start_date\\%22:\\%222021-01-06T03:00:00.000Z\\%22,\\%22end_date\\%22:\\%222021-01-12T03:00:00.000Z\\%22}],\\%22items\\%22:[{\\%22code\\%22:\\%22caixa25\\%22,\\%22name\\%22:\\%22\\%22,\\%22qty\\%22:52},{\\%22code\\%22:\\%22item2\\%22}],\\%22site\\%22:\\%225f203551ae950f0f3fbb92b4\\%22,\\%22invoice_number\\%22:\\%2214552\\%22,\\%22order_number\\%22:\\%2200423\\%22,\\%22shipment_number\\%22:\\%22552\\%22,\\%22service_time\\%22:10},{\\%22loads\\%22:[2,0.2,19.9,1],\\%22location\\%22:{\\%22address\\%22:{\\%22route\\%22:\\%22Av.%20Rio%20Branco\\%22,\\%22street_number\\%22:\\%221186\\%22,\\%22neighborhood\\%22:\\%22Centro\\%22,\\%22city\\%22:\\%22S%C3%A3o%20Paulo\\%22,\\%22state\\%22:\\%22SP\\%22,\\%22postal_code\\%22:\\%22\\%22,\\%22country\\%22:\\%22Brasil\\%22},\\%22headquarter\\%22:\\%22\\%22,\\%22code\\%22:\\%221410\\%22,\\%22name\\%22:\\%22Leia%20Organa\\%22,\\%22phone\\%22:\\%2211922566801\\%22,\\%22email\\%22:\\%22leia.organa@gmail.com\\%22},\\%22schedule_date\\%22:[{\\%22start_date\\%22:\\%222021-01-06T03:00:00.000Z\\%22}],\\%22site\\%22:\\%225f203551ae950f0f3fbb92b4\\%22,\\%22invoice_number\\%22:\\%2214553\\%22,\\%22order_number\\%22:\\%2200424\\%22,\\%22shipment_number\\%22:\\%22553\\%22,\\%22service_time\\%22:15}]}");
		URL url = new URL("https://company.routeasy.com.br/vehicles?api_key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So"); 
        // Open a connection(?) on the URL(?) and cast the response(??)
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //conn.setRequestProperty("Accept", "application/json");
        //conn.setRequestProperty("Content-Type", "application/json");
        conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        //conn.setRequestMethod("POST");
        
        InputStream responseStream = conn.getInputStream();

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
          (responseStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder;

	}
	
	public static void postPedidoTeste() throws IOException {
        
		JsonObject jo = (JsonObject) Json.createObjectBuilder()
				.add("orders", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("loads",Json.createArrayBuilder()
										.add(BigDecimal.valueOf(0.8))
										.add(BigDecimal.valueOf(0))
										.add(BigDecimal.valueOf(899.49))
										.add(BigDecimal.valueOf(1)))
								.add("location", Json.createObjectBuilder()
										.add("address", Json.createObjectBuilder()
												.add("route", "Rua Bacuri")
												.add("street_number", "89")
												.add("neighborhood", "Vila da Saúde")
												.add("city", "São Paulo")
												.add("states","SP")
												.add("country", "Brasil")
												.add("postal_code", "05469-030"))
										.add("headquarter", "")
										.add("code", "1850")
										.add("name", "Luke Skywalker")
										.add("phone", "11955044801")
										.add("email", "luke.skywalker@gmail.com"))
								.add("constraints", Json.createObjectBuilder()
										.add("skills",Json.createArrayBuilder()
												.add("fragil"))
										.add("prohibited_vehicles", Json.createArrayBuilder()
												.add("5cf5fe2a90d8f250edbea30e")
												.add("5cf5fe2090d8f250edbea30c"))
										.add("window_daily", Json.createArrayBuilder()
												.add(Json.createObjectBuilder()
														.add("start_time", "1970-01-01 08:00 GMT-3")
														.add("end_time", "1970-01-01 11:00 GMT-3"))
												.add(Json.createObjectBuilder()
														.add("start_time", "1970-01-01 13:00 GMT-3")
														.add("end_time", "1970-01-01 18:00 GMT-3")))
										.add("priority", BigDecimal.valueOf(100))
										.add("sequence", BigDecimal.valueOf(20))
										.add("region", "SPA"))
								.add("schedule_date", Json.createArrayBuilder()
										.add(Json.createObjectBuilder()
												.add("start_date", "2021-01-06T03:00:00.000Z")
												.add("end_date", "2021-01-12T03:00:00.000Z")))
								.add("items", Json.createArrayBuilder()
										.add(Json.createObjectBuilder()
												.add("code", "caixa52")
												.add("name", "")
												.add("qty", BigDecimal.valueOf(52)))
										.add(Json.createObjectBuilder()
												.add("code", "item2")))
								.add("site", "5f203551ae950f0f3fbb92b4")
								.add("invoice_number", "14552")
								.add("order_number", "00423")
								.add("shipment_number", "552")
								.add("service_time", BigDecimal.valueOf(10))))
				.build();
		StringBuilder textBuilder = new StringBuilder();
		textBuilder.append(jo);
		//postPedido(textBuilder);
		//return null;
	}
	public static StringBuilder postPedido() throws IOException{
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost("https://company.routeasy.com.br/orders/import?api_key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So");
			JsonObject jo = (JsonObject) Json.createObjectBuilder()
					.add("orders", Json.createArrayBuilder()
							.add(Json.createObjectBuilder()
									.add("loads",Json.createArrayBuilder()
											.add(BigDecimal.valueOf(0.8))
											.add(BigDecimal.valueOf(0))
											.add(BigDecimal.valueOf(899.49))
											.add(BigDecimal.valueOf(1)))
									.add("location", Json.createObjectBuilder()
											.add("address", Json.createObjectBuilder()
													.add("route", "Rua Bacuri")
													.add("street_number", "89")
													.add("neighborhood", "Vila da Saúde")
													.add("city", "São Paulo")
													.add("states","SP")
													.add("country", "Brasil")
													.add("postal_code", "05469-030"))
											.add("headquarter", "")
											.add("code", "1850")
											.add("name", "Luke Skywalker")
											.add("phone", "11955044801")
											.add("email", "luke.skywalker@gmail.com"))
									.add("constraints", Json.createObjectBuilder()
											.add("skills",Json.createArrayBuilder()
													.add("fragil"))
											.add("prohibited_vehicles", Json.createArrayBuilder()
													.add("5cf5fe2a90d8f250edbea30e")
													.add("5cf5fe2090d8f250edbea30c"))
											.add("window_daily", Json.createArrayBuilder()
													.add(Json.createObjectBuilder()
															.add("start_time", "1970-01-01 08:00 GMT-3")
															.add("end_time", "1970-01-01 11:00 GMT-3"))
													.add(Json.createObjectBuilder()
															.add("start_time", "1970-01-01 13:00 GMT-3")
															.add("end_time", "1970-01-01 18:00 GMT-3")))
											.add("priority", BigDecimal.valueOf(100))
											.add("sequence", BigDecimal.valueOf(20))
											.add("region", "SPA"))
									.add("schedule_date", Json.createArrayBuilder()
											.add(Json.createObjectBuilder()
													.add("start_date", "2021-01-06T03:00:00.000Z")
													.add("end_date", "2021-01-12T03:00:00.000Z")))
									.add("items", Json.createArrayBuilder()
											.add(Json.createObjectBuilder()
													.add("code", "caixa52")
													.add("name", "")
													.add("qty", BigDecimal.valueOf(52)))
											.add(Json.createObjectBuilder()
													.add("code", "item2")))
									.add("site", "5f203551ae950f0f3fbb92b4")
									.add("invoice_number", "14552")
									.add("order_number", "00423")
									.add("shipment_number", "552")
									.add("service_time", BigDecimal.valueOf(10))))
					.build();
							
			//StringEntity params = new StringEntity("{\"orders\":[{\"loads\":[0.8,0,899.49,1],\"location\":{\"address\":{\"route\":\"Rua Bacuri\",\"street_number\":\"89\",\"neighborhood\":\"Vila da Saúde\",\"city\":\"São Paulo\",\"state\":\"SP\",\"country\":\"Brasil\",\"postal_code\":\"05469-030\"},\"headquarter\":\"\",\"code\":\"1850\",\"name\":\"Luke Skywalker\",\"phone\":\"11955044801\",\"email\":\"luke.skywalker@gmail.com\"},\"constraints\":{\"skills\":[\"fragil\"],\"prohibited_vehicles\":[\"5cf5fe2a90d8f250edbea30e\",\"5cf5fe2090d8f250edbea30c\"],\"window_daily\":[{\"start_time\":\"1970-01-01 08:00 GMT-3\",\"end_time\":\"1970-01-01 11:00 GMT-3\"},{\"start_time\":\"1970-01-01 13:00 GMT-3\",\"end_time\":\"1970-01-01 18:00 GMT-3\"}],\"priority\":1000,\"sequence\":20,\"region\":\"SPA\"},\"schedule_date\":[{\"start_date\":\"2021-01-06T03:00:00.000Z\",\"end_date\":\"2021-01-12T03:00:00.000Z\"}],\"items\":[{\"code\":\"caixa25\",\"name\":\"\",\"qty\":52},{\"code\":\"item2\"}],\"site\":\"5f203551ae950f0f3fbb92b4\",\"invoice_number\":\"14552\",\"order_number\":\"00423\",\"shipment_number\":\"552\",\"service_time\":10},{\"loads\":[2,0.2,19.9,1],\"location\":{\"address\":{\"route\":\"Av. Rio Branco\",\"street_number\":\"1186\",\"neighborhood\":\"Centro\",\"city\":\"São Paulo\",\"state\":\"SP\",\"postal_code\":\"\",\"country\":\"Brasil\"},\"headquarter\":\"\",\"code\":\"1410\",\"name\":\"Leia Organa\",\"phone\":\"11922566801\",\"email\":\"leia.organa@gmail.com\"},\"schedule_date\":[{\"start_date\":\"2021-01-06T03:00:00.000Z\"}],\"site\":\"5f203551ae950f0f3fbb92b4\",\"invoice_number\":\"14553\",\"order_number\":\"00424\",\"shipment_number\":\"553\",\"service_time\":15}]}");
			StringEntity p = new StringEntity(jo.toString());
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-Type", "application/json");
			//request.addHeader("api-key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So");
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	//POST para criação do pedido na Routeasy
	public static StringBuilder postPedido(JsonArray jo2) throws IOException{
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		JsonObject jo = (JsonObject) Json.createObjectBuilder()
				.add("orders", Json.createArrayBuilder()
						.add(jo2))
				.build();
						
		StringEntity p = new StringEntity(jo.toString());
		//throw new MGEModelException("ordemCarga: " + entity);
		try {
			HttpPost request = new HttpPost("https://company.routeasy.com.br/orders/import?api_key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwODE3MDkxMGY0ZGZiNDIxODk1NWI1NCIsInRpbWVzdGFtcCI6MTY1MDQ3MTM3Mzk1Nn0.ccl6mJHpqYPrfl5SaZZLSDHQIpZCPdqcSS7nudPL8So");				
	
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-Type", "application/json");
			request.setEntity(p);
			
			HttpResponse response = httpClient.execute(request);
			StringBuilder textBuilder = new StringBuilder();
			if(response != null) {
				InputStream in = response.getEntity().getContent();//Recebe dados da resposta
				
		        try (Reader reader = new BufferedReader(new InputStreamReader
		          (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
		            int c = 0;
		            while ((c = reader.read()) != -1) {
		                textBuilder.append((char) c);
		            }
		        }
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return null;
	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {}

	@Override
	public void afterUpdate(PersistenceEvent arg0) throws Exception {
		DynamicVO formacaoCarga = (DynamicVO) arg0.getVo();
		BigDecimal ordemCarga = formacaoCarga.asBigDecimal("ORDEMCARGA");
		BigDecimal CODPARC = formacaoCarga.asBigDecimal("CODPARC");
		StringBuilder textBuilder = new StringBuilder();
		JsonArray jo = null;
		if(ordemCarga != null ) {
			//throw new MGEModelException("ordemCarga: "+formacaoCarga);
			JapeWrapper notaDAO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);
			Collection<DynamicVO> ordens = notaDAO.find("ORDEMCARGA = ?", formacaoCarga.asBigDecimal("ORDEMCARGA"));
			
			JapeWrapper parceiroDAO = JapeFactory.dao(DynamicEntityNames.PARCEIRO);
			JapeWrapper enderecoDAO = JapeFactory.dao(DynamicEntityNames.ENDERECO);
			
			for (DynamicVO it : ordens) {
				//textBuilder = montarArrItem(it.asBigDecimal("NUNOTA"));
				JsonObject j = montarArrItem(it.asBigDecimal("NUNOTA"));
				//throw new MGEModelException("it : "+textBuilder);
				DynamicVO codParc = parceiroDAO.findOne("CODPARC = ?", CODPARC);
				DynamicVO codEnd = enderecoDAO.findOne("CODEND = ?", codParc.asBigDecimal("CODEND"));
				jo = Json.createArrayBuilder()
								.add(Json.createObjectBuilder()
										.add("loads",Json.createArrayBuilder()
												.add(BigDecimal.valueOf(0.8))
												.add(BigDecimal.valueOf(0))
												.add(BigDecimal.valueOf(899.49))
												.add(BigDecimal.valueOf(1)))
										.add("location", Json.createObjectBuilder()
												.add("address", Json.createObjectBuilder()
														.add("route", codEnd.asString("NOMEEND"))
														.add("street_number", codParc.asString("NUMEND"))
														.add("neighborhood", "Vila da Saúde")
														.add("city", codParc.asBigDecimal("CODCID"))
														.add("states","AM")
														.add("country", "Brasil")
														.add("postal_code", codParc.asString("CEP")))
												.add("headquarter", "")
												.add("code", "1850")
												.add("name", codParc.asString("NOMEPARC"))
												.add("phone", codParc.asString("TELEFONE") != null ? codParc.asString("TELEFONE") : "")
												.add("email", codParc.asString("EMAIL")!= null ? codParc.asString("EMAIL") : ""))
										.add("constraints", Json.createObjectBuilder()
												.add("skills",Json.createArrayBuilder()
														.add("fragil"))
												.add("prohibited_vehicles", Json.createArrayBuilder()
														.add("5cf5fe2a90d8f250edbea30e")
														.add("5cf5fe2090d8f250edbea30c"))
												.add("window_daily", Json.createArrayBuilder()
														.add(Json.createObjectBuilder()
																.add("start_time", "1970-01-01 08:00 GMT-3")
																.add("end_time", "1970-01-01 11:00 GMT-3"))
														.add(Json.createObjectBuilder()
																.add("start_time", "1970-01-01 13:00 GMT-3")
																.add("end_time", "1970-01-01 18:00 GMT-3")))
												.add("priority", BigDecimal.valueOf(0))
												.add("sequence", BigDecimal.valueOf(0))
												.add("region", codParc.asBigDecimal("CODREG")))
										.add("schedule_date", Json.createArrayBuilder()
												.add(Json.createObjectBuilder()
														.add("start_date", "2021-01-06T03:00:00.000Z")
														.add("end_date", "2021-01-12T03:00:00.000Z")))
										.add("items", Json.createArrayBuilder()
												.add(j))
										.add("site", "5f203551ae950f0f3fbb92b4")
										.add("invoice_number", "14552")
										.add("order_number", "00423")
										.add("shipment_number", "552")
										.add("service_time", BigDecimal.valueOf(10)))
						.build();
				
				textBuilder.append(jo);
			}
			postPedido(jo);
			//throw new MGEModelException("json: "+textBuilder);
		}

	}

	public JsonObject montarArrItem(BigDecimal nunota) throws Exception {
		JapeWrapper itemNotaDAO = JapeFactory.dao(DynamicEntityNames.ITEM_NOTA);
		JapeWrapper produtoDAO = JapeFactory.dao(DynamicEntityNames.PRODUTO);
		
		Collection<DynamicVO> itens = itemNotaDAO.find("NUNOTA = ?", nunota);
		//StringBuilder textBuilder = new StringBuilder();
		JsonObject jo = null;
		for (DynamicVO it : itens) {
			DynamicVO produtoVO = produtoDAO.findOne("CODPROD = ?", it.asBigDecimal("CODPROD"));
			System.out.println(produtoVO);
			jo = (JsonObject) Json.createObjectBuilder()
					.add("code", it.asBigDecimal("CODPROD"))
					.add("name", produtoVO.asString("DESCRPROD"))
					.add("qty", it.asBigDecimal("QTDNEG")).build();
			//textBuilder.append(jo.toString());
		}
		
		return jo;
	} 

	@Override
	public void beforeCommit(TransactionContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
