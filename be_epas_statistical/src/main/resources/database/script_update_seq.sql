PURGE RECYCLEBIN;

DECLARE
l_max       NUMBER;
  l_seq_next  NUMBER;
  l_start     NUMBER;
BEGIN
  -- Duyệt tất cả cột identity trong schema hiện tại
FOR r IN (
    SELECT table_name,
           column_name,
           generation_type,   -- ALWAYS / BY DEFAULT / BY DEFAULT ON NULL
           sequence_name
    FROM   user_tab_identity_cols
  ) LOOP
    -- Lấy MAX(ID) hiện tại trong bảng
    EXECUTE IMMEDIATE
      'SELECT NVL(MAX("'||r.column_name||'"),0) FROM "'||r.table_name||'"'
      INTO l_max;

    -- Lấy nextval hiện tại của sequence identity (để chắc chắn không set START WITH nhỏ hơn)
EXECUTE IMMEDIATE
    'SELECT "'||r.sequence_name||'".nextval FROM dual'
    INTO l_seq_next;

-- Giá trị START WITH mới: lớn hơn cả MAX(ID) và giá trị sequence hiện tại
l_start := GREATEST(l_max + 1, l_seq_next + 1);

    -- Reseed identity bằng ALTER TABLE (không đụng trực tiếp vào sequence nữa)
EXECUTE IMMEDIATE
    'ALTER TABLE "'||r.table_name||'" '||
    'MODIFY ("'||r.column_name||'" GENERATED '||r.generation_type||
    ' AS IDENTITY (START WITH '||l_start||'))';
END LOOP;
END;
/
