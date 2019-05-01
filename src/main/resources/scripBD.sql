create database trivia;
create database trivia_test;
use trivia;

create table USERS (
		id int auto_increment,
    	nom varchar(255) not null,
    	ape varchar(255) not null,
    	dni varchar(255) not null,
    	tipo set('ADMIN','PLAYER'),
		pass varchar(8),
		nivel int,
    	primary key (id)
);
create table AREAS (
    	nombreArea varchar(255),
    	primary key(nombreArea)
);
create table QUESTIONS(
		id int auto_increment,
    	preg text,
    	areaNombre varchar(255),
    	foreign key (areaNombre) references AREAS(nombreArea), 
    	primary key (id)
);
create table ANSWERS(
		id int auto_increment,
    	resp varchar(255),
    	tipo enum('CORRECTA','INCORRECTA'),
    	pregId int,
    	foreign key (pregId) references QUESTIONS(id),
    	primary key (id,pregId)
);
create table GAMES(
		id int auto_increment,
		userId int,
		fecha date,
		foreign key (userId) references USERS(id),
		primary key (id)		 
);
create table QUESTIONS_GAMES(
		questionId int,
		gameId int,
		foreign key (questionId) references QUESTIONS(id),
		foreign key (gameId) references GAMES(id),
		primary key (questionId,gameId)
);
create table ANSWERS_GAMES(
		answerId int,
		gameId int,
		foreign key (answerId) references ANSWERS(id),
		foreign key (gameId) references GAMES(id),
		primary key (answerId,gameId)
);
create table STATS(
		id int auto_increment,
		userId int,
		cantCorrectas int,
		cantIncorrectas int,
		foreign key (userId) references USERS(id),
		primary key (id)
);