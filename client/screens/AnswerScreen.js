import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import {
  AsyncStorage,
  View,
  Text,
  Button,
  StyleSheet,
  Image,
} from 'react-native';
import axios from 'axios';

export default class AnswerScreen extends React.Component {
  static navigationOptions = {
    title: 'Respuesta:',
  };
  constructor(props) {
    super(props);
    this.state = {
      questState: []
    };
  }

  render() {
    const { navigation } = this.props;
    const tipoAnsw = navigation.getParam('tipo', 'NO-ID');
    return (
      <View style={styles.container}>

        <View>
          {this.correctaOIncorrecta(tipoAnsw)}
        </View>

        <View style={styles.button}>
          <Button
            onPress={() => this.nextQuestions()}
            title="Siguiente pregunta"
          />
        </View>

        <Text style={styles.espacio}> {"\n"}</Text>

        <Button
          onPress={() => this.exitGame()}
          title="Exit"
          color="#ff4500"
          accessibilityLabel="Learn more about this button"
        />


      </View>
    );
  }

  nextQuestions = async () => {
    const { navigation } = this.props;
    const area = navigation.getParam('areaId', 'NO-ID');
    axios.get(API_HOST + "selectQuestionAnswer/" + area, {
      headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
    })
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        const quest = response.data.preg;
        const questId = response.data.id;
        const answ1 = response.data.resp1;
        const answ2 = response.data.resp2;
        const answ3 = response.data.resp3;
        const answ4 = response.data.resp4;
        const type1 = response.data.tipo1;
        this.props.navigation.navigate('QuestionsAnswers',
          {
            'quest': quest, 'questId': questId, 'areaId': area, 'answ1': answ1,
            'answ2': answ2, 'answ3': answ3, 'answ4': answ4, 'type1': type1
          });
      })
      .catch((error) => {
        if (error.toString().match(/401/)) {
          alert("No hay preguntas");
          return;
        }
        alert("Networking Error question");
      })
  }
  
  //muestra al usuario si la respuesta seleccionada es la correcta o no
  correctaOIncorrecta(tipoAnsw) {
    if (tipoAnsw == 1) {
      return (
        <View>
          <View style={styles.welcomeContainer}>
            <Image
              source={
                __DEV__
                  ? require('../assets/images/caballo2.png')
                  : require('../assets/images/robot-prod.png')
              }
              style={styles.welcomeImage}
            />
          </View>
          <Text style={styles.welcome}>Correcto !</Text>
        </View>
      )
    } else {
      return (
        <View>
          <View style={styles.welcomeContainer}>
            <Image
              source={
                __DEV__
                  ? require('../assets/images/caballo.png')
                  : require('../assets/images/robot-prod.png')
              }
              style={styles.welcomeImage}
            />
          </View>
          <Text style={styles.welcome}>Incorrecto !</Text>
        </View>
      )
    }
  };

  //permite volver al home y habilita a la app a salvar las preguntas hechas en la partida en la base de datos
  exitGame = async () => {
    /*axios.post(API_HOST + "exit", {

    },
      {
        headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
      }
    )
    .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        // Handle the JWT response here
        AsyncStorage.setItem('userToken', response.config.headers.Authorization);//.config.headers.Authorization);
        this.props.navigation.navigate('Home');
      })*/
    //AsyncStorage.setItem('userToken', response.config.headers.Authorization);//.config.headers.Authorization);
    this.props.navigation.navigate('Home');
  }
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 35,
    textAlign: 'center',
    margin: 10,
  },
  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#4218F8'
  },
  button: {
    marginTop: 20,
  },

  welcomeContainer: {
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 20,
  },
  welcomeImage: {
    width: 150,
    height: 150,
    resizeMode: 'contain',
    marginTop: 3,
    marginLeft: -10,
  }
})