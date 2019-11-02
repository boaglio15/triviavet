import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import {Button} from 'react-native-elements';

export default class LostArea extends React.Component {
    static navigationOptions = {
        header: null,
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}> ¡PERDISTE! </Text>
                <Text style={styles.welcome}> Mejor vuelve a intentarlo</Text>
                <Text style={styles.welcome}>Volvé y elegí otra área o la misma para seguir jugando</Text>

                <Text>{"\n"}</Text>

                <Button
                    onPress = {() => this.props.navigation.navigate('Play')} style={styles.logout}
                    title = "VOLVER"
                    buttonStyle = {{backgroundColor:'black', width:20, alignSelf:'center'}}
                />                
            </View>
        );
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