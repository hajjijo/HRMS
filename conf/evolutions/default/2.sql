# --- !Ups

create table "roll_call"("id" BIGSERIAL NOT NULL PRIMARY KEY, "employ_id" BIGINT, "in_date" TIMESTAMP NOT NULL, "out_date" TIMESTAMP);
alter table "roll_call" add constraint "fk_RollCall_EmployId" foreign key("employ_id") references "employs"("id") on update NO ACTION on delete CASCADE;

# --- !Downs

alter table "roll_call" drop constraint "fk_RollCall_EmployId";
drop table "roll_call";
