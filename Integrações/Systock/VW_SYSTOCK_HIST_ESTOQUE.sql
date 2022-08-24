 SELECT
        DATA
	,
        IDPRODUTO
	,
        IDFILIAL
	,
        SALDO + QTDNEG AS SALDO
FROM (
	SELECT DATA
		,
        IDPRODUTO
		,
        IDFILIAL
		,
        SUM(QTDNEG) AS QTDNEG
		,
        SALDO
	FROM (
		SELECT DISTINCT (
				CASE 
					WHEN TO_CHAR(DTENTSAI,
        'DD/MM/YYYY') = '01/10/8201'
						THEN '2017/08/02'
					ELSE TO_CHAR(DTENTSAI,
        'DD/MM/YYYY')
					END
				) AS DATA
			,
        ITE.CODPROD AS IDPRODUTO
			,
        CAB.CODEMP AS IDFILIAL
			,
        ITE.ATUALESTOQUE * ITE.QTDNEG AS QTDNEG
			,
        (
				SELECT NVL(SUM(ESTOQUE),
        0) AS Expr1
				FROM TGFEST EST
				WHERE (TIPO = 'P')
					AND (CODPROD = PRO.CODPROD)
					AND (CODEMP = ITE.CODEMP)
				) - (
				SELECT NVL(SUM(QTDNEG * (
								CASE 
									WHEN (
											ESE.ATUALESTOQUE <> 0
											AND NVL(ESE.ATUALESTTERC,
        'N') IN (
												'N'
												,
        'T'
												,
        'D'
												)
											)
										THEN ESE.ATUALESTOQUE
									WHEN (
											ESE.ATUALESTOQUE = 0
											AND NVL(ESE.ATUALESTTERC,
        'N') IN (
												'P'
												,
        'R'
												)
											)
										THEN CASE 
												WHEN (NVL(ESE.ATUALESTTERC,
            'N') = 'P')
													THEN 1
												ELSE - 1
												END
									END
								)),
            0) AS Expr1
				FROM TGFESE ESE
				WHERE (CODPROD = PRO.CODPROD)
					AND (CODEMP = ITE.CODEMP)
					AND (ATUALESTOQUE <> 0)
					AND (
						NVL(ATUALESTTERC,
            'N') IN (
							'N'
							,
            'T'
							,
            'D'
							)
						)
					AND (DTREFERENCIA >= CAB.DTENTSAI)
					AND (DTREFERENCIA >= '30/12/1900')
					OR (CODPROD = PRO.CODPROD)
					AND (CODEMP = ITE.CODEMP)
					AND (ATUALESTOQUE = 0)
					AND (
						NVL(ATUALESTTERC,
            'N') IN (
							'P'
							,
            'R'
							)
						)
					AND (DTREFERENCIA >= CAB.DTENTSAI)
					AND (DTREFERENCIA >= '30/12/1900')
				) AS SALDO
		FROM TGFITE ITE
		INNER 
        JOIN
            TGFCAB CAB 
                ON ITE.NUNOTA = CAB.NUNOTA
		INNER 
        JOIN
            TGFPRO PRO 
                ON ITE.CODPROD = PRO.CODPROD
		INNER 
        JOIN
            TGFEMP EMP 
                ON ITE.CODEMP = EMP.CODEMP
		WHERE (CAB.TIPMOV <> 'T')
			AND (EMP.AD_USASYSTOCK = 'S')
			AND (CAB.DTENTSAI > SYSDATE - 1460)
			AND (ITE.RESERVA = 'N')
			AND (ITE.ATUALESTOQUE <> 0)
			AND (ITE.STATUSNOTA = 'L')
		) A
	GROUP 
        BY
            DATA
		,
            IDPRODUTO
		,
            IDFILIAL
		,
            SALDO
	) B
ORDER 
        BY
            DATA DESC