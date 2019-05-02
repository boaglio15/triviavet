create database trivia;
create database trivia_test;
use trivia;

create table users (
		id int auto_increment,
    	nom varchar(255) not null,
    	ape varchar(255) not null,
    	dni varchar(255) not null,
    	tipo set('ADMIN','PLAYER'),
		pass varchar(8),
		nivel int,
    	primary key (id)
);
create table areas (
    	nombreArea varchar(255),
    	primary key(nombreArea)
);
create table questions(
		id int auto_increment,
    	preg text,
    	areaNombre varchar(255),
    	foreign key (areaNombre) references areas(nombreArea), 
    	primary key (id)
);
create table answers(
		id int auto_increment,
    	resp varchar(255),
    	tipo enum('CORRECTA','INCORRECTA'),
    	pregId int,
    	foreign key (pregId) references questions(id),
    	primary key (id,pregId)
);
create table games(
		id int auto_increment,
		userId int,
		fecha date,
		foreign key (userId) references users(id),
		primary key (id)		 
);
create table questiongames(
		questionId int,
		gameId int,
		foreign key (questionId) references questions(id),
		foreign key (gameId) references games(id),
		primary key (questionId,gameId)
);
create table answersgames(
		answerId int,
		gameId int,
		foreign key (answerId) references answers(id),
		foreign key (gameId) references games(id),
		primary key (answerId,gameId)
);
create table stats(
		id int auto_increment,
		userId int,
		cantCorrectas int,
		cantIncorrectas int,
		foreign key (userId) references users(id),
		primary key (id)
);
