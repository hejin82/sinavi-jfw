DROP TABLE IF EXISTS TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST;

CREATE TABLE IF NOT EXISTS TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST (
  ID INTEGER PRIMARY KEY,
  NUM19 DECIMAL(19,0),
  NUM25 DECIMAL(25,0),
  NUM30 DECIMAL(30,0)
);

DROP SEQUENCE IF EXISTS TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST_SEQUENCE;

CREATE SEQUENCE IF NOT EXISTS TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST_SEQUENCE START WITH 1 INCREMENT BY 1;

INSERT INTO TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST VALUES (nextval('TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST_SEQUENCE'), 1234567890123456789, 1234567890123456789012345, 123456789012345678901234567890);
INSERT INTO TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST VALUES (nextval('TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST_SEQUENCE'), null, null, null);
INSERT INTO TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST VALUES (nextval('TYPE_HANDLER_BIGINTEGER_INTEGRATION_TEST_SEQUENCE'), 9999999999999999999, 9999999999999999999999999, 999999999999999999999999999999);