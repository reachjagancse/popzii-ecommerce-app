import React from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';

const { width } = Dimensions.get('window');
const cardWidth = (width - 48) / 2; // 2 columns with padding

export default function ProductCard({ product, onPress, onAdd }) {
  return (
    <TouchableOpacity style={styles.card} onPress={() => onPress(product)}>
      <Image source={{ uri: product.imageUrl || `https://picsum.photos/400/600?random=${product.id || 1}` }} style={styles.image} />
      <View style={styles.row}>
        <Text style={styles.title} numberOfLines={1}>{product.name}</Text>
        <Text style={styles.price}>${product.price}</Text>
      </View>
      <TouchableOpacity style={styles.addBtn} onPress={() => onAdd(product)}>
        <Text style={styles.addText}>ADD</Text>
      </TouchableOpacity>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  card: { backgroundColor: '#fff', borderRadius: 12, padding: width * 0.02, margin: width * 0.015, width: cardWidth, shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.1, shadowRadius: 4, elevation: 3 },
  image: { width: '100%', height: width * 0.4, borderRadius: 8, backgroundColor: '#f0f0f0' },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: width * 0.02 },
  title: { color: '#333', fontWeight: '700', width: cardWidth * 0.6, fontSize: width * 0.035 },
  price: { color: '#FF7E8B', fontWeight: '800', fontSize: width * 0.04 },
  addBtn: { backgroundColor: '#FF7E8B', marginTop: width * 0.02, paddingVertical: width * 0.02, borderRadius: 8, alignItems: 'center' },
  addText: { color: '#fff', fontWeight: '700', fontSize: width * 0.035 }
});
