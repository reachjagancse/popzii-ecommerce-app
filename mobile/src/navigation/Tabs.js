import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import HomeScreen from '../screens/HomeScreen';
import CartScreen from '../screens/CartScreen';
import RestaurantsScreen from '../screens/RestaurantsScreen';
import OrdersScreen from '../screens/OrdersScreen';
import { Text, Dimensions } from 'react-native';

const { width } = Dimensions.get('window');
const Tab = createBottomTabNavigator();

function Dummy(){ return <Text style={{color:'#fff'}}>Placeholder</Text> }

export default function Tabs(){
  return (
    <Tab.Navigator screenOptions={{ headerShown: false, tabBarStyle: { backgroundColor: '#fff', borderTopColor: '#ddd', height: width * 0.15 }, tabBarActiveTintColor: '#FF7E8B', tabBarInactiveTintColor: '#999', tabBarLabelStyle: { fontSize: width * 0.03 } }}>
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Explore" component={RestaurantsScreen} />
      <Tab.Screen name="Cart" component={CartScreen} />
      <Tab.Screen name="Orders" component={OrdersScreen} />
      <Tab.Screen name="Profile" component={Dummy} />
    </Tab.Navigator>
  );
}
