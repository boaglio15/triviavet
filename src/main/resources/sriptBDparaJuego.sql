use trivia;

INSERT INTO areas (nomArea) VALUES
	("A"),
    ("B")
	
    
INSERT INTO users (nom, ape, dni, pass, tipoUser) VALUES 
	("Agustin","Boaglio","343321","abc123",0),
    ("Fernando", "Bentolila", "4592313", "tre457",1)
    
INSERT INTO games (userId,fecha) VALUES
	(1,'2019-06-01'),
    (2,'2019-01-01')
    
INSERT INTO questions (preg,areaId,userAdminId) VALUES
	("¿Hola todo bien?",1,1),
    ("¿En donde estabas?",2,1),
    ("¿Fuiste a futbol?",1,1),
    ("¿Venis a comer?",1,1)
    
INSERT INTO answers (resp,tipoAnswer,pregId) VALUES /* las tres opciones incorrectas siempre serian las mismas, FIJARSE ESOOOO*/
	("Si",1,1),
	("No",0,1),
	("Que",0,1),
	("Por",0,1),
    ("Universidad",1,2),
	("Futbol",0,2), 
	("Casa",0,2),
	("Parque",0,2),
    ("si al futbol",1,3),
    ("no al barco",0,3),
    ("al parque",0,3),
    ("cocinando",0,3),
    ("como",1,4),
    ("no como",0,4),
    ("dieta",0,4),
    ("donde",0,4)
    
INSERT INTO questions_games (questionId,gameId,estado) VALUES
	(1,1,0), /*la preg 1 fue contestada mal por el jugador 1*/
	(1,2,1) /*la preg 1 fue contestda bien por el jugador 2*/
    
INSERT INTO answers_games (answerId,gameId) VALUES /*PUEDE SER QUE ESTA TABLA ESTE DE MAS XQ LAS RESPUESTAS QUE DIO UN JUGADOR EN UN JUEGO PUDEN OBTENERSE POR questions_games HABLARLO !! */
(1,1),/*las respuestas para la perg 1 son hechas al jugador 1*/
(2,1),
(3,1),
(4,1),
(1,2),/*las respuestas para la preg 1 son hechas al jugador 2*/
(2,2),
(3,2),
(4,2)	

INSERT INTO users_areas (userId,areaId,completada,nivel) VALUES
(1,1,0,0),
(2,2,0,0)
