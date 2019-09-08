import { createSwitchNavigator, createStackNavigator, createAppContainer } from 'react-navigation';

import HomeScreen from '../screens/HomeScreen';
import PlayScreen from '../screens/PlayScreen';
 
import QuestionsSreens from '../screens/QuestionsSreens';
import SignInScreen from '../screens/SignInScreen';
import AuthLoadingScreen from '../screens/AuthLoadingScreen';
import AnswerScreen from '../screens/AnswerScreen';
import Registration from '../screens/Registration';
import CompletArea from '../screens/CompletArea';
import EmptyArea from '../screens/EmptyArea';
import LostArea from '../screens/LostArea';
import Stat from '../screens/Stat';
import StatAreaComplet from '../screens/StatAreaComplet';
import StatAreaInPlay from '../screens/StatAreaInPlay';

//createStackNavigator: crea un componente que se coloca en la pila de navegacion que se usa para
//recorrer distintas pantallas en la app
const AppStack = createStackNavigator({ Home: HomeScreen, 
  Play: PlayScreen,  
  QuestionsAnswers: QuestionsSreens,
  Answer: AnswerScreen,
  Registro: Registration, 
  AreaCompletada: CompletArea, 
  AreaSinPreguntas: EmptyArea,
  AreaPerdida: LostArea,
  Estadistica: Stat,
  EstadisticaAComplet: StatAreaComplet,
  EstadisticaEnJuego: StatAreaInPlay});
  
const AuthStack = createStackNavigator({ SignIn: SignInScreen }); //contenedor para inicio por defecto
                                                                  //carga el logueo
export default createAppContainer(createSwitchNavigator(
  {
    AuthLoading: AuthLoadingScreen, //genera contenedores para navegar
    App: AppStack, // es una pila donde se carga las distintas pantallas de la app
    Auth: AuthStack, // es una pila donde esta lo que se carga por defecto al inicial la app y permite loguearse
  },
  {
    initialRouteName: 'AuthLoading',
  }
));
