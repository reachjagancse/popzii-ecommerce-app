import React, { useEffect, useState } from 'react';
import { View, Text, SafeAreaView, FlatList, StyleSheet, TouchableOpacity, Dimensions, TextInput } from 'react-native';
import { api } from '../api/api';

const { width } = Dimensions.get('window');

export default function RestaurantsScreen({ navigation }) {
  const [restaurants, setRestaurants] = useState([]);
  const [search, setSearch] = useState('');
  const [cuisine, setCuisine] = useState('');
  const [minRating, setMinRating] = useState('');

  const loadRestaurants = async () => {
    const params = new URLSearchParams();
    if (cuisine) params.append('cuisine', cuisine);
    if (minRating) params.append('minRating', minRating);
    params.append('page', '0');
    params.append('size', '50');
    const resp = await api.get(`/api/restaurants?${params.toString()}`);
    setRestaurants(resp.data?.data || []);
  };

  useEffect(() => { loadRestaurants(); }, []);

  const filtered = restaurants.filter(r =>
    r.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <SafeAreaView style={styles.safe}>
      <Text style={styles.title}>Restaurants</Text>
      <View style={styles.filters}>
        <TextInput
          style={styles.input}
          placeholder="Search..."
          value={search}
          onChangeText={setSearch}
        />
        <TextInput
          style={styles.input}
          placeholder="Cuisine"
          value={cuisine}
          onChangeText={setCuisine}
          onBlur={loadRestaurants}
        />
        <TextInput
          style={styles.input}
          placeholder="Min rating"
          value={minRating}
          onChangeText={setMinRating}
          keyboardType="numeric"
          onBlur={loadRestaurants}
        />
      </View>
      <FlatList
        data={filtered}
        keyExtractor={item => String(item.id)}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.card}
            onPress={() => navigation.navigate('RestaurantMenu', { restaurant: item })}
          >
            <Text style={styles.name}>{item.name}</Text>
            <Text style={styles.meta}>{item.cuisine || 'Cuisine'} · Rating {item.rating || 'N/A'}</Text>
            <Text style={styles.address}>{item.address || 'Address unavailable'}</Text>
          </TouchableOpacity>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff', padding: width * 0.04 },
  title: { fontSize: width * 0.06, fontWeight: '800', marginBottom: width * 0.03, color: '#333' },
  filters: { gap: width * 0.02, marginBottom: width * 0.03 },
  input: { borderWidth: 1, borderColor: '#eee', borderRadius: 12, padding: width * 0.03, fontSize: width * 0.04 },
  card: { backgroundColor: '#f9f9f9', padding: width * 0.04, borderRadius: 12, marginBottom: width * 0.03 },
  name: { fontSize: width * 0.045, fontWeight: '700', color: '#333' },
  meta: { marginTop: width * 0.015, color: '#FF7E8B', fontWeight: '600' },
  address: { marginTop: width * 0.01, color: '#666' }
});
