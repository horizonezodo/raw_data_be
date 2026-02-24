CREATE OR REPLACE
PROCEDURE "GENERATE_NEXT_SEQUENCE" (
    p_table_name     IN  VARCHAR2,
    p_column_name    IN  VARCHAR2,
    p_prefix         IN  VARCHAR2,
    p_num_to_gen     IN  NUMBER DEFAULT 1,
    p_padded_length  IN  NUMBER DEFAULT 5,
    p_date_text      IN  VARCHAR2,
    p_sequence_value OUT VARCHAR2
)
AS
    v_date VARCHAR2(8);
    v_stt  NUMBER;
    v_found BOOLEAN := FALSE;
BEGIN
	-- Chỉ xóa nếu truyền cả prefix và date_text
IF p_prefix IS NOT NULL AND p_date_text IS NOT NULL THEN
    DELETE FROM EPAS_CODE_SEQUENCE
    WHERE LAST_DATE IS NOT NULL
      AND TO_DATE(LAST_DATE, 'YYYYMMDD') < TRUNC(SYSDATE) - 2
      AND UPPER(TABLE_NAME) = UPPER(p_table_name)
      AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
      AND UPPER(PREFIX) = UPPER(p_prefix);
END IF;

    BEGIN
        SELECT last_date, last_stt INTO v_date, v_stt
        FROM (
            SELECT last_date, last_stt
            FROM EPAS_CODE_SEQUENCE
            WHERE UPPER(TABLE_NAME) = UPPER(p_table_name)
              AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
              AND (p_prefix IS NULL OR UPPER(PREFIX) = UPPER(p_prefix))
              AND (p_date_text IS NULL OR LAST_DATE = p_date_text)
            ORDER BY LAST_DATE DESC, LAST_STT DESC
        )
        WHERE ROWNUM = 1;

        v_found := TRUE;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_date := NULL;
            v_stt := 0; -- nếu không có bản ghi thì bắt đầu từ 0, tí nữa +1
    END;

    -- Luôn tăng lên 1, không reset về 1 nếu tìm được bản ghi trước đó
    v_stt := v_stt + 1;

    -- Sinh chuỗi sequence
    IF p_prefix IS NOT NULL AND p_date_text IS NOT NULL THEN
        p_sequence_value := p_prefix || '.' || p_date_text || '.' || LPAD(v_stt, p_padded_length, '0');
    ELSIF p_prefix IS NOT NULL THEN
        p_sequence_value := p_prefix || '.' || LPAD(v_stt, p_padded_length, '0');
    ELSIF p_date_text IS NOT NULL THEN
        p_sequence_value := p_date_text || '.' || LPAD(v_stt, p_padded_length, '0');
    ELSE
        p_sequence_value := LPAD(v_stt, p_padded_length, '0');
    END IF;

    -- Nếu đã có bản ghi thì update
    IF v_found THEN
        UPDATE EPAS_CODE_SEQUENCE
        SET LAST_STT = v_stt + p_num_to_gen - 1
        WHERE UPPER(TABLE_NAME) = UPPER(p_table_name)
          AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
          AND (p_prefix IS NULL OR UPPER(PREFIX) = UPPER(p_prefix))
          AND (p_date_text IS NULL OR LAST_DATE = p_date_text);
    ELSE
        -- Không có bản ghi cũ → insert mới
        INSERT INTO EPAS_CODE_SEQUENCE (TABLE_NAME, COLUMN_NAME, PREFIX, LAST_DATE, LAST_STT)
        VALUES (
            p_table_name,
            p_column_name,
            p_prefix,
            p_date_text,
            v_stt + p_num_to_gen - 1
        );
    END IF;

    COMMIT;
END;