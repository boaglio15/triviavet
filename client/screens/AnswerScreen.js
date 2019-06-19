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
    this.state = {};
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

        <Text style={styles.espacio}> {"\n\n\n\n\n"}</Text>

        <View style={styles.getStartedContainer}>
          <Button
            onPress={this.exitGame} style={styles.logout}
            title="--- exit ---"
            color="#ff0000"
          />
        </View>

      </View>
    );
  }

  //pide un pregunta a la app y parametros de juego
  nextQuestions = async () => {
    const { navigation } = this.props;
    const area = navigation.getParam('areaId', 'NO-ID');
    axios.get(API_HOST + "selectQuestionAnswer/" + area, {
      headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
    })
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        const areaComplet = response.data.areaComplet;
        const nivel = response.data.nivel;
        if (areaComplet == 1) { //caso area jugada y completada  (falta contemplar cuando mo hay mas preg)     
          return (
            this.props.navigation.navigate('AreaCompletada', { 'areaId': area, 'areaComplet': areaComplet }))
        } else { //caso area en juego
          const questId = response.data.id;
          const quest = response.data.preg;
          const answ1 = response.data.resp1;
          const answ2 = response.data.resp2;
          const answ3 = response.data.resp3;
          const answ4 = response.data.resp4;
          const type1 = response.data.tipo1;
          const nivel = response.data.nivel;
          this.props.navigation.navigate('QuestionsAnswers',
            {
              'quest': quest, 'questId': questId, 'areaId': area, 'answ1': answ1,
              'answ2': answ2, 'answ3': answ3, 'answ4': answ4, 'type1': type1, 'nivel': nivel, 'areaComplet': areaComplet
            }); //envia parametros a la sig vista
        }
      })
      .catch((error) => {
        if (error.toString().match(/401/)) {
          alert("No hay preguntas");
          return;
        }
        alert("Networking Error question");
      })
  }

  //det si la respuesta seleccionada es corecta o no
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
    const { navigation } = this.props;
    const area = navigation.getParam('areaId', 'NO-ID');
    const areaComplet = navigation.getParam('areaComplet', 'NO-ID');
    const nivel = navigation.getParam('nivel', 'NO-ID');
    //const nivel1 = JSON.stringify(nivel);
    //const area1 = JSON.stringify(area);
    //const comp = JSON.stringify(areaComplet) 
    console.log(nivel);
    console.log(areaComplet);
    axios.post(API_HOST + "exit", {
      areaId: area,
      completada: areaComplet,
      nivel: nivel,
    },
      {
        headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
      }
    )
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        AsyncStorage.setItem('userToken', response.config.headers.Authorization);
      })
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
  },

  logout: {
    fontSize: 14,
    color: '#2e78b7',
    textAlign: 'right',
  },

  getStartedContainer: {
    alignItems: 'center',
    marginHorizontal: 50,
  },

})