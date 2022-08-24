 SELECT
        CAB.NUNOTA AS IDSOLICITACAO,
        
			CAB.DTNEG AS DATA_SOLICITACAO,
        
			CAB.DTNEG AS DATA_LIBERACAO,
        
			ITE.SEQUENCIA AS ITEM,
        
			ITE.CODPROD AS IDPRODUTO,
        
			PRO.DESCRPROD AS DESCRICAO_PRODUTO,
        
            CASE 
            WHEN ITE.QTDENTREGUE <> ITE.QTDNEG 
            AND ITE.PENDENTE = 'N' 
				THEN ITE.QTDENTREGUE 
				ELSE ITE.QTDNEG 
			END AS QTDE_SOLICITADA,
            
			ITE.QTDENTREGUE AS QTDE_ENTREGUE,
            
            PRO.CODVOL AS UNIDADE_MEDIDA,
            
			CAB.NUNOTA AS ORDEM_COMPRA,
            
			CAB.CODEMP AS IDFILIAL,
            
			CAB.DTPREVENT AS DATA_PREVISAO,
            
			CAB.DTFATUR AS DATA_FATURAMENTO,
            
            ITE.VLRUNIT AS PRECO_COMPRA_SOLICITACAO,
            
			ITE.VLRUNIT AS PRECO_LIQUIDO_COMPRA,
            
			CUS.CUSREP AS PRECO_COMPRA_ANTERIOR_COMPRA,
            
            CASE 
				WHEN TPO.AD_USASYSTOCK = 'S' 
				THEN 'PROGRAMADO' 
				ELSE 'PEDIDO' 
			END AS FLAG_PEDIDO_FATURAMENTO
FROM        TGFCAB CAB 
            INNER JOIN
            TGFITE ITE 
                ON CAB.NUNOTA = ITE.NUNOTA INNER JOIN
            TGFPRO PRO 
                    ON ITE.CODPROD = PRO.CODPROD LEFT OUTER JOIN
            TGFVOA VOA 
                        ON VOA.CODPROD = ITE.CODPROD 
                        AND 
			VOA.CODVOL = ITE.CODVOL 
                        AND 
			(ITE.CONTROLE IS NULL 
                        AND VOA.CONTROLE = ' ' 
                        OR ITE.CONTROLE IS NOT NULL 
                        AND ITE.CONTROLE = VOA.CONTROLE) LEFT OUTER JOIN
            TGFCUS  CUS 
                            ON CUS.CODPROD = PRO.CODPROD 
                            AND 
			CUS.CODEMP = CAB.CODEMP INNER JOIN
			TGFTOP TPO 
				ON TPO.CODTIPOPER = CAB.CODTIPOPER 
				AND CAB.DHTIPOPER = TPO.DHALTER INNER JOIN
			TGFEMP EMP 
					ON CAB.CODEMP = EMP.CODEMP			
			
WHERE       (CAB.TIPMOV = 'O') 
                            AND 
			(EMP.AD_USASYSTOCK = 'S') 
                            AND 
			(TO_CHAR(CAB.DTNEG,
                            'YYYY') >= TO_CHAR(SYSDATE,
                            'YYYY') - 2) AND
			CUS.DTATUAL =(SELECT
                                MAX(DTATUAL) AS Expr1
                          FROM
                                    TGFCUS C
                          
                            WHERE
                                (
                                    C.CODPROD = CUS.CODPROD
                                ) 
                                AND (
                                    C.DTATUAL <= CAB.DTNEG
                                ))