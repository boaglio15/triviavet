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
        <Text style={styles.welcome}> selecciona un area para jugar </Text>


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
            color="#a4f590"
          /> 
        </View>

      </View>
    );
  }



  handleQuestionAnswer = async (area) => {
    axios.get("http://192.168.0.94:4567/selectQuestionAnswerInit/" + area, {
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
      //var descripcion = this.state.preg;
      //var idQuest = this.state.questId;
      //console.log(descripcion);
      //console.log(idQuest);
      //const {navigation} = this.props;
      //const idQuest = this.state.questId;
      /*
      axios.get("http://192.168.1.9:4567/newAnswer/" + this.state.questId, {
          headers: {'Authorization' : await AsyncStorage.getItem('userToken')}
        })
        .then(response => JSON.parse(JSON.stringify(response)))
        .then(response =>{
          var listAnswers = response.data; //lista de answers
          console.log(listaAnswers);
          this.props.navigation.navigate('QuestionsAnswers',
          {'preg': preg, 'questId': idQuest, 'area': area,
            'listAnswers' : listAnswers});
        })
        .catch((error) => {
          if (error.toString().match(/401/)){
            alert("Error con las respuestas de la pregunta");
            return;
          }
          alert("Networking Error answer");
        }) 
  };*/

/*
  showPregunta = async () => {
    axios.get("http://192.168.88.96:4567/newQuestion/" + areaId, {
      auth: "Basic AsyncStorage.getItem('userToken')",
    })
      .then(response => {
        { this.setState({ questJson: response.data }) }
      })
      .catch((error) => {
        if (error.toString().match(/401/)) {
          alert("No hay preguntas");
          return;
        }
        alert("Networking Error");
      });
  };*/








  /*
    _signInAsync = async () => {
      await AsyncStorage.setItem('use', JSON.stringify(1));
      this.props.navigation.navigate('QuestionsAnswers')
  
   */
  /*
    _signInAsync = async () => {
      await AsyncStorage.setItem('use',JSON.stringify(1));
      this.props.navigation.navigate('QuestionsAnswers')
  <View style={styles.button}>
          <Button title="Sign in!" onPress={this._signInAsync} />
          </View>
  
    };*/


  //QUE HAY QUE MODIFICAR EN LA APP PARA PODER HACER LAS CONSULTAS???
  //COMO MOSTRAR EL RESULTADO DEL GET (areas)???
  //COMO HACER PARA GENERAR UN BOTON DE ACUERDO A LA CANTIDAD DE AREAS EN LAS QUE ESTA JUGANDO???

  //_handleBack = async () => {
  // await AsyncStorage.clear();
  // this.props.navigation.navigate('App');
  //};


  /*
    questionsAnswers = (idArea) => {
      axios.get("http://192.168.0.17:4567/newQuestion")
        .then(res => {
          const idPreg = res.data;
          this.setState({ idPreg });
        },
  
          {
            auth: "Basic AsyncStorage.getItem('userToken')"
          }
        )
        .then(response => JSON.stringify(response))
        .then(response => {
          //console.log(response);
  
          // Handle the JWT response here
  
        })
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
