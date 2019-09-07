import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
import axios from "axios";

export default class StatAreaInPlay extends React.Component {
  static navigationOptions = {
    title: "Area en juego"
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
        const { navigation } = this.props;
        const correctas = navigation.getParam("cantCorrectas", "NO-ID");
        const incorrectas = navigation.getParam("cantIncorrectas", "NO-ID");
        const nivel = navigation.getParam("nivel", "NO-ID");
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}> Estas en juego en esta area </Text>

        <Text> La cantidad de correctas {correctas} </Text>
        <Text> La cantidad de incorrectas {incorrectas} </Text>
        <Text> El nivel actual es {nivel} </Text>

        <View style={styles.button}>
          <Button
            onPress={() => this.props.navigation.goBack()}
            title="volver"
            color="#ff0000"
          />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: "center",
      backgroundColor: "#F5FCFF"
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
      borderBottomColor: "#4218F8"
    },
    button: {
      marginTop: 20
    }
});