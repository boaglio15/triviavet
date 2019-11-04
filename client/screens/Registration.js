import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import { AsyncStorage, View, Text, TextInput, StyleSheet, ScrollView, KeyboardAvoidingView
} from 'react-native';
import {Button} from 'react-native-elements';
import axios from 'axios';

export default class Registration extends React.Component {
    static navigationOptions = {
        header: null,
    };

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            nombre: '',
            apellido: '',
            dni: '',
            pass: ''
        }
    }

    render() {
        return (
                <View style={styles.container}>
                    <Text style={styles.welcome}> Registrate! </Text>

                    <TextInput
                        placeholder="Nombre"
                        style={styles.input}
                        onChangeText={(value) => this.setState({ nombre: value })}
                        value={this.state.nombre}
                    />

                    <TextInput
                        placeholder="Apellido"
                        style={styles.input}
                        onChangeText={(value) => this.setState({ apellido: value })}
                        value={this.state.apellido}
                    />

                    <TextInput
                        placeholder="Dni"
                        style={styles.input}
                        onChangeText={(value) => this.setState({ dni: value })}
                        value={this.state.dni}
                    />

                    <TextInput
                        placeholder="Password"
                        style={styles.input}
                        secureTextEntry={true}
                        onChangeText={(value) => this.setState({ pass: value })}
                        value={this.state.pass}
                    />
                    <View style={styles.button}>
                        <Button title="Registrar!" 
                            onPress={this.registration} 
                            buttonStyle = {{backgroundColor:'black', width:100}}
                        />
                    </View>

                    <View style={styles.button}>
                        <Button title="VOLVER" 
                        onPress={this.backLogin} 
                        buttonStyle = {{backgroundColor:'black', width:100}}/>
                    </View>
                </View>
        );
    }

    backLogin = () => {
        this.props.navigation.navigate('SignIn')
    }

    registration = () => {
        const { nombre, apellido, dni, pass } = this.state;
        axios.post(API_HOST + "user", {           
            nom: nombre,
            ape: apellido,
            dni: dni,
            pass: pass,
            tipoUser: 0,
        },
            {
                auth: {
                    username: 'admin',
                    password: 'admin'
                }
            }
        )
            .then(response => JSON.stringify(response))
            .then(response => {
                // Handle the JWT response here
                AsyncStorage.setItem('userToken', response.config.headers.Authorization);
                this.props.navigation.navigate('Auth');
            })
            .catch((error) => {
                if (error.toString().match(/401/)) {
                    alert("error");
                    return;
                }
                //alert("Networking Error");
            });
            this.props.navigation.navigate('Auth');
    };
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#30A9AC',
        alignItems:'center',
        borderRadius: 500,
    },
    welcome: {
        fontSize: 40,
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