import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
import axios from "axios";

export default class QuestionsSreens extends React.Component {
  static navigationOptions = {
    header: null,
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

    return (
      <View>
        <Text style={styles.espacio}> {"\n\n\n\n"}</Text>

        <View>
          <Text style={styles.welcome}> {question}</Text>
        </View>

        <View style={styles.button}>
          <Button
            onPress={() => this.handleAnswer(tipoResp1)}  
            title={respuesta1}
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View style = {styles.button}>
          <Button
            onPress = {() => this.handleAnswer(tipoResp2)}
            title = {respuesta2}
            color = "black"
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View style = {styles.button}>
          <Button
            onPress = {() => this.handleAnswer(tipoResp3)}
            title = {respuesta3}
            color = "black"
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View style = {styles.button}>
          <Button
            onPress = {() => this.handleAnswer(tipoResp4)}
            title = {respuesta4}
            color = "black"
          />
        </View>

        <Text style={styles.espacio}> {"\n\n"}</Text>

        <View>
          <Text style={styles.nivel}> Nivel {nivel}</Text>
        </View>

        <View>
          <Text style={styles.nivel}> respuestas incorrectas {cantQuestIncorrect}</Text>
        </View>

      </View>
    );
  }
  //toma el tipo de respuesta seleccionada por el jugador y la envia a la app
  //en caso de seleccionar una resp incorrecta obtiene la respuesta correcta para mostrar en la siguiente vista
  handleAnswer = async (tipo) => {
    
    const { navigation } = this.props;
    const r1 = navigation.getParam("answ1", "NO-ID");
    const t1 = navigation.getParam("tipoAnsw1", "NO-ID");
    const r2 = navigation.getParam("answ2", "NO-ID");
    const t2 = navigation.getParam("tipoAnsw2", "NO-ID");
    const r3 = navigation.getParam("answ3", "NO-ID");
    const t3 = navigation.getParam("tipoAnsw3", "NO-ID");
    const r4 = navigation.getParam("answ4", "NO-ID");
    const t4 = navigation.getParam("tipoAnsw4", "NO-ID");
    
    const tipoResp = tipo;
    var resp = "error";
    
    //para obtener la respuesta correcta en caso de haber seleccionado la incorrecta
    if (tipoResp == 0) {
      if (t1 == 1) {
        resp = r1;
      }
      if (t2 == 1) {
        resp = r2;
      }
      if (t3 == 1) {
        resp = r3;
      }
      if (t4 == 1) {
        resp = r4;
      }
    }
    
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
      resp: resp,
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
    backgroundColor: "#30A9AC",
    borderRadius: 500
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
    color: "black"
  },

  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 1,
    borderBottomColor: "black"
  }
});
