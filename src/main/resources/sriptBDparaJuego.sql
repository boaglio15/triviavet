use trivia;

INSERT INTO areas (nomArea) VALUES
	("A"),
    ("B"),
	("C")
    
INSERT INTO users (nom, ape, dni, pass, tipoUser) VALUES 
	("Agustin","Boaglio","admin","admin",0),
    ("Fernando", "Bentolila", "45", "45",1),
    ("diego","maradona","4545","123",0)
    
INSERT INTO games (userId) VALUES
	(1),
    (2),
    (3)
    
INSERT INTO questions (preg,areaId,userAdminId) VALUES
	("¿Hola todo bien?",1,1),
    ("¿En donde estabas?",2,1),
    ("¿Fuiste a futbol?",1,1),
    ("¿Venis a comer?",1,1),
    ("quien gano?",1,1),
    ("es de dia?",1,1)
    
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
    ("donde",0,4),
    ("yo",1,5),
    ("tu",0,5),
    ("ellos",0,5),
    ("nosotros",0,5),
    ("afirmativo",1,6),
    ("no lo se",0,6),
    ("de noche",0,6),
    ("ni",0,6)
    
INSERT INTO questions_games (questionId,gameId,estado) VALUES
	(1,1,1), /*la preg 1 fue contestada bien por el jugador 1*/
	(1,2,1), /*la preg 1 fue contestda bien por el jugador 2*/
    (1,3,0),
    (2,2,0)
    
INSERT INTO answers_games (answerId,gameId) VALUES /*PUEDE SER QUE ESTA TABLA ESTE DE MAS XQ LAS RESPUESTAS QUE DIO UN JUGADOR EN UN JUEGO PUDEN OBTENERSE POR questions_games HABLARLO !! */
(1,1),/*las respuestas para la perg 1 son hechas al jugador 1*/
(2,1),
(3,1),
(4,1),
(1,2),/*las respuestas para la preg 1 son hechas al jugador 2*/
(2,2),
(3,2),
(4,2),
(1,3),/*las respuestas para la preg 1 son hechas al jugador 3*/
(2,3),
(4,3),
(5,3)	

INSERT INTO users_areas (userId,areaId,completada,nivel) VALUES
(1,1,0,1),
(2,2,0,1),
(3,1,0,1)
