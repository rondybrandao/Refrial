  SELECT
        CAB.NUNOTA
	,
        ITE.SEQUENCIA
	,
        ITE.CODPROD
	,
        ITE.QTDNEG
	,
        LOC.CODLOCAL
    ,
        ITE.DTALTER
FROM TGFCAB CAB
INNER 
    JOIN
        TGFITE ITE 
            ON CAB.NUNOTA = ITE.NUNOTA
INNER 
    JOIN
        TGFLOC LOC 
            ON ITE.CODLOCALORIG = LOC.CODLOCAL
WHERE CAB.NUNOTA IN (
                SELECT
                    NUNOTA 
            FROM
                AD_VWMSENTRADA
        )
	AND (
            CAB.AD_STATUSWMS <> 'E' 
            OR CAB.AD_STATUSWMS IS NULL
        )