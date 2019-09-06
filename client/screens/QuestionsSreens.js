import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
import axios from "axios";

export default class QuestionsSreens extends React.Component {
  static navigationOptions = {
    title: "Tu pregunta es:"
  };
  constructor(props) {
    super(props);
    this.state = {};
  }
  render() {
    //recibir cant preg hacer
    const { navigation } = this.props;
    const question = navigation.getParam("quest", "NO-ID");
    const respuesta1 = navigation.getParam("answ1", "NO-ID");
    const tipoResp1 = navigation.getParam("tipoAnsw1", "NO-ID");
    const respuesta2 = navigation.getParam("answ2", "NO-ID");
    const tipoResp2 = navigation.getParam("tipoAnsw2", "NO-ID");
    const respuesta3 = navigation.getParam("answ3", "NO-ID");
    const tipoResp3 = navigation.getParam("tipoAnsw3", "NO-ID");
    const respuesta4 = navigation.getParam("answ4", "NO-ID");
    const tipoResp4 = navigation.getParam("tipoAnsw4", "NO-ID");
    const nivel = navigation.getParam("nivel", "NO-ID");
    const cantQuestIncorrect = navigation.getParam("cantQuestIncorrect","NO-ID");

    console.log(tipoResp1);
    console.log(tipoResp2);
    console.log(tipoResp3);
    console.log(tipoResp4);

    //console.log(tipo1);
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
            onPress={this.handleAnswer.bind(this, tipoResp1)}
            title={respuesta1}
          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, tipoResp2)}
            title={respuesta2}
          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, tipoResp3)}
            title={respuesta3}
          />
        </View>
        <Text style={styles.espacio}> {"\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleAnswer.bind(this, tipoResp4)}
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
  handleAnswer = async tipo => {
    const { navigation } = this.props;
    const tipoResp = tipo;
    axios
      .post(
        API_HOST + "updateTypeQuest",
        {
          tipoResp: tipoResp
        },
        {
          headers: { Authorization: await AsyncStorage.getItem("userToken") }
        }
      )
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        AsyncStorage.setItem(
          "userToken",
          response.config.headers.Authorization
        );
      });
    const area = navigation.getParam("areaId", "NO-ID");
    const nivel = navigation.getParam("nivel", "NO-ID");
    const areaComplet = navigation.getParam("areaComplet", "NO-ID");
    const areaSinPreg = navigation.getParam("areaSinPreg", "No-ID");
    const cantQuestIncorrect = navigation.getParam("cantQuestIncorrect","NO-ID");

    //envia datos al componente AnswersSreens (navigation machea Answer con AnswersSreens)
    this.props.navigation.navigate("Answer", {
      areaId: area,
      tipo: tipo,
      nivel: nivel,
      areaComplet: areaComplet,
      areaSinPreg: areaSinPreg,
      cantQuestIncorrect: cantQuestIncorrect
    }); //envia parametros a la sig vista  }
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    backgroundColor: "#F5FCFF"
  },
  welcome: {
    fontSize: 20,
    textAlign: "center",
    margin: 10
  },

  nivel: {
    fontSize: 40,
    textAlign: "right",
    margin: 15,
    color: "#ff0000"
  },

  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 1,
    borderBottomColor: "#4218F8"
  }
});
