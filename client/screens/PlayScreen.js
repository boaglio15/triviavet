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

export default class PlayScreen extends React.Component {
  static navigationOptions = {
    title: 'play',
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}> Selecciona un area para jugar </Text>


        <View style={styles.button}>
          <Button title="Cucarachas" onPress={this.handleQuestionAnswer.bind(this, '1')} />
        </View>

        <View style={styles.button}>
          <Button title="Bichos Bolitas" onPress={this.handleQuestionAnswer.bind(this, '2')} />
        </View>

        <View style={styles.button}>
          <Button title="Abejorros" onPress={this.handleQuestionAnswer.bind(this, '3')} />
        </View>
        <View style={styles.button}>
          {this.stateArea}
        </View>


        <View style={styles.button}>
          <Button
            onPress={() => this.props.navigation.goBack()}
            title="volver"
            color="#ff0000"
          /> 
        </View>

      </View>
    );
  }



  handleQuestionAnswer = async (area) => {
    axios.get(API_HOST + "selectQuestionAnswerInit/" + area, {
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
          {'quest': quest, 'questId': questId, 'areaId': area, 'answ1': answ1,
            'answ2':answ2,  'answ3':answ3, 'answ4':answ4, 'type1':type1
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
