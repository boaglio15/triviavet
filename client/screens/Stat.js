import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
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
        <Text style={styles.welcome}> ESTADISTICAS POR AREA </Text>

        <View style={styles.button}>
          <Button
            onPress={this.handleStatArea.bind(this, 1)}
            title="Anatomia"
            color="black"
          />
        </View>

        <View style={styles.button}>
          <Button
            onPress={this.handleStatArea.bind(this, 2)}
            title="Genetica Basica"
            color="black"
          />
        </View>

        <View style={styles.button}>
          <Button
            onPress={this.handleStatArea.bind(this, 3)}
            title="Inmunologia"
            color="black"
          />
        </View>
        
        <Text style={styles.espacio}> {"\n\n\n"}</Text>

        <View style={styles.button}>
          <Button
            onPress = {() => this.props.navigation.goBack()}
            title = "volver"
            color = "black"
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
