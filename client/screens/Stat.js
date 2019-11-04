import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, StyleSheet } from "react-native";
import {Button} from 'react-native-elements';
import axios from "axios";

export default class Stat extends React.Component {
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
        <Text style={styles.welcome}> Selecciona el area para ver tus estadisticas </Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 1)}
            title="Examen Clínico"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 2)}
            title="Farmacología y Terapéutica"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 3)}
            title="Enferm. Infecciosas y Parasitarias"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 4)}
            title="Clínica Médica"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 5)}
            title="Clínica Quirúrgica"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>

        <View>
          <Button
            onPress={this.handleStatArea.bind(this, 6)}
            title="Manejo Poblacional"
            buttonStyle = {styles.buttonAreaStat}
          />
        </View>

        <Text>{"\n"}</Text>
        
        <View>
          <Button
            onPress = {() => this.props.navigation.goBack()}
            title = "VOLVER"
            buttonStyle = {{backgroundColor:'black'}}
          />
        </View>
      </View>
    );
  }

  handleStatArea = async area => {
    axios
      .get(API_HOST + "stat/" + area, {
        headers: { Authorization: await AsyncStorage.getItem("userToken") }
      })
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        const isPlay = response.data.isPlayArea;
        if (isPlay == 0) {
          //caso en el que el area no ha sido jugada
          alert("El area aun no ha sido jugada");
        } else {
            const completada = response.data.completada;
            const cantCorrectas = response.data.cantCorrect;
            const cantIncorrectas = response.data.cantIncorrect;
            if (completada == 1) {
                //Cuando el area esta completada
                this.props.navigation.navigate("EstadisticaAComplet", {
                    'cantCorrectas':cantCorrectas,
                    'cantIncorrectas':cantIncorrectas
                });
            } else {
            //Cuando el area no esta completada y en juego
            const nivel = response.data.nivel;
            this.props.navigation.navigate("EstadisticaEnJuego", {
                'cantCorrectas':cantCorrectas,
                'cantIncorrectas':cantIncorrectas,
                'nivel': nivel
            });
          }
        }
      })
      .catch(error => {
        if (error.toString().match(/401/)) {
          alert("Error");
          return;
        }
        alert("Networking Error question");
      });
  }
}

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      alignItems: "center",
      backgroundColor: "#30A9AC",
      borderRadius: 500
    },
    welcome: {
      fontSize: 25,
      fontFamily: 'sans-serif-condensed',
      textAlign: "center",
      margin: 30
    },
    buttonAreaStat: {
      backgroundColor:'black',
      width:270,
      borderWidth:2,
      borderColor:'#C7E3DB',
    },
    button: {
      marginTop: 20
    }
}); 
