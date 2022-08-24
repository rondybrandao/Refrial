   SELECT
        IDFILIAL,
        
			IDPRODUTO,
        
			COD_PRODUTO,
        
			DESCRICAO_PRODUTO,
        
			UNIDADE_VENDA,
        
			UNIDADE_COMPRA,
        
			VALOR_UNITARIO,
        
			CUSTO_UNITARIO,
        
			VALOR_COMPRA,
        
			SUM(ESTOQUE) AS ESTOQUE,
        
            SUM(ESTOQUE_BLOQUEADO) AS ESTOQUE_BLOQUEADO,
        
			IDDEPARTAMENTO,
        
			IDCATEGORIA,
        
			IDCOMPRADOR,
        
			IDFORNECEDOR,
        
			REVENDA,
        
			IMPORTADO,
        
			FATOR_CONVERSAO,
        
			LOTE_MINIMO,
        
			EMBALAGEM_PADRAO,
        
            CODIGO_BARRAS,
        
			DATA_CADASTRO,
        
			STATUS
			
FROM            (SELECT
            TO_CHAR(CASE 
                                                    
                WHEN EST.CODEMP IS NULL THEN 1 
                                                    
                ELSE EST.CODEMP 
                                                 
            END) AS IDFILIAL,
            
                                                 PRO.CODPROD IDPRODUTO,
            
                                                 PRO.REFFORN COD_PRODUTO,
            
                                                 PRO.DESCRPROD DESCRICAO_PRODUTO,
            
                                                 PRO.CODVOL UNIDADE_VENDA,
            
                                                 PRO.CODVOL UNIDADE_COMPRA,
            
                                                 EXC.VLRVENDA VALOR_UNITARIO,
            
                                                 CUS.CUSREP CUSTO_UNITARIO,
            
                                                 CUS.CUSREP  VALOR_COMPRA,
            
                                                 EST.ESTOQUE ESTOQUE,
            
                                                 EST.RESERVADO ESTOQUE_BLOQUEADO,
            
                                                 PAI.CODGRUPOPROD IDDEPARTAMENTO,
            
                                                 GRU.CODGRUPOPROD IDCATEGORIA,
            
                                                 PAR.CODVEND IDCOMPRADOR,
            
                                                 PRO.CODPARCFORN IDFORNECEDOR,
            
                                                 'S' AS REVENDA,
            
                                                 CASE 
                WHEN PRO.ORIGPROD IN (1,
                2,
                6,
                7) THEN 'S' 
                                                 
                ELSE 'N' 
            END AS IMPORTADO,
            
                                                 CASE 
                WHEN PRO.ATIVO = 'N' THEN 'FL' 
                ELSE 'A' 
            END AS STATUS,
            
                                                 1 AS FATOR_CONVERSAO,
            
                                                 NVL(PRO.AGRUPMIN,
            0) AS LOTE_MINIMO,
            
                                                 PRO.CODVOL AS EMBALAGEM_PADRAO,
            
                                                 PRO.REFERENCIA AS CODIGO_BARRAS,
            
                                                 MIN(ALT.DHACAO) AS DATA_CADASTRO
                          
        FROM
            TGFPRO PRO 
        LEFT OUTER JOIN
                                          TGFEXC EXC 
            ON PRO.CODPROD = EXC.CODPROD LEFT OUTER JOIN
                                          TGFTAB TAB 
                ON EXC.NUTAB = TAB.NUTAB LEFT OUTER JOIN
                                          TGFCUS  CUS 
                    ON PRO.CODPROD = CUS.CODPROD LEFT OUTER JOIN
                                          TGFEST  EST 
                        ON PRO.CODPROD = EST.CODPROD LEFT OUTER JOIN
                                          TGFGRU  GRU 
                            ON GRU.CODGRUPOPROD = PRO.CODGRUPOPROD LEFT OUTER JOIN
                                          TGFGRU  PAI 
                                ON GRU.CODGRUPAI = PAI.CODGRUPOPROD LEFT OUTER JOIN
                                          TGFPAR  PAR 
                                    ON PAR.CODPARC = PRO.CODPARCFORN LEFT OUTER JOIN
                                          TSIALT  ALT 
                                        ON PRO.CODPROD = ALT.CHAVE 
                                        AND ALT.NOMETAB = 'TGFPRO' INNER JOIN
										  TGFEMP  EMP 
                                            ON CUS.CODEMP = EMP.CODEMP
                          WHERE
                                                (
                                                    TAB.DTVIGOR =
                                                        (
                                                        SELECT
                                                            MAX(T.DTVIGOR) AS Expr1
                                                          
                                                    FROM
                                                        TGFTAB T 
                                                    INNER JOIN
                                                                          TGFEXC E 
                                                        ON T.NUTAB = E.NUTAB
                                                          WHERE
                                                            (
                                                                E.CODPROD = PRO.CODPROD
                                                            ) 
                                                            AND (
                                                                T.CODTAB = 0
                                                            )
                                                    )
                                                ) 
                                                AND (
                                                    TAB.CODTAB = 0
                                                ) 
                                                AND (
                                                    EMP.AD_USASYSTOCK = 'S'
                                                ) 
                                                AND (
                                                    CUS.DTATUAL =
                                                        (
                                                        SELECT
                                                            MAX(DTATUAL) AS Expr1
                                                          
                                                    FROM
                                                        TGFCUS  C
                                                          
                                                    WHERE
                                                        (
                                                            CODPROD = CUS.CODPROD
                                                        ) 
                                                        AND (
                                                            EMP.AD_USASYSTOCK = 'S'
                                                        )
                                                )
                                            )
                          
                                        GROUP BY
                                            
                                   PRO.CODPROD,
                                            
                                   PRO.REFFORN,
                                            
                                   PRO.DESCRPROD,
                                            
                                   PRO.CODVOL,
                                            
                                   PRO.CODVOL,
                                            
                                   EXC.VLRVENDA,
                                            
								   PRO.CODVOL,
                                            
								   EXC.VLRVENDA,
                                            
								   CUS.CUSREP,
                                            
								   PAI.CODGRUPOPROD,
                                            
								   GRU.CODGRUPOPROD,
                                            
								   PAR.CODVEND,
                                            
								   PRO.CODPARCFORN,
                                            
								   CASE 
                                                WHEN PRO.ORIGPROD IN (1,
                                                2,
                                                6,
                                                7) THEN 'S' 
                                                ELSE 'N' 
                                            END,
                                            PRO.ORIGPROD,
                                            
								   CASE 
                                                WHEN EST.CODEMP IS NULL THEN 1 
                                                ELSE EST.CODEMP 
                                            END,
                                            
								   CASE 
                                                WHEN PRO.ATIVO = 'N' THEN 'FL' 
                                                ELSE 'A' 
                                            END,
                                            PRO.ATIVO,
                                            
								   PRO.REFERENCIA,
                                            PRO.AGRUPMIN,
                                            EST.ESTOQUE,
                                            EST.RESERVADO) A

GROUP 
                                        BY
                                            	IDFILIAL,
                                            IDPRODUTO,
                                            COD_PRODUTO,
                                            DESCRICAO_PRODUTO,
                                            UNIDADE_VENDA,
                                            
UNIDADE_COMPRA,
                                            VALOR_UNITARIO,
                                            CUSTO_UNITARIO,
                                            VALOR_COMPRA,
                                            IDDEPARTAMENTO,
                                            
IDCATEGORIA,
                                            IDCOMPRADOR,
                                            IDFORNECEDOR,
                                            REVENDA,
                                            IMPORTADO,
                                            
FATOR_CONVERSAO,
                                            LOTE_MINIMO,
                                            EMBALAGEM_PADRAO,
                                            CODIGO_BARRAS,
                                            DATA_CADASTRO,
                                            
STATUS
ORDER 
                                        BY
                                            IDPRODUTO