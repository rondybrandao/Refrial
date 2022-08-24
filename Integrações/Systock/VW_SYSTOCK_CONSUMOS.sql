 SELECT
        CAB.CODEMP AS IDFILIAL,
        
            CAB.NUNOTA AS IDPEDIDO,
        
            CAB.DTNEG AS EMISSAO,
        
            CAB.HRMOV AS HORARIOMOV,
        
            ITE.CODPROD AS IDPRODUTO,
        
            ITE.SEQUENCIA AS ITEM,
        
            ITE.QTDNEG AS QTDE,
        
            ITE.VLRUNIT AS VALOR_UNIT_VENDA,
        
            CUS.CUSREP AS VALOR_UNIT_CMV,
        
            CAB.TIPMOV
FROM            TGFCAB CAB 
    INNER JOIN
                TGFITE ITE 
        ON CAB.NUNOTA = ITE.NUNOTA INNER JOIN
                TGFCUS CUS 
            ON CUS.CODPROD = ITE.CODPROD INNER JOIN
                TGFPRO PRO 
                ON PRO.CODPROD = ITE.CODPROD INNER JOIN
				TGFTOP TPO 
                    ON TPO.CODTIPOPER = CAB.CODTIPOPER 
                    AND CAB.DHTIPOPER = TPO.DHALTER INNER JOIN
				TGFEMP EMP 
                        ON CAB.CODEMP = EMP.CODEMP
WHERE        (CAB.TIPMOV IN ('V',
                        'D')) 
                        AND (TPO.AD_USASYSTOCK = 'S') 
                        AND (CUS.DTATUAL =
                             (SELECT
                            MAX(DTATUAL) AS Expr1
                               FROM
                                TGFCUS C
                               
                        WHERE
                            (
                                CODPROD = CUS.CODPROD
                            ) 
                            AND (
                                DTATUAL <= CAB.DTNEG
                            ))) 
                        AND 
                                            (TO_CHAR(CAB.DTNEG,
                        'YYYY') >= TO_CHAR(SYSDATE,
                        'YYYY') - 2) 
                        AND 
                                            (EMP.AD_USASYSTOCK = 'S') 
                        AND (PRO.ATIVO = 'S') OR
                                            (TPO.AD_USASYSTOCK = 'S') 
                        AND 
                                            (CUS.DTATUAL =(SELECT
                            MAX(DTATUAL) AS Expr1
                                                           
                        FROM
                            TGFCUS C
                                                           
                        WHERE
                            (
                                CODPROD = CUS.CODPROD
                            ) 
                            AND (
                                DTATUAL <= CAB.DTNEG
                            ))) 
                        AND 
                                            (TO_CHAR(CAB.DTNEG,
                        'YYYY') >= TO_CHAR(SYSDATE,
                        'YYYY') - 2) 
                        AND (TPO.AD_USASYSTOCK = 'S')