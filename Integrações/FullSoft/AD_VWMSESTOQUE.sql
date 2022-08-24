   SELECT
        CODPROD
,
        SUM(ESTOQUE) AS ESTOQUE
,
        CODEMP
FROM TGFEST
WHERE ESTOQUE > 0
GROUP 
    BY
        CODPROD
	,
        CODEMP
ORDER 
    BY
        CODPROD