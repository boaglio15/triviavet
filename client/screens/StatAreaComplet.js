import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, Button, StyleSheet } from "react-native";
import axios from "axios";

export default class StatAreaComplet extends React.Component {
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
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}> Esta area está completa </Text>

        <Text> La cantidad de correctas {correctas} </Text>
        <Text> La cantidad de incorrectas {incorrectas} </Text>

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