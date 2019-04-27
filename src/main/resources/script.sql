create database trivia2;
use trivia2;

create table USERS (
	userId INT auto_increment,
    	nom varchar(255) not null,
    	ape varchar(255) not null,
    	dni varchar(255) not null,
    	tipo set('ADMIN','PLAYER'),
    	primary key (userId)
);
create table CATEGORIES (
    	nombreCat varchar(255),
    	primary key(nombreCat)
);
create table QUESTIONS(
	questionId int auto_increment,
    	descripcion text,
    	catNombre varchar(255),
    	foreign key (catNombre) references CATEGORIES(nombreCat), 
    	primary key (questionId,catNombre)
);
create table ANSWERS(
	answerId int auto_increment,
    	resp varchar(255),
    	tipo enum('CORRECTA','INCORRECTA'),
    	pregId int,
    	foreign key (pregId) references QUESTIONS(questionId),
    	primary key (answerId,pregId)
);
