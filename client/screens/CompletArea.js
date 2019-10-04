import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import { AsyncStorage, View, Text, Button, StyleSheet } from 'react-native';
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
        return (
            <View>
                <View>
                    <Text style={styles.welcome}> ¡¡¡Felicitaciones!!! </Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View>
                    <Text style={styles.welcome}> Area {area} Desbloqueada¡¡¡</Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View>
                    <Text style={styles.welcome}> Elegí otra para jugar</Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View style={styles.getStartedContainer}>
                    <Button
                        onPress={this.salvarArea} style={styles.logout}
                        title="--- ir a play ---"
                        color="black"
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
    },
    welcome: {
        fontSize: 35,
        textAlign: 'center',
        margin: 10,
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