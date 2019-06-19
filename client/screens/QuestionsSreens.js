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
    //recibir cant preg hacer
    const { navigation } = this.props;
    const question = navigation.getParam('quest', 'NO-ID');
    const respuesta1 = navigation.getParam('answ1', 'NO-ID');
    const respuesta2 = navigation.getParam('answ2', 'NO-ID');
    const respuesta3 = navigation.getParam('answ3', 'NO-ID');
    const respuesta4 = navigation.getParam('answ4', 'NO-ID');
    const nivel = navigation.getParam('nivel', 'NO-ID');
    //FALTA GENERAR MOSTRAR LAS RESPUESTAS EN DISTINTOS BOTONES PORQUE
    //DE ESTA FORMA SIEMPRE LA RESPUESTA CORRECTA ESTA EN EL MISMO BOTON
    return (
      <View>

        <Text style={styles.espacio}> {"\n\n\n\n"}</Text>

        <View>
          <Text style={styles.welcome}> {question}</Text>
        </View>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, 1)}
            title={respuesta1}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, 0)}
            title={respuesta2}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, 0)}
            title={respuesta3}

          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, 0)}
            title={respuesta4}
          />
        </View>

        <Text style={styles.espacio}> {"\n\n"}</Text>

        <View>
          <Text style={styles.nivel}> Nivel {nivel}</Text>
        </View>

      </View>
    );
  }
  //toma la respuesta seleccionada por el jugador y la envia a la app
  handleAnswer = async (tipo) => {
    const { navigation } = this.props;
    const tipoResp = JSON.stringify(tipo);
    axios.post(API_HOST + "updateTypeQuest", {
      tipoResp: tipoResp,
    },
      {
        headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
      }
    )
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        AsyncStorage.setItem('userToken', response.config.headers.Authorization);
      })
    const area = navigation.getParam('areaId', 'NO-ID');
    const nivel = navigation.getParam('nivel', 'NO-ID');
    const areaComplet = navigation.getParam('areaComplet', 'NO-ID');
    this.props.navigation.navigate('Answer', { 'areaId': area, 'tipo': tipo, 'nivel': nivel, 'areaComplet': areaComplet}); //envia parametros a la sig vista
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

  nivel: {
    fontSize: 40,
    textAlign: 'right',
    margin: 15,
    color: "#ff0000",
  },

  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#4218F8'
  }

})
