SELECT
        CAB.TIPMOV,
        
       CAB.NUNOTA,
        
       CAB.NUMNOTA,
        
       CAB.NUREM,
        
       CAB.DTNEG,
        
       CAB.DTALTER,
        
       PAR.CODPARC,
        
       PAR.RAZAOSOCIAL,
        
       EMP.NOMEFANTASIA,
        
       CAB.CODEMP
  
    FROM
        TGFCAB CAB
 
    INNER JOIN
        TGFPAR PAR 
            on CAB.CODPARC = PAR.CODPARC
 
    INNER JOIN
        TSIEMP EMP 
            on CAB.CODEMPNEGOC = EMP.CODEMP
 
    INNER JOIN
        TGFEMP PEMP 
            on EMP.CODEMP = PEMP.CODEMP
 
    INNER JOIN
        TGFTOP TPO 
            ON CAB.CODTIPOPER = TPO.CODTIPOPER 
            AND CAB.DHTIPOPER = TPO.DHALTER
 
    WHERE
        CAB.TIPMOV IN (
            'C', 'D'
        )
   
        AND CAB.PENDENTE = 'N'
   
        AND PEMP.AD_USAWMS = 'S'
   
        AND CAB.STATUSNOTA = 'L'   
   
        AND TPO.AD_USAWMS = 'S'
   
        AND CAB.AD_STATUSWMS IS NULL
 
    ORDER BY
        CAB.DTALTER