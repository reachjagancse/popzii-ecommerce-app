import React from 'react';
import { View, Text, Image, SafeAreaView, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { useDispatch } from 'react-redux';
import { addToCart } from '../store/cartSlice';

const { width } = Dimensions.get('window');

export default function ProductDetailScreen({ route, navigation }){
  const { product } = route.params;
  const dispatch = useDispatch();

  return (
    <SafeAreaView style={styles.safe}>
      <Image source={{ uri: product.imageUrl || `https://picsum.photos/800/1000?random=${product.id}` }} style={styles.image} />
      <View style={styles.body}>
        <Text style={styles.name}>{product.name}</Text>
        <Text style={styles.price}>${product.basePrice ?? product.price}</Text>
        <Text style={styles.desc}>{product.description || 'No description available.'}</Text>
        <TouchableOpacity style={styles.buy} onPress={()=>{ dispatch(addToCart({ id:product.id, name:product.name, price:product.basePrice ?? product.price })); navigation.navigate('Cart'); }}>
          <Text style={styles.buyText}>Add to Cart</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff' },
  image: { width: '100%', height: width * 0.8, backgroundColor: '#f0f0f0' },
  body: { padding: width * 0.04 },
  name: { color: '#333', fontSize: width * 0.06, fontWeight: '800' },
  price: { color: '#FF7E8B', fontWeight: '800', marginTop: width * 0.02, fontSize: width * 0.05 },
  desc: { color: '#666', marginTop: width * 0.03, fontSize: width * 0.04 },
  buy: { backgroundColor: '#FF7E8B', padding: width * 0.04, borderRadius: 10, marginTop: width * 0.05, alignItems: 'center' },
  buyText: { color: '#fff', fontWeight: '800', fontSize: width * 0.045 }
});
