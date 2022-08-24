SELECT
        V.NUNOTAORIG AS ORDEM_COMPRA,
        
            CAB.DTENTSAI AS DATA_ENTRADA,
        
            CAB.NUMNOTA AS NRO_NFE,
        
            ITE.CODPROD AS IDPRODUTO,
        
            PRO.DESCRPROD AS DESCRICAO_PRODUTO,
        
            ITE.SEQUENCIA AS ITEM,
        
            ITE.QTDNEG AS QTDE,
        
            ITE.VLRUNIT AS VALOR_UNIT_NF,
        
            PRO.CODVOLCOMPRA AS UNIDADE_MEDIDA,
        
            CAB.CODEMP AS IDFILIAL,
        
            CAB.CHAVENFE AS CHAVE_NFE
FROM           TGFCAB CAB 
    INNER JOIN
                         TGFITE ITE 
        ON CAB.NUNOTA = ITE.NUNOTA INNER JOIN
                         TGFVAR V 
            ON CAB.NUNOTA = V.NUNOTA INNER JOIN
                         TGFPRO PRO 
                ON ITE.CODPROD = PRO.CODPROD LEFT OUTER JOIN
                         TGFVOA VOA 
                    ON VOA.CODPROD = ITE.CODPROD 
                    AND 
                         VOA.CODVOL = ITE.CODVOL 
                    AND 
                         (ITE.CONTROLE IS NULL 
                    AND 
                         VOA.CONTROLE = ' ' OR
                         ITE.CONTROLE IS NOT NULL 
                    AND ITE.CONTROLE = VOA.CONTROLE) INNER JOIN
                        TGFTOP TPO 
                            
                        ON TPO.CODTIPOPER = CAB.CODTIPOPER 
                            
                        AND CAB.DHTIPOPER = TPO.DHALTER INNER JOIN
                        TGFEMP EMP 
                                
                            ON CAB.CODEMP = EMP.CODEMP
                         
                         
WHERE       (CAB.TIPMOV = 'C') 
                            AND 
            (TPO.AD_USASYSTOCK = 'S') 
                            AND 
            (EMP.AD_USASYSTOCK = 'S') 
                            AND 
            (TO_CHAR(CAB.DTNEG,
                            'YYYY') >= TO_CHAR(SYSDATE,
                            'YYYY') - 2)