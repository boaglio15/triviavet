import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import {
  AsyncStorage,
  View,
  Text,
  Button,
  StyleSheet,
} from 'react-native';
import axios from 'axios';

export default class AnswerScreen extends React.Component {
  static navigationOptions = {
    title: 'respuesta:',
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
  //CADA VEZ QUE SE CARGA UN RENDER SE CARGA EL ESTADO
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

  correctaOIncorrecta(tipoAnsw) {
    if (tipoAnsw == 1) {
      return (
        <View>
          <Text>Respuesta Correcta</Text>
        </View>
      )
    } else {
      return (
        <View>
          <Text>Respuesta Incorrecta</Text>
        </View>
      )
    }
  };

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
    fontSize: 20,
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
  }
})