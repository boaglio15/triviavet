import {API_HOST} from 'react-native-dotenv';
import React from 'react';
import {AsyncStorage, View, Text, StyleSheet, Image } from 'react-native';
import {Button} from 'react-native-elements';
import axios from 'axios';

export default class LostArea extends React.Component {
    static navigationOptions = {
        header: null,
    };

    constructor(props) {
        super(props);
    }

    render() {
        const perdi = "¡PERDISTE!"
        return (
            <View style={styles.container}>
                <View>
                    <Button
                        title = {perdi}
                        type = 'outline'
                        titleStyle = {{color:'black', fontSize:35, fontFamily:'sans-serif-condensed'}}
                        buttonStyle = {{borderColor:'#445D56', backgroundColor:'#C7E3DB', borderWidth:2, alignSelf:'center'}}
                    />
                </View>
                <Text style={styles.welcome}> Mejor vuelve a intentarlo</Text>
                <View style={styles.welcomeContainer}>
                    <Image
                        source={
                            __DEV__
                            ? require('../assets/images/cavallo.png')
                            : require('../assets/images/robot-prod.png')
                        }
                        style={styles.welcomeImage}
                    />
                </View>

                <Text>{"\n"}</Text>

                <Button
                    onPress = {this.resetArea}
                    title = "VOLVER"
                    buttonStyle = {{backgroundColor:'black', width:150, alignSelf:'center'}}
                />                
            </View>
        );
    }

    resetArea = async () => {
        axios.put(API_HOST + "reset",{},
        {
            headers:  { 'Authorization': await AsyncStorage.getItem('userToken') }
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
        borderRadius:500,
    },
    welcome: {
        fontSize: 35,
        textAlign: 'center',
        fontFamily: 'sans-serif-condensed',
        margin: 10,
    },
    welcomeContainer: {
        alignItems: 'center',
        marginTop: 10,
        marginBottom: 10,
    },
    welcomeImage: {
        width: 200,
        height: 200,
        resizeMode: 'contain',
        marginTop: 3,
        marginLeft: -10,
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