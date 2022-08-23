   CREATE OR REPLACE PROCEDURE  "STP_TGFCAB_VALIDA_DESCONTO" (
    P_NUNOTA INT,
P_SUCESSO OUT VARCHAR,
P_MENSAGEM OUT VARCHAR2,
P_CODUSULIB OUT INT) 
AS 
    V_COUNT INT;
    F_PERCDESC FLOAT;
    F_LIBERADOR NUMBER(10);

BEGIN

    BEGIN
        SELECT DISTINCT
            PERCDESC
            ,MIN(
            -- REGRA PARA CLIENTES DE BAIXO GIRO
            CASE WHEN PERFIL = 10403000 THEN
                (CASE   
                    WHEN PERCDESC > 5.0 AND PERCDESC <= 50.0
                        THEN  LDR_SAL
                    WHEN PERCDESC > 50.0
                        THEN GER_OPE
                    END)
				--CLIENTE DE REVENDA
                WHEN PERFIL = 10402000 AND SNK_VER_PROMOCAO(NUNOTA) = 'S' THEN
                (CASE   
                    WHEN PERCDESC > 5.0 AND PERCDESC <= 50.0
                        THEN GER_COM
                    WHEN PERCDESC > 50.0
                        THEN GER_OPE
                    END)
				WHEN PERFIL = 10402000 AND SNK_VER_PROMOCAO(NUNOTA) = 'N'THEN
                (CASE
                    WHEN PERCDESC > 5.0 AND PERCDESC <= 20.0
                        THEN LDR_SAL             
                    WHEN PERCDESC > 50.0
                        THEN GER_OPE
                    END)
				-- CLIENTE PADRÃO
                WHEN PERFIL = 10401000 OR PERFIL = 0 OR PERFIL IS NULL AND SNK_VER_PROMOCAO(NUNOTA) = 'N' THEN
                (CASE   
                    WHEN PERCDESC > 5.0 AND PERCDESC <= 10.0
                        THEN  LDR_SAL
                    WHEN PERCDESC > 10.0 AND PERCDESC <= 50.0
                        THEN GER_COM
                    WHEN PERCDESC > 50.0
                        THEN GER_OPE
                    END)

				WHEN PERFIL = 10401000 OR PERFIL = 0 OR PERFIL IS NULL AND SNK_VER_PROMOCAO(NUNOTA) = 'S' THEN  
                (CASE   
                    WHEN PERCDESC > 5.0 AND PERCDESC <= 50.0
                        THEN  GER_COM
                    WHEN PERCDESC > 50.0
                        THEN GER_OPE
                    END)
                END) AS LIBERADOR
        INTO 
        F_PERCDESC
        ,F_LIBERADOR
        FROM VGF_DESC
        WHERE NUNOTA =P_NUNOTA
        GROUP BY PERCDESC;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        F_PERCDESC := 0;
    END;
   /* 
    SELECT COUNT(*) INTO V_COUNT FROM TSILIB WHERE NUCHAVE = P_NUNOTA;

    IF V_COUNT > 0 THEN
         DELETE FROM TSILIB WHERE NUCHAVE = P_NUNOTA;    
    END IF;*/

    IF F_PERCDESC > 5
    THEN
        P_SUCESSO := 'N';
        P_MENSAGEM := 'Percentual Solicitado= '||F_PERCDESC||' Usuário Liberador: '||F_LIBERADOR;
        P_CODUSULIB := F_LIBERADOR;
    ELSE
        P_SUCESSO := 'S';
    END IF;

END;
/