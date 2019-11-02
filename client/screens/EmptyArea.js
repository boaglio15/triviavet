import React from 'react';
import { View, Text, StyleSheet, } from 'react-native';
import {Button} from 'react-native-elements'; 

export default class EmptyArea extends React.Component {
    static navigationOptions = {
        header: null,
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={styles.container}>
                <View>
                    <Text style={styles.welcome}> No hay mas preguntas en el area </Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View>
                    <Text style={styles.welcome}> Cambia de area para seguir jugando </Text>
                </View>

                <Text>{"\n"}</Text>

                <View>
                    <Button
                        onPress={() => this.props.navigation.navigate('Play')}
                        title="VOLVER"
                        buttonStyle = {{backgroundColor:'black'}}
                    />
                </View>          
            </View>
        );
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
        fontSize: 30,
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
    getStartedContainer: {
        alignItems: 'center',
        marginHorizontal: 50,
      },

})