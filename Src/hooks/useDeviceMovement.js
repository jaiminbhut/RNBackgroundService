import Geolocation from '@react-native-community/geolocation';
import {useEffect, useState} from 'react';
import {ToastAndroid} from 'react-native';

const useDeviceMovement = () => {
  const [speed, setSpeed] = useState(0);

  const calculateSpeed = location => {
    if (location.coords.speed) {
      // Convert speed from meters/second to km/h
      const speedInKmPerHour = (location.coords.speed * 3.6).toFixed(2);
      setSpeed(parseFloat(speedInKmPerHour));
    }
  };

  useEffect(() => {
    const watchId = Geolocation.watchPosition(
      position => calculateSpeed(position),
      error => {
        ToastAndroid.show(error.message, 1000);
      },
      {
        enableHighAccuracy: true,
        distanceFilter: 10, // Minimum distance (in meters) for a location update
        interval: 1000, // Time interval (in milliseconds) for location updates
      },
    );

    return () => {
      Geolocation.clearWatch(watchId);
    };
  }, []);

  return {
    speed,
  };
};

export default useDeviceMovement;
