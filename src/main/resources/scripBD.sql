create database if not exists trivia;
create database if not exists trivia_test;
use trivia;

create table users (
		id int auto_increment,
    	nom varchar(255) not null,
    	ape varchar(255) not null,
    	dni varchar(255) not null,
    	tipoUser int,
		pass varchar(8),
		nivel int,
    	primary key (id)
);
create table areas (
        id int auto_increment,	
        nombreArea varchar(255),
        userId int,    
    	primary key(id),
        foreign key (userId) references users(id)
);
create table questions(
		id int auto_increment,
    	preg text,
    	areaId int,
        userId int,
        correcta int,
    	foreign key (areaId) references areas(id), 
    	foreign key (userId) references users(id),
        primary key (id)
);
create table answers(
		id int auto_increment,
    	resp varchar(255),
    	tipoAnswer int, 
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
create table questions_games(
		questionId int,
		gameId int,
		foreign key (questionId) references questions(id),
		foreign key (gameId) references games(id),
		primary key (questionId,gameId)
);

create table answers_games(
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
