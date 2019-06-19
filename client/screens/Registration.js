import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import {
    AsyncStorage,
    View,
    Text,
    TextInput,
    Button,
    StyleSheet,
} from 'react-native';
import axios from 'axios';

export default class Registration extends React.Component {
    static navigationOptions = {
        title: 'Registrate como usuario',
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
                    placeholder="Ingresa tu nombre"
                    style={styles.input}
                    onChangeText={(value) => this.setState({ nombre: value })}
                    value={this.state.nombre}
                />

                <TextInput
                    placeholder="Ingresa tu apellido"
                    style={styles.input}
                    onChangeText={(value) => this.setState({ apellido: value })}
                    value={this.state.apellido}
                />

                <TextInput
                    placeholder="Ingresa tu dni"
                    style={styles.input}
                    onChangeText={(value) => this.setState({ dni: value })}
                    value={this.state.dni}
                />

                <TextInput
                    placeholder="Ingresa tu password"
                    style={styles.input}
                    onChangeText={(value) => this.setState({ pass: value })}
                    value={this.state.pass}
                />

                <Button title="registrar!" onPress={this.registration} />
            </View>
        );
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
                alert("Networking Error");
            });
            this.props.navigation.navigate('Auth');
    };
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#F5FCFF',
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
        borderBottomColor: '#ff0000',
    }
})