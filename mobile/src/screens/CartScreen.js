import React from 'react';
import { View, Text, SafeAreaView, FlatList, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { updateQty, removeFromCart, clearCart } from '../store/cartSlice';
import { addOrder } from '../store/ordersSlice';
import { api } from '../api/api';

const { width } = Dimensions.get('window');

export default function CartScreen({ navigation }){
  const items = useSelector(s => s.cart.items);
  const dispatch = useDispatch();

  const total = items.reduce((s,i)=> s + (i.price * (i.qty || 1)), 0);

  const handleCheckout = async () => {
    const payload = {
      items: items.map(i => ({
        productId: i.id,
        name: i.name,
        price: i.price,
        quantity: i.qty || 1
      }))
    };
    const resp = await api.post('/api/carts/checkout', payload);
    const order = resp.data?.data;
    dispatch(addOrder(order));
    dispatch(clearCart());
    navigation.navigate('OrderTracking', { orderId: order.id, orderType: 'cart' });
  };

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>Your Cart</Text>
      <FlatList data={items} keyExtractor={i=>String(i.id)} renderItem={({item})=> (
        <View style={styles.row}>
          <Text style={styles.name}>{item.name}</Text>
          <View style={styles.controls}>
            <TouchableOpacity onPress={()=> dispatch(updateQty({ id: item.id, qty: Math.max(1, (item.qty||1)-1) }))}><Text style={styles.qtyBtn}>-</Text></TouchableOpacity>
            <Text style={styles.qty}>{item.qty||1}</Text>
            <TouchableOpacity onPress={()=> dispatch(updateQty({ id: item.id, qty: (item.qty||1)+1 }))}><Text style={styles.qtyBtn}>+</Text></TouchableOpacity>
          </View>
          <TouchableOpacity onPress={()=> dispatch(removeFromCart({ id: item.id }))}><Text style={styles.remove}>Remove</Text></TouchableOpacity>
        </View>
      )} />

      <View style={styles.footer}>
        <Text style={styles.total}>Total: ${total.toFixed(2)}</Text>
        <TouchableOpacity style={styles.checkout} onPress={handleCheckout}><Text style={styles.coText}>Checkout</Text></TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.03 },
  title: { color: '#333', fontSize: width * 0.06, fontWeight: '800', marginBottom: width * 0.03 },
  row: { backgroundColor: '#f9f9f9', padding: width * 0.04, marginBottom: width * 0.02, borderRadius: 10, shadowColor: '#000', shadowOffset: { width: 0, height: 1 }, shadowOpacity: 0.1, shadowRadius: 2, elevation: 2 },
  name: { color: '#333', fontWeight: '700', fontSize: width * 0.045 },
  controls: { flexDirection: 'row', alignItems: 'center', marginTop: width * 0.02 },
  qtyBtn: { color: '#FF7E8B', fontSize: width * 0.05, paddingHorizontal: width * 0.03 },
  qty: { color: '#333', marginHorizontal: width * 0.02, fontSize: width * 0.04 },
  remove: { color: '#FF7E8B', marginTop: width * 0.02, fontSize: width * 0.035 },
  footer: { marginTop: 'auto', padding: width * 0.03 },
  checkout: { backgroundColor: '#FF7E8B', padding: width * 0.04, borderRadius: 10, alignItems: 'center' },
  coText: { color: '#fff', fontWeight: '800', fontSize: width * 0.045 },
  total: { color: '#333', fontWeight: '800', marginBottom: width * 0.02, fontSize: width * 0.045 }
});
