 SELECT
        CODVEND AS ID,
        APELIDO AS NOME_COMPLETO
FROM          TGFVEN
WHERE        (TIPVEND = 'C') 
        AND (ATIVO = 'S')