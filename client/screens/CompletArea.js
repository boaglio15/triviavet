import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import { AsyncStorage, View, Text, StyleSheet, Image } from 'react-native';
import {Button} from 'react-native-elements';
import axios from 'axios';

export default class CompletArea extends React.Component {
    static navigationOptions = {
        header: null,
    };

    constructor(props) {
        super(props);
    }

    render() {
        const { navigation } = this.props;
        const area = navigation.getParam('areaId', 'NO-ID');
        const feli = "¡¡Felicitaciones!!";
        return (
            <View style = {styles.container}>
                <View>
                    <Button
                        title = {feli}
                        type = 'outline'
                        titleStyle = {{color:'black', fontSize:35, fontFamily:'sans-serif-condensed'}}
                        buttonStyle = {{borderColor:'#445D56', backgroundColor:'#C7E3DB', borderWidth:2, alignSelf:'center'}}
                    />
                </View>

                <Text style={styles.welcome}> Has completado esta área del juego</Text>

                <Text style={styles.welcome}> Elegí otra para seguir jugando</Text>

                <View>
                    <View style={styles.welcomeContainer}>
                        <Image
                            source={
                                __DEV__
                                ? require("../assets/images/vaca.jpg")
                                : require("../assets/images/robot-prod.png")
                            }
                            style={styles.welcomeImage}
                        />
                    </View>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View>
                    <Button
                        onPress={this.salvarArea}
                        title="VOLVER"
                        buttonStyle = {{backgroundColor:'black', width:150, alignSelf:'center'}}
                    />
                </View>
            </View>
        );
    }
    //permite volver al home y habilita a la app a salvar las preguntas hechas en la partida en la base de datos
    salvarArea = async () => {
        const { navigation } = this.props;
        const area = navigation.getParam('areaId', 'NO-ID');
        const areaComplet = navigation.getParam('areaComplet', 'NO-ID');
           
        axios.post(API_HOST + "exit", {
            areaId: area,
            completada: areaComplet,
            nivel: 3,                                       //si completo el area el nivel es 3
        },
            {
                headers: { 'Authorization': await AsyncStorage.getItem('userToken') }
            }
        )
            .then(response => JSON.parse(JSON.stringify(response)))
            .then(response => {
                AsyncStorage.setItem('userToken', response.config.headers.Authorization);
            })
        this.props.navigation.navigate('Play');
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#30A9AC',
        borderRadius: 500,
    },
    welcome: {
        fontSize: 35,
        textAlign: 'center',
        fontFamily: 'sans-serif-condensed',
        margin: 10,
    },
    welcomeImage: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
        marginTop: 3,
        marginLeft: -10,
    },
    welcomeContainer: {
        alignItems: 'center',
        marginTop: 20,
        marginBottom: 20,
    },
    input: {
        margin: 15,
        height: 40,
        padding: 5,
        fontSize: 16,
        borderBottomWidth: 1,
        borderBottomColor: 'black'
    },
    logout: {
        fontSize: 14,
        color: 'black',
        textAlign: 'right',
    },
    getStartedContainer: {
        alignItems: 'center',
        marginHorizontal: 50,
      },

})