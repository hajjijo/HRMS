# --- !Ups

create table "dateTable"("id" BIGSERIAL NOT NULL PRIMARY KEY, "date" TIMESTAMP);

# --- !Downs

drop table "dateTable";
