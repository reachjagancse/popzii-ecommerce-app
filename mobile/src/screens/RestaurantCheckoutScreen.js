import React from 'react';
import { View, Text, SafeAreaView, FlatList, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { useDispatch } from 'react-redux';
import { api } from '../api/api';
import { addOrder } from '../store/ordersSlice';

const { width } = Dimensions.get('window');

export default function RestaurantCheckoutScreen({ route, navigation }) {
  const { restaurant, cart, total } = route.params;
  const dispatch = useDispatch();

  const handlePlaceOrder = async () => {
    const payload = {
      restaurantId: restaurant.id,
      items: cart.map(i => ({
        menuItemId: i.id,
        name: i.name,
        price: i.price,
        quantity: i.qty
      }))
    };
    const resp = await api.post('/api/foodorders', payload);
    const order = resp.data?.data;
    dispatch(addOrder(order));
    navigation.navigate('OrderTracking', { orderId: order.id, orderType: 'food' });
  };

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>Checkout · {restaurant.name}</Text>
      <FlatList
        data={cart}
        keyExtractor={item => String(item.id)}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.name}>{item.name}</Text>
            <Text style={styles.qty}>x{item.qty}</Text>
            <Text style={styles.price}>${(item.price * item.qty).toFixed(2)}</Text>
          </View>
        )}
      />
      <View style={styles.footer}>
        <Text style={styles.total}>Total: ${total.toFixed(2)}</Text>
        <TouchableOpacity style={styles.checkout} onPress={handlePlaceOrder}>
          <Text style={styles.checkoutText}>Place order</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.04 },
  title: { fontSize: width * 0.055, fontWeight: '800', color: '#333', marginBottom: width * 0.03 },
  row: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: width * 0.02 },
  name: { fontWeight: '600', color: '#333' },
  qty: { color: '#666' },
  price: { color: '#FF7E8B', fontWeight: '700' },
  footer: { marginTop: 'auto' },
  total: { fontSize: width * 0.05, fontWeight: '800', marginBottom: width * 0.02 },
  checkout: { backgroundColor: '#FF7E8B', padding: width * 0.04, borderRadius: 12, alignItems: 'center' },
  checkoutText: { color: '#fff', fontWeight: '800' }
});
