import React from 'react';
import {
    View,
    Text,
    Button,
    StyleSheet,
} from 'react-native';

export default class EmptyArea extends React.Component {
    static navigationOptions = {
        title: 'Area vacia',
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View>
                <View>
                    <Text style={styles.welcome}> El area seleccionada no contiene mas preguntas para responder </Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View>
                    <Text style={styles.welcome}> Elegí otra area para jugar</Text>
                </View>

                <Text style={styles.espacio}> {"\n"}</Text>

                <View style={styles.getStartedContainer}>
                    <Button
                        onPress={() => this.props.navigation.navigate('Play')} style={styles.logout}
                        title="--- ir a play ---"
                        color="#ff0000"
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
        backgroundColor: '#F5FCFF',
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
        borderBottomColor: '#4218F8'
    },
    logout: {
        fontSize: 14,
        color: '#2e78b7',
        textAlign: 'right',
    },
    getStartedContainer: {
        alignItems: 'center',
        marginHorizontal: 50,
      },

})