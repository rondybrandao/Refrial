package br.com.sankhya.eventos;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.bmp.PersistentLocalEntity;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

public class CalculoTCIF implements EventoProgramavelJava {

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterUpdate(PersistenceEvent arg0) throws Exception {
		DynamicVO registro = (DynamicVO) arg0.getVo();
		if(registro.asBigDecimal("ORDEMCARGA") == null) {
			try {
				JapeWrapper parceiroDAO = JapeFactory.dao(DynamicEntityNames.PARCEIRO);
				JapeWrapper notaDAO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);
				JapeWrapper cidadeDAO = JapeFactory.dao(DynamicEntityNames.CIDADE);
				
				int codparc = registro.asBigDecimal("CODPARC").intValue();
				BigDecimal valorNota = registro.asBigDecimal("VLRNOTA");
				DynamicVO searchParceiro = parceiroDAO.findOne("CODPARC = ?", codparc);
				DynamicVO notaVO = notaDAO.findOne("NUNOTA = ?", registro.asBigDecimal("NUNOTA").intValue());
				DynamicVO cidadeVO = cidadeDAO.findOne("CODCID = ?", searchParceiro.asBigDecimal("CODCID").intValue());
				
				int codUf = cidadeVO.asBigDecimal("UF").intValue();
				String tipoMovimento = registro.asString("TIPMOV");
				BigDecimal fase1 = null;
				//BigDecimal fase2 = null;
				BigDecimal faseDois = null;
				
				if(tipoMovimento.equals("C") && codUf != 18) {
					JapeWrapper itemNotaDAO = JapeFactory.dao(DynamicEntityNames.ITEM_NOTA);
					//DynamicVO searchItemNota = itemNotaDAO.findOne("NUNOTA = ?", registro.asBigDecimal("NUNOTA").intValue());
					Collection<DynamicVO> searchItemNota = itemNotaDAO.find("NUNOTA = ?", registro.asBigDecimal("NUNOTA"));
					//valorItemNota.addAll(searchItemNota);
					
					fase1 = validarFase1(valorNota);
					faseDois = validarFaseDois(searchItemNota);
					
					BigDecimal valorTCIF = fase1.add(faseDois);
					notaDAO.prepareToUpdate(notaVO)
					.set("AD_VLRTCIF", valorTCIF)
					.update();
					
					
				} else {
					//return;
					//throw new MGEModelException("top: " + tipoMovimento + "UF: " + codUf);
					notaDAO.prepareToUpdate(notaVO)
					.set("AD_VLRTCIF", BigDecimal.valueOf(0))
					.update();
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				throw new MGEModelException("erro" + e);
			}
		} 
		
	}
	
	private BigDecimal validarFaseDois(Collection<DynamicVO> vlrItem) throws MGEModelException {
		BigDecimal fase2 = new BigDecimal(0);
		for (DynamicVO item: vlrItem) {
			BigDecimal vlrUnit = item.asBigDecimal("VLRUNIT");
			BigDecimal valida = validarFase2(vlrUnit, item.asBigDecimal("QTDNEG"));
			fase2 = fase2.add(valida);
		}
		return fase2;
	}

	public BigDecimal validarFase1(BigDecimal v){
		BigDecimal tx = v.multiply(BigDecimal.valueOf(0.005));
		if( tx.compareTo(BigDecimal.valueOf(200)) == -1){
			return tx;
		} else {
			return BigDecimal.valueOf(200);
		}
		
	}
	
	public BigDecimal validarFase2(BigDecimal v, BigDecimal q) throws MGEModelException{
		BigDecimal tx = v.multiply(BigDecimal.valueOf(0.005));
	 
		if( tx.compareTo(BigDecimal.valueOf(30)) == -1){
			return tx.multiply(q);
		} else {
			return BigDecimal.valueOf(30).multiply(q);
		}
		
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
