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
    title: 'tu pregunta es:',
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
          <Text>Pregunta: {question}</Text>
        </View>
       
        <Button
          onPress={this.handleAnswer.bind(this, '1')}
          title={respuesta1}
          color="#a4f590"
        />
        <Button
          onPress={this.handleAnswer.bind(this, '0')}
          title={respuesta2}
          color="#a4f590"
        />
        <Button
          onPress={this.handleAnswer.bind(this, '0')}
          title={respuesta3}
          color="#a4f590"
        />
        <Button
          onPress={this.handleAnswer.bind(this, '0')}
          title={respuesta4}
          color="#a4f590"
        />

      </View>
    );
  }

  handleAnswer = async (tipo) => {
    const { navigation } = this.props;
    /*axios.post("http://192.168.1.9:4567/updateTypeQuest", {
      tipoResp : 'tipo',
    },
      {
        headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
      }
    )*/
    const area = navigation.getParam('areaId', 'NO-ID');
    this.props.navigation.navigate('Answer',{ 'areaId': area,'tipo': tipo});
  }

  


  

/*
  //<Button title="btn!" onPress={this.btnPress()} />
  //muestra los elementos de un array
  showArrayElements() {
    if (this.state.list) {
      return this.state.list.map((task, i) => {
        return (
          <View style={styles.container} Key={i}>
            <Text>
              {task}
            </Text>
          </View>
        )
      })
    }
  }
*/
  
/*
  // saca un elemento guardado en AsyncStorage
  showElement2() {
    AsyncStorage.getItem('use').then(value => {
      return (
        <View style={styles.container}>
          <Text>
            {this.setState({ area: JSON.parse(value) })}
          </Text>
        </View>
      )
    }
    )*/


  



  /*btnPress() {
    const data = {
      dat1: this.state.a1,
      dat2: this.state.a2,
      dat3: this.state.a3,
    }
    AsyncStorage.getItem('datosLocales').then((value) => {
      if (value !== null) {
        AsyncStorage.setItem('datosLocales',JSON.parse(value));
        this.props.navigator('Home');
        //.push('Home'
          //{
          //title:'lll',
          //component: Home
       // }
        //)
      } else {
        AsyncStorage.setItem('datosLocales',JSON.stringify(data));
        this.props.navigator('Home')
        //.push('Play')
          //{
          //title:'sss',
          //component: Play }
          //)
        }
    })
  }


}*/

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
