import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, StyleSheet } from "react-native";
import {Button} from 'react-native-elements';
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
        var correctasString = "Correctas =";
        const cantCorrec = correctasString.concat(" ", correctas);
        const incorrectas = navigation.getParam("cantIncorrectas", "NO-ID");
        var incorString = "Incorrectas =";
        const cantIncorr = incorString.concat(" ", incorrectas);
    return (
      <View style={styles.container}>
        <Text style={styles.felicitaciones}>¡FELICITACIONES!</Text>
        <Text style={styles.welcome}> ÁREA COMPLETADA </Text>
        <Text style={styles.welcome}>Estos son tus resultados:</Text>

        <Button
          title = {cantCorrec}
          titleStyle = {{color:'black', fontSize:20}}
          type = 'outline'
          buttonStyle = {{borderColor:'green', borderWidth:2, width:200, alignSelf:'center'}}
        />
        <Text>{"\n"}</Text>
        
        <Button
          title = {cantIncorr}
          titleStyle = {{color:'black', fontSize:20}}
          type = 'outline'
          buttonStyle = {{borderColor:'red', borderWidth:2, width:200, alignSelf:'center'}}
        />

        <View style = {styles.button}>
          <Button
            onPress = {() => this.props.navigation.goBack()}
            title = "VOLVER"
            buttonStyle = {{backgroundColor:'black', width:100, alignSelf:'center'}}
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
      backgroundColor: "#30A9AC",
      borderRadius:500,
    },
    welcome: {
      fontSize: 25,
      fontFamily: 'sans-serif-condensed',
      textAlign: "center",
      margin: 30
    },
    felicitaciones: {
      fontSize: 35,
      fontFamily: 'sans-serif-condensed',
      textAlign: "center",
      margin: 30,      
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