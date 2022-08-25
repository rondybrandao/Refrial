package br.com.sankhya.eventos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

public class LiberacaoBancaria implements EventoProgramavelJava {

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {
		
		DynamicVO registro = (DynamicVO) arg0.getVo();
		BigDecimal VLRLANC = registro.asBigDecimal("VLRLANC");
		
		JapeWrapper limiteDAO = JapeFactory.dao(DynamicEntityNames.LIBERACAO_LIMITE);
		
		JapeWrapper usuarioDAO = JapeFactory.dao(DynamicEntityNames.USUARIO);
		Collection<DynamicVO> searchDiretoria = usuarioDAO.find("CODGRUPO = 5");
		Collection<DynamicVO> searchGerFin = usuarioDAO.find("CODGRUPO = 26");
		
		//String dhAtual = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		Date date = new Date();  
        Timestamp dh=new Timestamp(date.getTime());   
		
		if(VLRLANC.compareTo(new BigDecimal("0")) == 1 && VLRLANC.compareTo(new BigDecimal("200000")) == -1) {
			for (DynamicVO it : searchGerFin) {
				limiteDAO.create()
				.set("NUCHAVE", registro.asBigDecimal("CODLIBMBC"))
		    	.set("VLRATUAL", VLRLANC)
		    	.set("CODUSULIB", it.asBigDecimal("CODUSU"))
		    	.set("CODUSUSOLICIT", BigDecimal.valueOf(0))
		    	//.set("CODTIPOPER", BigDecimal.valueOf(1602))
		    	//.set("DHTIPOPER", registro.asBigDecimal("DHTIPOPER"))
		    	.set("TABELA", "AD_LIBMBC")
		    	.set("EVENTO", BigDecimal.valueOf(1003))
		    	.set("DHSOLICIT", dh)
		    	.set("VLRLIMITE", VLRLANC)
		    	.save();
			}
			
		} else {
			//throw new MGEModelException("erro" + VLRLANC);
			for(DynamicVO it : searchDiretoria) {
				limiteDAO.create()
				.set("NUCHAVE", registro.asBigDecimal("CODLIBMBC"))
		    	.set("VLRATUAL", VLRLANC)
		    	.set("CODUSULIB", it.asBigDecimal("CODUSU"))
		    	.set("CODUSUSOLICIT", BigDecimal.valueOf(0))
		    	//.set("CODTIPOPER", BigDecimal.valueOf(1602))
		    	.set("TABELA", "AD_LIBMBC")
		    	.set("EVENTO", BigDecimal.valueOf(1003))
		    	.set("DHSOLICIT", dh)
		    	.set("VLRLIMITE", VLRLANC)
		    	.save();
			}
			
		}

	}

	@Override
	public void afterUpdate(PersistenceEvent arg0) throws Exception {
		

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
		DynamicVO tela = (DynamicVO) arg0.getVo();
		String status = tela.asString("STATUS");
		BigDecimal codLiberador = tela.asBigDecimal("CODUSULIB");
		//int codLibMbc = tela.asBigDecimal("CODLIBMBC").intValue();
		
		//String LIBERADO = "LIBERADO";
		//throw new MGEModelException("Não é possivel alterar registro após STATUS LIBERADO " + status );
		/*switch (status) {
			case "LIBERADO": 
				throw new MGEModelException("Não é possivel alterar registro após STATUS LIBERADO "+status);
			case "CONFIRMANDO":
				JapeWrapper libMbcDAO = JapeFactory.dao("AD_LIBMBC");
				DynamicVO libMbcVO = libMbcDAO.findOne("CODLIBMBC = ?", codLibMbc);
				libMbcDAO.prepareToUpdate(libMbcVO)
				.set("STATUS", "APROVADO")
				//.set("CODUSULIB", libMbcVO.asBigDecimal("CODUSULIB").intValue())
				.update();
			case "PENDENTE":
				return;
		}*/
		if(status.equals("LIBERADO")) {
			throw new MGEModelException("Não é possivel alterar o registro após STATUS LIBERADO");
		} 
	}

}
