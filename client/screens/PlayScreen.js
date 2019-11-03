import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, StyleSheet } from "react-native";
import {Button} from 'react-native-elements';
import axios from "axios";

export default class PlayScreen extends React.Component {
  static navigationOptions = {
    header: null,
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>

        <Text>{"\n"}</Text>
        
        <Text style={styles.welcome}> Selecciona un area para jugar: </Text>

        <Text>{"\n"}</Text>
        
        <Button
          onPress = {this.handleQuestionAnswer.bind(this, 1) }
          title = "Examen Clínico"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}</Text>

        <Button
          onPress={this.handleQuestionAnswer.bind(this, 2) }
          title = "Farmacología y Terapéutica"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}</Text>

        <Button
          onPress = {this.handleQuestionAnswer.bind(this, 3) }
          title = "Enferm. Infecciosas y Parasitarias"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}</Text>

        <Button
          onPress = {this.handleQuestionAnswer.bind(this, 4) }
          title = "Clínica Médica"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}</Text>

        <Button
          onPress = {this.handleQuestionAnswer.bind(this, 5) }
          title = "Clínica Quirúrgica"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}</Text>

        <Button
          onPress = {this.handleQuestionAnswer.bind(this, 6) }
          title = "Manejo Poblacional"
          buttonStyle = {styles.buttonArea}
        />

        <Text>{"\n"}{"\n"}</Text>
        
        <View style={styles.button}>
          <Button
            onPress={() => this.props.navigation.goBack()}
            title = "VOLVER"
            buttonStyle = {{backgroundColor:'black'}}
          />
        </View>
      </View>
    );
  }

  //pide una pregunta inicial y parametros para iniciar la partida de acuerdo al area seleccionada
  handleQuestionAnswer = async area => {
    axios
      .get(API_HOST + "selectQuestionAnswerInit/" + area, {
        headers: { Authorization: await AsyncStorage.getItem("userToken") }
      })
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        const areaComplet = response.data.areaComplet;
        const areaSinPreg = response.data.areaSinPreg;
        if (areaSinPreg == 1) {
          //caso area sin preguntas para hacer
          this.props.navigation.navigate("AreaSinPreguntas");
        } else {
          if (areaComplet == 1) {
            //caso area jugada y completada
            console.log("AREA COMPLETADA " + areaComplet);

            return this.props.navigation.navigate("AreaCompletada", {
              areaId: area,
              areaComplet: areaComplet
            });
          } else {
            //caso area en juego
            const quest = response.data.preg;
            const questId = response.data.id;
            const answ1 = response.data.resp1;
            const tipoAnsw1 = response.data.tipo1;
            const answ2 = response.data.resp2;
            const tipoAnsw2 = response.data.tipo2;
            const answ3 = response.data.resp3;
            const tipoAnsw3 = response.data.tipo3;
            const answ4 = response.data.resp4;
            const tipoAnsw4 = response.data.tipo4;
            const nivel = response.data.nivel;
            const cantQuestIncorrect = response.data.cantPregInco;
            //console.log(nivel);
            //console.log(areaComplet);
            this.props.navigation.navigate("QuestionsAnswers", {
              quest: quest,
              questId: questId,
              areaId: area,
              answ1: answ1,
              tipoAnsw1: tipoAnsw1,
              tipoAnsw2: tipoAnsw2,
              tipoAnsw3: tipoAnsw3,
              tipoAnsw4: tipoAnsw4,
              answ2: answ2,
              answ3: answ3,
              answ4: answ4,
              nivel: nivel,
              areaComplet: areaComplet,
              areaSinPreg: areaSinPreg,
              cantQuestIncorrect: cantQuestIncorrect
            });
          }
        }
      })
      .catch(error => {
        if (error.toString().match(/401/)) {
          alert("No hay preguntas");
          return;
        }
        alert("Networking Error question");
      });
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems:"center",
    backgroundColor: "#30A9AC",
    borderRadius: 500,
  },
  welcome: {
    fontSize: 40,
    fontFamily:'sans-serif-condensed', 
    textAlign: 'center',
    margin: 10, 
  },
  buttonArea: {
    backgroundColor:'black',
    borderWidth:2,
    borderColor:'#C7E3DB',
  },
  button: {
    flex:1,
    width:200,
    marginEnd:1,
  }
});
