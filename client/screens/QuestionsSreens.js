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

export default class QuestionsSreens extends React.Component {
  static navigationOptions = {
    title: 'Tu pregunta es:',
  };
  constructor(props) {
    super(props);
    this.state = {};
  }
  render() {
    const { navigation } = this.props;
    const question = navigation.getParam('quest', 'NO-ID');
    const respuesta1 = navigation.getParam('answ1', 'NO-ID');
    const respuesta2 = navigation.getParam('answ2', 'NO-ID');
    const respuesta3 = navigation.getParam('answ3', 'NO-ID');
    const respuesta4 = navigation.getParam('answ4', 'NO-ID');
    //FALTA GENERAR MOSTRAR LAS RESPUESTAS EN DISTINTOS BOTONES PORQUE
    //DE ESTA FORMA SIEMPRE LA RESPUESTA CORRECTA ESTA EN EL MISMO BOTON
    return (
      <View style={styles.container}>

        <View>
          <Text style={styles.welcome}> {question}</Text>
        </View>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, '1')}
            title={respuesta1}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, '0')}
            title={respuesta2}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, '0')}
            title={respuesta3}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, '0')}
            title={respuesta4}

          />
        </View>
      </View>
    );
  }
  //toma la respuesta seleccionada por el jugador y la envia la app
  handleAnswer = async (tipo) => {
    const { navigation } = this.props;
    /*axios.post(API_HOST + "updateTypeQuest/" + tipo , {
      //tipoResp: tipo,
    },
      {
        headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
      }
    )
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        // Handle the JWT response here 
        AsyncStorage.setItem('userToken', response.config.headers.Authorization);//.config.headers.Authorization);
        const area = navigation.getParam('areaId', 'NO-ID');
        this.props.navigation.navigate('Answer', { 'areaId': area, 'tipo': tipo });
      })*/
    const area = navigation.getParam('areaId', 'NO-ID');
    this.props.navigation.navigate('Answer', { 'areaId': area, 'tipo': tipo });
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
