import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
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
        <Text style={styles.welcome}> SELECCIONA UN AREA PARA JUGAR </Text>

        <View style={styles.button}>
          <Button
            onPress = {this.handleQuestionAnswer.bind(this, 1) }
            title = "Anatomia"
            color = "black"
          />
        </View>

        <View style={styles.button}>
          <Button
            onPress={this.handleQuestionAnswer.bind(this, 2) }
            title = "Genetica Basica"
            color = "black"
          />
        </View>

        <View style={styles.button}>
          <Button
            onPress = {this.handleQuestionAnswer.bind(this, 3) }
            title = "Inmunologia"
            color = "black"
          />
        </View>

        <Text style={styles.espacio}> {"\n\n\n"}</Text>
        
        <View style={styles.button}>
          <Button
            onPress={() => this.props.navigation.goBack()}
            title = "volver"
            color = "black"
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
            return this.props.navigation.navigate("AreaCompletada", {
              areaId: area
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
    justifyContent: "center",
    backgroundColor: "#30A9AC",
    borderRadius: 500
  },
  welcome: {
    fontSize: 25,
    textAlign: "center",
    margin: 30
  },
  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 1,
    borderBottomColor: "black"
  },
  button: {
    marginTop: 20
  }
});
