import React, { useEffect, useState } from 'react';
import { View, Text, SafeAreaView, FlatList, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { api } from '../api/api';

const { width } = Dimensions.get('window');

export default function RestaurantMenuScreen({ route, navigation }) {
  const { restaurant } = route.params;
  const [categories, setCategories] = useState([]);
  const [items, setItems] = useState([]);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const [cart, setCart] = useState([]);

  useEffect(() => {
    const load = async () => {
      const resp = await api.get(`/api/restaurants/menu-categories?restaurantId=${restaurant.id}&page=0&size=50`);
      const data = resp.data?.data || [];
      setCategories(data);
      if (data[0]) setSelectedCategoryId(data[0].id);
    };
    load();
  }, [restaurant.id]);

  useEffect(() => {
    if (!selectedCategoryId) return;
    const loadItems = async () => {
      const resp = await api.get(`/api/restaurants/menu-items?categoryId=${selectedCategoryId}&page=0&size=50`);
      setItems(resp.data?.data || []);
    };
    loadItems();
  }, [selectedCategoryId]);

  const addItem = item => {
    setCart(prev => {
      const existing = prev.find(i => i.id === item.id);
      if (existing) return prev.map(i => i.id === item.id ? { ...i, qty: i.qty + 1 } : i);
      return [...prev, { ...item, qty: 1 }];
    });
  };

  const total = cart.reduce((sum, i) => sum + (i.price * i.qty), 0);

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>{restaurant.name}</Text>
      <View style={styles.categoryRow}>
        {categories.map(c => (
          <TouchableOpacity
            key={c.id}
            style={[styles.categoryChip, selectedCategoryId === c.id && styles.categoryChipActive]}
            onPress={() => setSelectedCategoryId(c.id)}
          >
            <Text style={[styles.categoryText, selectedCategoryId === c.id && styles.categoryTextActive]}>{c.name}</Text>
          </TouchableOpacity>
        ))}
      </View>
      <FlatList
        data={items}
        keyExtractor={item => String(item.id)}
        renderItem={({ item }) => (
          <View style={styles.itemCard}>
            <View>
              <Text style={styles.itemName}>{item.name}</Text>
              <Text style={styles.itemDesc}>{item.description || 'No description'}</Text>
            </View>
            <View style={styles.itemRight}>
              <Text style={styles.itemPrice}>${item.price}</Text>
              <TouchableOpacity style={styles.addBtn} onPress={() => addItem(item)}>
                <Text style={styles.addText}>Add</Text>
              </TouchableOpacity>
            </View>
          </View>
        )}
      />

      <TouchableOpacity
        style={[styles.checkout, cart.length === 0 && styles.checkoutDisabled]}
        disabled={cart.length === 0}
        onPress={() => navigation.navigate('RestaurantCheckout', { restaurant, cart, total })}
      >
        <Text style={styles.checkoutText}>View Cart · ${total.toFixed(2)}</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.04 },
  title: { fontSize: width * 0.055, fontWeight: '800', color: '#333', marginBottom: width * 0.03 },
  categoryRow: { flexDirection: 'row', flexWrap: 'wrap', gap: width * 0.02, marginBottom: width * 0.02 },
  categoryChip: { paddingVertical: width * 0.015, paddingHorizontal: width * 0.03, borderRadius: 20, borderWidth: 1, borderColor: '#eee' },
  categoryChipActive: { backgroundColor: '#FF7E8B', borderColor: '#FF7E8B' },
  categoryText: { color: '#555' },
  categoryTextActive: { color: '#fff', fontWeight: '700' },
  itemCard: { backgroundColor: '#f9f9f9', padding: width * 0.04, borderRadius: 12, marginBottom: width * 0.03, flexDirection: 'row', justifyContent: 'space-between' },
  itemName: { fontWeight: '700', color: '#333', fontSize: width * 0.042 },
  itemDesc: { marginTop: width * 0.01, color: '#666', fontSize: width * 0.035, maxWidth: width * 0.55 },
  itemRight: { alignItems: 'flex-end', gap: width * 0.015 },
  itemPrice: { color: '#FF7E8B', fontWeight: '700' },
  addBtn: { backgroundColor: '#FF7E8B', paddingVertical: width * 0.015, paddingHorizontal: width * 0.03, borderRadius: 8 },
  addText: { color: '#fff', fontWeight: '700' },
  checkout: { backgroundColor: '#FF7E8B', padding: width * 0.04, borderRadius: 12, alignItems: 'center', marginTop: width * 0.02 },
  checkoutDisabled: { backgroundColor: '#f3b5bc' },
  checkoutText: { color: '#fff', fontWeight: '800' }
});
