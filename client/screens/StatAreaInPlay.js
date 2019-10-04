import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
import axios from "axios";

export default class StatAreaInPlay extends React.Component {
  static navigationOptions = {
    header: null,
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

        <View style = {styles.button}>
          <Button
            onPress = {() => this.props.navigation.goBack()}
            title = "volver"
            color = "black"
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
      backgroundColor: "#30A9AC"
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