/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect} from 'react';
import {
  PermissionsAndroid,
  SafeAreaView,
  StatusBar,
  Text,
  View,
  useColorScheme,
} from 'react-native';

import {requestNotifications} from 'react-native-permissions';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import LocationService from './LocationService';

async function requestUserPermission() {
  await requestNotifications();
  await requestLocationPermissions();
}

const requestLocationPermissions = async () => {
  try {
    const foregroundGranted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    );

    if (foregroundGranted === PermissionsAndroid.RESULTS.GRANTED) {
      const backgroundGranted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION,
      );

      if (backgroundGranted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('Location Permission Granted');
        LocationService.startService();
      } else {
        // Background location permission denied
        // Handle the denial or inform the user accordingly
      }
    } else {
      // Foreground location permission denied
      // Handle the denial or inform the user accordingly
    }
  } catch (error) {
    console.log('Error occurred while requesting location permissions:', error);
  }
};

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  // const {speed} = useDeviceMovement();

  // useEffect(() => {
  //   ToastAndroid.show(`Speed ${speed} KM/H`, 1000);
  // }, [speed]);

  useEffect(() => {
    // Start the location service
    requestUserPermission();
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <View>
        <Text>RNBGS</Text>
      </View>
    </SafeAreaView>
  );
}

export default App;
