import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import { AsyncStorage, View, Text, TextInput, StyleSheet } from 'react-native';
import {Button} from 'react-native-elements';
import axios from 'axios';

export default class SignInScreen extends React.Component {
  static navigationOptions = {
    header: null,
  };

  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: ''
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome} > TriviaVet </Text>

        <View>
          <TextInput
            placeholder="Dni"
            style={styles.input}
            onChangeText={(value) => this.setState({ username: value })}
            value={this.state.username}
          />

          <TextInput
            placeholder="Password"
            style={styles.input}
            secureTextEntry={true}
            onChangeText={(value) => this.setState({ password: value })}
            value={this.state.password}
          />

          <View style={styles.button}>
            <Button title="INGRESAR" onPress={this._signIn} buttonStyle = {{backgroundColor:'black'}}/>
            <Text style={styles.espacio}> {"\n"}</Text>
          </View>

          <View style={styles.button}>
            <Button title="REGISTRATE" onPress={this.registrar} buttonStyle = {{backgroundColor:'black'}}/>
            <Text style={styles.espacio}> {"\n"}</Text>
          </View>

        </View>
      </View>
    );
  }

  registrar = () => {
    this.props.navigation.navigate('Registro')

  }

  _signIn = () => {
    const { username, password } = this.state;
    axios.post(API_HOST + "login", { //API_HOST
      username: username,
      password: password,
    },
      {
        auth: {
          username: username,
          password: password,
        }
      }
    )
      .then(response => JSON.parse(JSON.stringify(response)))
      .then(response => {
        // Handle the JWT response here
        AsyncStorage.setItem('userToken', response.config.headers.Authorization);//.config.headers.Authorization);
        this.props.navigation.navigate('App');
      })
      .catch((error) => {
        if (error.toString().match(/401/)) {
          alert("Username or Password incorrect");
          return;
        }

        alert("Networking Error");
      });
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems:'center',
    backgroundColor: '#30A9AC',
    borderRadius: 500,    
  },
  welcome: {
    fontSize: 40,
    fontFamily:'sans-serif-condensed', 
    textAlign: 'center',
    margin: 10,   
  },
  input: {
    margin: 15,
    height: 40,
    padding: 5,
    fontSize: 16,
    borderBottomWidth: 3,
    borderBottomColor: 'black',
  },
  button: {
    marginTop:15,
    paddingRight: 70,
    paddingLeft: 70,
  },
})
