package br.com.gerenciarota.services;

import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.SessionBean;


import com.google.gson.JsonObject;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.BaseSPBean;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.ws.ServiceContext;

/**
 * @author Rondynely
 * @ejb.bean name="GerenciaRotaSP" 
 * jndi-name="br/com/gerenciarota/services/GerenciaRotaSP" 
 * type="Stateless" 
 * transaction-type="Container" 
 * view-type="remote"
 * 
 * @ejb.transaction type="Supports"
 *
 * @ejb.util generate="false"
 */
public class GerenciaRotaSPBean extends BaseSPBean implements SessionBean {
	
	/**  *  */
	private static final long serialVersionUID = 1L;

	/**
	 * @ejb.interface-method tview-tipe="remote"
	 * @ejb.transaction type="Required"
	 */
	public void finalizaRota(ServiceContext sctx) {
		SessionHandle hnd = null;
		JsonObject response = new JsonObject();
		
		try {
			hnd = JapeSession.open();
			JsonObject req = sctx.getJsonRequestBody();
			String status = req.get("status").getAsString();
			BigDecimal nunota = req.get("code").getAsBigDecimal();
			
			JapeWrapper cabecalhoNotaDAO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);
			DynamicVO notaVO =  cabecalhoNotaDAO.findOne("NUNOTA = ?", nunota);
			
			cabecalhoNotaDAO.prepareToUpdate(notaVO)
			.set("AD_STATUSROTA", status)
			.update();
			
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("response", "Erro ao abrir sessão" + e.getMessage());
		} finally {
			JapeSession.close(hnd);
		}
		sctx.setJsonResponse(response);
	}
}
