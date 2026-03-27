import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Tabs from './Tabs';
import ProductDetailScreen from '../screens/ProductDetailScreen';
import OrderTrackingScreen from '../screens/OrderTrackingScreen';
import RestaurantMenuScreen from '../screens/RestaurantMenuScreen';
import RestaurantCheckoutScreen from '../screens/RestaurantCheckoutScreen';

const Stack = createNativeStackNavigator();

export default function RootNavigator(){
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Main" component={Tabs} />
        <Stack.Screen name="ProductDetail" component={ProductDetailScreen} />
        <Stack.Screen name="RestaurantMenu" component={RestaurantMenuScreen} />
        <Stack.Screen name="RestaurantCheckout" component={RestaurantCheckoutScreen} />
        <Stack.Screen name="OrderTracking" component={OrderTrackingScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
