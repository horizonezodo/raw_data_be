CREATE OR REPLACE PROCEDURE GENERATE_NEXT_SEQUENCE (
    p_table_name     IN  VARCHAR2,
    p_column_name    IN  VARCHAR2,
    p_prefix         IN  VARCHAR2,
    p_num_to_gen     IN  NUMBER DEFAULT 1,
    p_padded_length  IN  NUMBER DEFAULT 5,
    p_date_text      IN  VARCHAR2,
    p_separator      IN  VARCHAR2 DEFAULT '.',
    p_sequence_value OUT VARCHAR2
)
AS
    v_stt       NUMBER;
    v_found     BOOLEAN := FALSE;
    v_sep       VARCHAR2(10);
    v_date_key  VARCHAR2(20); -- Biến mới để lưu vào DB (Không bao giờ NULL)
BEGIN
    -- 1. Xử lý các biến đầu vào
    v_sep := NVL(p_separator, '');

    -- QUAN TRỌNG: Nếu không có ngày (Customer Code), dùng chuỗi 'NO_DATE' để lưu DB
    -- để tránh lỗi ORA-01400 (PK cannot be NULL)
    v_date_key := NVL(p_date_text, 'NO_DATE');

    -- 2. Dọn dẹp sequence cũ (Chỉ chạy khi đúng là có ngày tháng định dạng YYMMDD)
    IF p_prefix IS NOT NULL AND p_date_text IS NOT NULL THEN
BEGIN
DELETE FROM EPAS_CODE_SEQUENCE
WHERE LAST_DATE IS NOT NULL
  AND LENGTH(LAST_DATE) = 6 -- Chỉ xóa các record dạng ngày YYMMDD
  AND TO_DATE(LAST_DATE, 'RRMMDD') < TRUNC(SYSDATE) - 2
  AND UPPER(TABLE_NAME) = UPPER(p_table_name)
  AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
  AND UPPER(PREFIX) = UPPER(p_prefix);
EXCEPTION
            WHEN OTHERS THEN NULL;
END;
END IF;

    -- 3. Tìm số thứ tự hiện tại (Sử dụng v_date_key thay vì p_date_text)
BEGIN
SELECT last_stt INTO v_stt
FROM (
         SELECT last_stt
         FROM EPAS_CODE_SEQUENCE
         WHERE UPPER(TABLE_NAME) = UPPER(p_table_name)
           AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
           AND (p_prefix IS NULL OR UPPER(PREFIX) = UPPER(p_prefix))
           AND LAST_DATE = v_date_key -- So sánh với key đã xử lý
         ORDER BY LAST_STT DESC
     )
WHERE ROWNUM = 1;

v_found := TRUE;
EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_stt := 0;
END;

    -- 4. Tăng số thứ tự
    v_stt := v_stt + 1;

    -- 5. Sinh chuỗi kết quả (Vẫn dùng p_date_text gốc để hiển thị đúng yêu cầu)
    IF p_prefix IS NOT NULL AND p_date_text IS NOT NULL THEN
        -- Case: CRM.250312.00001
        p_sequence_value := p_prefix || v_sep || p_date_text || v_sep || LPAD(v_stt, p_padded_length, '0');

    ELSIF p_prefix IS NOT NULL THEN
        -- Case Customer: HN01000001 (p_date_text là NULL nên không nối vào chuỗi)
        p_sequence_value := p_prefix || v_sep || LPAD(v_stt, p_padded_length, '0');

    ELSIF p_date_text IS NOT NULL THEN
        p_sequence_value := p_date_text || v_sep || LPAD(v_stt, p_padded_length, '0');
ELSE
        p_sequence_value := LPAD(v_stt, p_padded_length, '0');
END IF;

    -- 6. Cập nhật hoặc Thêm mới vào DB (Dùng v_date_key)
    IF v_found THEN
UPDATE EPAS_CODE_SEQUENCE
SET LAST_STT = v_stt + p_num_to_gen - 1
WHERE UPPER(TABLE_NAME) = UPPER(p_table_name)
  AND UPPER(COLUMN_NAME) = UPPER(p_column_name)
  AND (p_prefix IS NULL OR UPPER(PREFIX) = UPPER(p_prefix))
  AND LAST_DATE = v_date_key; -- Update theo key 'NO_DATE' hoặc '250312'
ELSE
        INSERT INTO EPAS_CODE_SEQUENCE (TABLE_NAME, COLUMN_NAME, PREFIX, LAST_DATE, LAST_STT)
        VALUES (
            p_table_name,
            p_column_name,
            p_prefix,
            v_date_key, -- Insert 'NO_DATE' thay vì NULL
            v_stt + p_num_to_gen - 1
        );
END IF;

COMMIT;
END;