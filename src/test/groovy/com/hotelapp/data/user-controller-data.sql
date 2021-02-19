DELETE FROM RESERVATIONS;
DELETE FROM USER_ROLES;
DELETE FROM USERS;
DELETE FROM ROLES;

INSERT INTO ROLES(ID,ROLE_NAME)  values(1,'ROLE_USER');
INSERT INTO ROLES(ID,ROLE_NAME)  values(2,'ROLE_ADMIN');
INSERT INTO ROLES(ID,ROLE_NAME)  values(3,'ROLE_HOTEL');
INSERT INTO USERS(ID,email,first_name,last_name,is_active,password,phone_number,username)
            values(1,'adrianwilczynski4@gmail.com','Adrian','Wilczynski','true','$2y$12$PpOAPDefnGAEHOspZiFnxuNcePZ90fYgACVxXZOABBxsUbbLGF15W','663111222','admin');
INSERT INTO USER_ROLES(user_id,role_id) values(1,2);
INSERT INTO USER_ROLES(user_id,role_id) values(1,3);

