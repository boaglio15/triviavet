import { API_HOST } from "react-native-dotenv";
import React from "react";
import { AsyncStorage, View, Text, StyleSheet } from "react-native";
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/AntDesign';
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
        const correctas = navigation.getParam("cantCorrectas", "NO-ID").toString();
        const incorrectas = navigation.getParam("cantIncorrectas", "NO-ID").toString();
        const nivel = navigation.getParam("nivel", "NO-ID").toString();
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}> Estas en juego en esta area </Text>

        <Text style={styles.textStat}> Correctas</Text>
        <Button
          icon = {
            <Icon
              name = "checkcircleo"
              color = "green"
              size = {20}
            />
          }
          title = {correctas}
          titleStyle = {{color:'black', fontSize:20}}
          type = 'outline'
          buttonStyle = {{borderColor:'white', borderWidth:3, width:100, alignSelf:'center'}}
        />
        
        <Text>{"\n"}</Text>
        
        <Text style={styles.textStat}> Incorrectas</Text>
        <Button 
          icon = {
            <Icon
              name = "closecircleo"
              color = "red"
              size = {20}
              
            />
          }
          title = {incorrectas}
          titleStyle = {{color:'black', fontSize:20}}
          type = "outline"
          buttonStyle = {{borderColor:'white', borderWidth:3, width:100, alignSelf:'center'}}
        />

        <Text>{"\n"}</Text>

        <Text style={styles.textStat}> Tu nivel actual </Text>
        <Button
          title = {nivel}
          titleStyle = {{color:'black', fontSize:20}}
          type = "outline"
          buttonStyle = {{borderColor:'white', borderWidth:3, width:100, alignSelf:'center'}}
        />


        <Text>{"\n"}{"\n"}{"\n"}</Text>      
        <View>
          <Button
            onPress = {() => this.props.navigation.goBack()}
            title = "VOLVER"
            buttonStyle = {{backgroundColor:'black', width:150, alignSelf:'center'}}
          />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: "#30A9AC",
      borderRadius:500,
    },
    welcome: {
      fontSize: 35,
      fontFamily: 'sans-serif-condensed',
      textAlign: "center",
      margin: 30
    },
    textStat: {
      fontSize: 30,
      fontFamily: 'sans-serif-condensed',
      textAlign:'center',
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