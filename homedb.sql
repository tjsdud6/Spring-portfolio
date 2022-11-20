show databases;
use homedb;
show tables;

--  db 생성
create database homedb;

-- shop db 권한 주기
grant all privileges on homedb.* to root@localhost with grant option;

select * from member;
select * from notice;
select * from customer;
select * from item order by reg_time desc;
select * from item_img order by reg_time desc;
select * from orders order by reg_time desc;
select * from order_item order by reg_time desc;

update member set role = 'ADMIN';