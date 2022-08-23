 SELECT
        PRO.CODPROD CODITEM,
        
        PRO.DTALTER,
        
        VOA.CODBARRA,
        
        VOA.QUANTIDADE QUANTIDADE,
        
        CASE Length(VOA.CODBARRA) 
            
            WHEN 13 THEN 1 
            
            WHEN 14 THEN 2 
            
            ELSE 3 
        
        END AS TIPO,
        
        TO_CHAR(0) EMPRESA
FROM TGFPRO PRO 
    INNER JOIN
     TGFVOA VOA 
        ON PRO.CODPROD = VOA.CODPROD
WHERE VOA.CODBARRA IS NOT NULL