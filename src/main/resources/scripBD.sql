create database if not exists trivia;
create database if not exists trivia_test;
use trivia;

create table areas (
        id int auto_increment,	
        nomArea varchar(100), /* solo hay 6 areas */
    	primary key(id)        
);


create table users (
		id int auto_increment,
    	nom varchar(255) not null,
    	ape varchar(255) not null,
    	dni varchar(255) not null,
    	pass varchar(8),
        tipoUser int,
        /*idArea int,*/
    	primary key (id)
        
);


create table questions(
	    id int auto_increment,
    	preg text,
    	areaId int,
        userAdminId int,/*indica el admin que creo la pregunta*/
    	foreign key (areaId) references areas(id), 
    	foreign key (userAdminId) references users(id),
        primary key (id)
);
create table answers(
		id int auto_increment,
    	resp text,
    	tipoAnswer int, 
    	pregId int,
    	foreign key (pregId) references questions(id),
    	primary key (id)
);
create table games(
		id int auto_increment,
		userId int,
		foreign key (userId) references users(id),
		primary key (id)		 
);
create table questions_games(
		id int auto_increment,
        questionId int,
		gameId int,
        estado int, /* 0 : hecha e incorrecta, 1: hecha correcta*/
		/*foreign key (questionId) references questions(id),
		foreign key (gameId) references games(id),
		primary key (questionId,gameId) */
        primary key (id)
);

create table answers_games(
		id int auto_increment,
        answerId int,
		gameId int,
		/*foreign key (answerId) references answers(id),
		foreign key (gameId) references games(id),
		primary key (answerId,gameId)*/
        primary key (id)
);

create table users_areas(
		id int auto_increment,
        userId int,
        areaId int,
        completada int,
		nivel int,
        /*foreign key (userId) references users(id),
        foreign key (areaId) references areas(id),
        primary key (userId,areaId)*/
        primary key (id)
);

create table stats(
		id int auto_increment,
		userId int,
		cantCorrectas int,
		cantIncorrectas int,
		foreign key (userId) references users(id),
		primary key (id)
);
