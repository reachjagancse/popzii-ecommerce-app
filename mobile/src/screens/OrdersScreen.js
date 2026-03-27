import React from 'react';
import { View, Text, SafeAreaView, FlatList, StyleSheet, Dimensions } from 'react-native';
import { useSelector } from 'react-redux';

const { width } = Dimensions.get('window');

export default function OrdersScreen() {
  const orders = useSelector(s => s.orders.items);

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>Your Orders</Text>
      <FlatList
        data={orders}
        keyExtractor={item => String(item.id)}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.name}>Order #{item.id}</Text>
            <Text style={styles.status}>{item.status || 'PLACED'}</Text>
          </View>
        )}
        ListEmptyComponent={<Text style={styles.empty}>No orders yet.</Text>}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.04 },
  title: { fontSize: width * 0.055, fontWeight: '800', color: '#333', marginBottom: width * 0.03 },
  row: { backgroundColor: '#f9f9f9', padding: width * 0.04, borderRadius: 10, marginBottom: width * 0.02 },
  name: { fontWeight: '700', color: '#333' },
  status: { marginTop: width * 0.01, color: '#FF7E8B', fontWeight: '700' },
  empty: { color: '#666' }
});
