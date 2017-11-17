# --- !Ups

create table "employs"("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(32) NOT NULL,"family" VARCHAR(32) NOT NULL, "nationalId" VARCHAR(10), "zipCode" VARCHAR(10), "phone" VARCHAR(15), "address" VARCHAR(80), "employStatus" VARCHAR(32), "salary" BIGINT);

# --- !Downs

drop table "employs";
