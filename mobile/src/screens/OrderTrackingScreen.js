import React, { useEffect, useRef, useState } from 'react';
import { View, Text, SafeAreaView, StyleSheet, Dimensions } from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { updateOrderStatus } from '../store/ordersSlice';
import { api } from '../api/api';

const { width } = Dimensions.get('window');

export default function OrderTrackingScreen({ route }){
  const { orderId, orderType } = route.params || {};
  const order = useSelector(s => s.orders.items.find(o => o.id === orderId));
  const dispatch = useDispatch();
  const [status, setStatus] = useState(order?.status || 'PLACED');
  const statusRef = useRef(status);

  useEffect(() => {
    statusRef.current = status;
  }, [status]);

  useEffect(() => {
    const load = async () => {
      if (orderType === 'food') {
        const resp = await api.get(`/api/foodorders/${orderId}`);
        const data = resp.data?.data;
        if (data?.status) setStatus(data.status);
      }
      if (orderType === 'cart') {
        const resp = await api.get(`/api/carts/${orderId}`);
        const data = resp.data?.data;
        if (data?.status) setStatus(data.status);
      }
    };
    load();
  }, [orderId, orderType]);

  useEffect(()=>{
    // simulate real-time updates
    const seq = ['PLACED','CONFIRMED','PREPARING','OUT_FOR_DELIVERY','DELIVERED'];
    let idx = seq.indexOf(statusRef.current);
    const t = setInterval(async ()=>{
      if (idx < seq.length-1) {
        idx++;
        setStatus(seq[idx]);
        dispatch(updateOrderStatus({ id: orderId, status: seq[idx] }));
        if (orderType === 'food') {
          await api.patch(`/api/foodorders/${orderId}/status?status=${seq[idx]}`);
        }
        if (orderType === 'cart') {
          await api.patch(`/api/carts/${orderId}/status?status=${seq[idx]}`);
        }
      } else {
        clearInterval(t);
      }
    }, 4000);
    return ()=> clearInterval(t);
  }, [orderId, orderType, dispatch]);

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>Order #{orderId}</Text>
      <Text style={styles.status}>Status: {status}</Text>
      <View style={styles.timeline}>
        <Text style={styles.p}>We will update the order status in real-time here.</Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.04 },
  title: { color: '#333', fontSize: width * 0.055, fontWeight: '800' },
  status: { color: '#FF7E8B', marginTop: width * 0.03, fontWeight: '800', fontSize: width * 0.045 },
  timeline: { marginTop: width * 0.06 },
  p: { color: '#666', fontSize: width * 0.04 }
});
