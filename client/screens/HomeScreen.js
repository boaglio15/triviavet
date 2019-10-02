import { API_HOST } from 'react-native-dotenv';
import React from 'react';
import {
  AsyncStorage,
  Image,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Button,
} from 'react-native';
import { MonoText } from '../components/StyledText';

export default class HomeScreen extends React.Component {
  static navigationOptions = {
    header: null,
  };

  render() {
    return (
      <View style={styles.container}>
        <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>

          <View style={styles.getStartedContainer}>
            <Text style={styles.texto}>¿Cuanto Sabes de Veterinaria?</Text>
            <Text style={styles.espacio}> {"\n"}</Text>
            <Text style={styles.getStartedText}>Pon en practica tus conocimientos</Text>
            <View style={[styles.codeHighlightContainer, styles.homeScreenFilename]}>
              <MonoText style={styles.codeHighlightText}></MonoText>
            </View>

          <View style={styles.welcomeContainer}>
            <Image
              source={
                __DEV__
                  ? require('../assets/images/Logo.png')
                  : require('../assets/images/robot-prod.png')
              }
              style={styles.welcomeImage}
            />
          </View>

            <View style={styles.button}>
              <Button
                onPress={() => this.props.navigation.navigate('Play')}
                title="Play"
                color="black"
              />
            </View>

            <View style={styles.button}>
              <Button
                onPress={() => this.props.navigation.navigate('Estadistica')}
                title="Estadisticas"
                color="black"
              />
            </View>

            <View style={styles.button}>
              <Button
                onPress={this._handleLogout}
                title="Logout"
                color="black"
              />
            </View>
          </View>

          <View style={styles.helpContainer}>
            <TouchableOpacity onPress={this._handleHelpPress} style={styles.helpLink}>
              <Text style={styles.helpLinkText}></Text>
            </TouchableOpacity>
          </View>
        </ScrollView>

      </View>
    );
  }

  /*
  <View style={styles.tabBarInfoContainer}>
          <Text style={styles.tabBarInfoText}></Text>

          <View style={[styles.codeHighlightContainer, styles.navigationFilename]}>
            <MonoText style={styles.codeHighlightText}></MonoText>
          </View>
        </View>
  */

  onPressCategoryButton = (category) => {
    alert(category);
  }

  _handleLogout = async () => {
    await AsyncStorage.clear();
    this.props.navigation.navigate('Auth');
  };

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#30A9AC',
  },

  go: {
    fontSize:120,
    textAlign: 'center',
    margin: 10,
  },
  developmentModeText: {
    marginBottom: 20,
    color: 'rgba(0,0,0,0.4)',
    fontSize: 14,
    lineHeight: 19,
    textAlign: 'center',
  },
  contentContainer: {
    paddingTop: 30,
  },
  welcomeContainer: {
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 20,
  },
  welcomeImage: {
    width: 100,
    height: 100,
    resizeMode: 'contain',
    marginTop: 3,
    marginLeft: -10,
  },
  getStartedContainer: {
    alignItems: 'center',
    marginHorizontal: 50,
  },
  homeScreenFilename: {
    marginVertical: 7,
  },
  codeHighlightText: {
    color: 'rgba(96,100,109, 0.8)',
  },
  codeHighlightContainer: {
    backgroundColor: 'rgba(0,0,0,0.05)',
    borderRadius: 3,
    paddingHorizontal: 4,
  },
  getStartedText: {
    fontSize: 20,
    color: 'white',
    lineHeight: 24,
    textAlign: 'center',
  },
  tabBarInfoContainer: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    ...Platform.select({
      ios: {
        shadowColor: 'black',
        shadowOffset: { height: -3 },
        shadowOpacity: 0.1,
        shadowRadius: 3,
      },
      android: {
        elevation: 20,
      },
    }),
    alignItems: 'center',
    backgroundColor: '#fbfbfb',
    paddingVertical: 20,
  },
  tabBarInfoText: {
    fontSize: 17,
    color: 'rgba(96,100,109, 1)',
    textAlign: 'center',
  },
  navigationFilename: {
    marginTop: 5,
  },
  helpContainer: {
    marginTop: 15,
    alignItems: 'center',
  },
  helpLink: {
    paddingVertical: 15,
  },
  helpLinkText: {
    fontSize: 14,
    color: '#2e78b7',
  },
  logout: {
    fontSize: 14,
    color: '#2e78b7',
    textAlign: 'center',
  },
  button: {
    marginTop:15,
    flex:1,
    //paddingRight: 70,
    //paddingLeft: 70,
  },
  texto: {
    fontSize:40,
    color:'black',
    textAlign:'center',
  },
});
