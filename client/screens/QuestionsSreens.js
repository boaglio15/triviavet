import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, StyleSheet } from "react-native";
import {Button} from "react-native-elements";
import Icon from 'react-native-vector-icons/FontAwesome';
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

    var nivelString = "Nivel:";
    const nivelComplet = nivelString.concat(" ", nivel);
    var incorString = "Incorrectas:";
    const incorComplet = incorString.concat(" ", cantQuestIncorrect);

    return (
      <View style={styles.container}>
        <Text> {"\n"}{"\n"}</Text>

        <View>
          <Button
            title = {question}
            titleStyle = {{color:'black', fontFamily:'sans-serif-condensed', fontSize:25}}
            type = 'outline'
            buttonStyle = {styles.buttonQuestion}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={() => this.handleAnswer(tipoResp1)}  
            title={respuesta1}
            buttonStyle={styles.buttonOptions}
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View>
          <Button
            onPress = {() => this.handleAnswer(tipoResp2)}
            title = {respuesta2}
            buttonStyle={styles.buttonOptions}
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View>
          <Button
            onPress = {() => this.handleAnswer(tipoResp3)}
            title = {respuesta3}
            buttonStyle={styles.buttonOptions}
          />
        </View>
        <Text style = {styles.espacio}> {"\n"}</Text>

        <View>
          <Button
            onPress = {() => this.handleAnswer(tipoResp4)}
            title = {respuesta4}
            buttonStyle={styles.buttonOptions}
          />
        </View>

        <Text style={styles.espacio}> {"\n\n"}</Text>

        <Button
          title = {nivelComplet}
          type = 'outline'
          titleStyle = {{color:'black', fontFamily:'sans-serif-condensed', fontSize:25}}
          buttonStyle = {{borderColor:'white', borderWidth:2, width:200, alignSelf:'center'}}
        />
        <Text>{"\n"}</Text>
        
        <Button
          title = {incorComplet}
          type = 'outline'
          titleStyle = {{color:'black', fontFamily:'sans-serif-condensed', fontSize:25}}
          buttonStyle = {{borderColor:'white', borderWidth:2, width:200, alignSelf:'center'}}
        />

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
    fontSize: 25,
    textAlign: "center",
    fontFamily:'sans-serif-condensed',
    margin: 10
  },
  nivel: {
    fontSize: 40,
    textAlign: "right",
    fontFamily:'sans-serif-condensed',
    margin: 15,
    color: "black"
  },
  buttonQuestion: {
    borderColor:'#445D56',
    backgroundColor:'#C7E3DB',
    borderWidth:2, 
    alignSelf:'center',
  },
  buttonOptions: {
    backgroundColor:'black',
    alignSelf:'center',
    borderWidth:2,
    borderColor:'#C7E3DB',
  },
});
