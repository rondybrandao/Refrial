package br.com.sankhya.eventos;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

public class EventoLiberacaoBancaria implements EventoProgramavelJava {

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {
		/*DynamicVO registro = (DynamicVO) arg0.getVo();
		int evento = registro.asBigDecimal("EVENTO").intValue();
		String reprovado = registro.asString("REPROVADO");
		
		JapeWrapper contaDAO = JapeFactory.dao(DynamicEntityNames.MOVIMENTO_BANCARIO);
		JapeWrapper ad_libmbcDAO = JapeFactory.dao(DynamicEntityNames.FINANCEIRO);
		
		DynamicVO financeiro = ad_libmbcDAO.findOne("NUFIN = ?", registro.asBigDecimal("NUCHAVE").intValue());
		//BigDecimal VLRLANC = CODLIBMBC.asBigDecimal("VLRLANC");
		//Timestamp dtLanc = CODLIBMBC.asTimestamp(CODLIBMBC.asTimestamp("DTLANC").toString()); 
		
		if(evento == 1003 && reprovado.equals("N")) {
			//throw new MGEModelException("erro" + evento);
			contaDAO.create()
			.set("ORIGMOV", "A")
			.set("DTLANC", registro.asTimestamp("DHSOLICIT"))
			.set("VLRLANC", registro.asBigDecimal("VLRATUAL"))
			.set("CODCTABCOINT", financeiro.asBigDecimal("AD_CODCTABCOORI"))
			.set("CODLANC", financeiro.asBigDecimal("AD_CODLANCORIG"))
			.set("CODCTABCOINTDEST", financeiro.asBigDecimal("AD_CODCTAMBCDEST"))
			.set("CODLANCDEST", financeiro.asBigDecimal("AD_CODLANCDEST"))
			.save();
			
		}*/

	}

	@Override
	public void afterUpdate(PersistenceEvent arg0) throws Exception {
		DynamicVO registro = (DynamicVO) arg0.getVo();
		int evento = registro.asBigDecimal("EVENTO").intValue();
		//String reprovado = registro.asString("REPROVADO");
		Timestamp DHLIB = registro.asTimestamp("DHLIB");
		BigDecimal usuarioLiberador = registro.asBigDecimal("CODUSULIB");
		
		JapeWrapper movimentoBancarioDAO = JapeFactory.dao(DynamicEntityNames.MOVIMENTO_BANCARIO);
		JapeWrapper libMbcDAO = JapeFactory.dao("AD_LIBMBC");
		
		DynamicVO libMbcVO = libMbcDAO.findOne("CODLIBMBC = ?", registro.asBigDecimal("NUCHAVE").intValue());
		//BigDecimal VLRLANC = CODLIBMBC.asBigDecimal("VLRLANC");
		//Timestamp dtLanc = CODLIBMBC.asTimestamp(CODLIBMBC.asTimestamp("DTLANC").toString()); 
		
		if(evento == 1003 && DHLIB != null) {
			//throw new MGEModelException("CODLIBMBC: " + DHLIB);
			movimentoBancarioDAO.create()
			.set("ORIGMOV", "A") 
			.set("DTLANC", registro.asTimestamp("DHSOLICIT"))
			.set("CODTIPOPER", libMbcVO.asBigDecimal("CODTIPOPER"))
			.set("DHTIPOPER", libMbcVO.asTimestamp("DHTIPOPER"))
			.set("VLRLANC", registro.asBigDecimal("VLRATUAL"))
			.set("CODCTABCOINT", libMbcVO.asBigDecimal("CODCTABCOORI"))
			.set("CODLANC", libMbcVO.asBigDecimal("CODLANC"))
			.set("CODCTABCOINTDEST", libMbcVO.asBigDecimal("CODCTAMBCDEST"))
			.set("CODLANCDEST", libMbcVO.asBigDecimal("CODLANCDEST"))
			.save();
			//validaStatus(libMbcVO, usuarioLiberador);
			libMbcDAO.prepareToUpdate(libMbcVO)
			//.set("STATUS", "LIBERADO")
			.set("CODUSULIB", usuarioLiberador)
			.update();
			
		} else {
			return;
			//throw new MGEModelException("Não foi possivel concluir a transação");
		}

	}
	
	public void validaStatus(DynamicVO libMbcVO, BigDecimal usuarioLiberador) throws MGEModelException{
		String s = libMbcVO.asString("STATUS");
		JapeWrapper libMbcDAO = JapeFactory.dao("AD_LIBMBC");
		if(s.equals("PENDENTE")) {
			try {
				libMbcDAO.prepareToUpdate(libMbcVO)
				.set("STATUS", "LIBERADO")
				.set("CODUSULIB", usuarioLiberador)
				.update();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {throw new MGEModelException("Não foi possivel concluir a transação"+libMbcVO);}
		
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
