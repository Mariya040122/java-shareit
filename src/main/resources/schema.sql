DROP TABLE IF EXISTS PUBLIC.BOOKINGS;
CREATE TABLE IF NOT EXISTS PUBLIC.BOOKINGS (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL, END_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL, ITEM_ID INT8 NOT NULL, BOOKER_ID INT8 NOT NULL, STATUS VARCHAR NOT NULL, CONSTRAINT PK_BOOKING PRIMARY KEY (ID));
ALTER TABLE IF EXISTS PUBLIC.BOOKINGS DROP CONSTRAINT IF EXISTS BOOKINGS_FK;
ALTER TABLE IF EXISTS PUBLIC.BOOKINGS DROP CONSTRAINT IF EXISTS BOOKINGS_FK_1;
CREATE INDEX IF NOT EXISTS BOOKINGS_ID_IDX ON PUBLIC.BOOKINGS (ID);

DROP TABLE IF EXISTS PUBLIC.REQUESTS;
CREATE TABLE IF NOT EXISTS PUBLIC.REQUESTS (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, DESCRIPTION VARCHAR NOT NULL, REQUESTOR_ID BIGINT NOT NULL, CREATED TIMESTAMP NOT NULL, CONSTRAINT PK_REQUEST PRIMARY KEY (ID));

DROP TABLE IF EXISTS PUBLIC."COMMENTS";
CREATE TABLE IF NOT EXISTS PUBLIC."COMMENTS" (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "TEXT" VARCHAR NOT NULL, ITEM_ID BIGINT NOT NULL, AUTHOR_ID BIGINT NOT NULL, CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL, CONSTRAINT PK_COMMENT PRIMARY KEY (ID));
ALTER TABLE PUBLIC."COMMENTS" DROP CONSTRAINT IF EXISTS COMMENTS_FK;
ALTER TABLE PUBLIC."COMMENTS" DROP CONSTRAINT IF EXISTS COMMENTS_FK_1;

DROP TABLE IF EXISTS PUBLIC.ITEMS;
CREATE TABLE IF NOT EXISTS PUBLIC.ITEMS (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "NAME" VARCHAR NOT NULL, DESCRIPTION VARCHAR NOT NULL, AVAILABLE BOOL NOT NULL, "OWNER" BIGINT NOT NULL, "REQUEST_ID" BIGINT NULL_TO_DEFAULT, CONSTRAINT PK_ITEM PRIMARY KEY (ID));
CREATE INDEX IF NOT EXISTS ITEM_ID_IDX ON PUBLIC.ITEMS (ID);
ALTER TABLE PUBLIC.ITEMS DROP CONSTRAINT IF EXISTS ITEMS_FK;

DROP TABLE IF EXISTS PUBLIC."USERS";
CREATE TABLE IF NOT EXISTS PUBLIC."USERS" (ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "NAME" VARCHAR NOT NULL, EMAIL VARCHAR NOT NULL, CONSTRAINT USERS_UN UNIQUE (EMAIL), CONSTRAINT PK_USER PRIMARY KEY (ID));
CREATE INDEX IF NOT EXISTS USER_ID_IDX ON PUBLIC."USERS" USING BTREE (ID);

ALTER TABLE PUBLIC.BOOKINGS ADD CONSTRAINT BOOKINGS_FK FOREIGN KEY (BOOKER_ID) REFERENCES PUBLIC."USERS"(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE PUBLIC.BOOKINGS ADD CONSTRAINT BOOKINGS_FK_1 FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PUBLIC."COMMENTS" ADD CONSTRAINT COMMENTS_FK FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE PUBLIC."COMMENTS" ADD CONSTRAINT COMMENTS_FK_1 FOREIGN KEY (AUTHOR_ID) REFERENCES PUBLIC."USERS"(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PUBLIC.ITEMS ADD CONSTRAINT ITEMS_FK FOREIGN KEY ("OWNER") REFERENCES PUBLIC."USERS"(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE PUBLIC.REQUESTS ADD CONSTRAINT REQUESTS_FK FOREIGN KEY (REQUESTOR_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT;