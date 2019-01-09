/* Validation on name field in member table to allow names with the length of 2 to 100 and should
always start with an alphabet. Please do not add validations on all other fields. */
CREATE TRIGGER trigger_member_name_check
  BEFORE INSERT
  ON member
  FOR EACH ROW
BEGIN
  IF (NEW.name REGEXP '^([a-zA-Z]+.+){2,100}$') = 0 THEN
    /* See https://dev.mysql.com/doc/refman/5.6/en/signal.html */
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Name has to have the length of 2 to 100 and should always start with an alphabet';
  END IF;
END;