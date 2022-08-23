   SELECT
        
	prod.CODPROD
	,
        prod.DESCRPROD
	,
        prod.CODVOL
	,
        voa.AD_PESO
	,
        voa.QUANTIDADE
	,
        prod.ATIVO
	,
        prod.PRAZOVAL		--Aba Medidas e Estoque
	,
        voa.LASTRO
	,
        prod.DTALTER
	,
        prod.REFERENCIA
	,
        voa.CODBARRA
FROM 
	TGFPRO prod
	LEFT 
    JOIN
        TGFVOA voa 
            ON voa.CODPROD = prod.CODPROD
WHERE PROD.AD_USAWMS = 'S'		--Aba Medidas e Estoque
ORDER 
    BY
        prod.DTALTER ASC