import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, SafeAreaView, StyleSheet, TouchableOpacity, TextInput, Dimensions, ScrollView } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { fetchProducts } from '../store/productsSlice';
import { fetchCategories } from '../store/categoriesSlice';
import ProductCard from '../components/ProductCard';
import { addToCart } from '../store/cartSlice';

const { width, height } = Dimensions.get('window');

export default function HomeScreen({ navigation }) {
  const dispatch = useDispatch();
  const products = useSelector(s => s.products.items);
  const categories = useSelector(s => s.categories.items);
  const [search, setSearch] = useState('');
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const [sidebarVisible, setSidebarVisible] = useState(true);

  useEffect(() => {
    dispatch(fetchCategories());
  }, [dispatch]);

  useEffect(() => {
    dispatch(fetchProducts({ categoryId: selectedCategoryId, search }));
  }, [dispatch, selectedCategoryId]);

  useEffect(() => {
    const timer = setTimeout(() => {
      dispatch(fetchProducts({ categoryId: selectedCategoryId, search }));
    }, 300);
    return () => clearTimeout(timer);
  }, [dispatch, search, selectedCategoryId]);

  useEffect(() => {
    if (!selectedCategoryId && categories.length > 0) {
      setSelectedCategoryId(categories[0].id);
    }
  }, [categories, selectedCategoryId]);

  const filteredProducts = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase())
  ).map(p => ({ ...p, price: p.basePrice ?? p.price }));

  const selectedCategory = categories.find(c => c.id === selectedCategoryId)?.name;

  const handlePress = p => navigation.navigate('ProductDetail', { product: p });
  const handleAdd = p => dispatch(addToCart({ id: p.id, name: p.name, price: p.basePrice ?? p.price }));

  const renderSidebar = () => (
    <View style={styles.sidebar}>
      <Text style={styles.sidebarTitle}>Categories</Text>
      <ScrollView showsVerticalScrollIndicator={false}>
        {categories.map(category => (
          <View key={category.id}>
            <TouchableOpacity
              style={[styles.categoryItem, selectedCategoryId === category.id && styles.selectedCategory]}
              onPress={() => setSelectedCategoryId(category.id)}
            >
              <Text style={[styles.categoryText, selectedCategoryId === category.id && styles.selectedCategoryText]}>
                {category.name}
              </Text>
            </TouchableOpacity>
          </View>
        ))}
      </ScrollView>
    </View>
  );

  return (
    <SafeAreaView style={styles.safe}>
      <View style={styles.container}>
        {sidebarVisible && renderSidebar()}
        <View style={[styles.mainContent, sidebarVisible && styles.mainContentWithSidebar]}>
          <View style={styles.header}>
            <TouchableOpacity
              style={styles.menuButton}
              onPress={() => setSidebarVisible(!sidebarVisible)}
            >
              <Text style={styles.menuIcon}>☰</Text>
            </TouchableOpacity>
            <Text style={styles.title}>Popzii</Text>
            <View style={styles.headerRight} />
          </View>

          <TextInput
            style={styles.search}
            placeholder="Search products..."
            value={search}
            onChangeText={setSearch}
            placeholderTextColor="#999"
          />

          <View style={styles.banner}>
            <Text style={styles.bannerText}>
              {selectedCategory ? `${selectedCategory} - Fresh & Quality Products!` : 'Fresh groceries delivered fast!'}
            </Text>
          </View>

          <FlatList
            contentContainerStyle={styles.grid}
            data={filteredProducts}
            keyExtractor={i => String(i.id)}
            numColumns={2}
            renderItem={({ item }) => (
              <ProductCard product={item} onPress={handlePress} onAdd={handleAdd} />
            )}
            ListEmptyComponent={
              <View style={styles.emptyContainer}>
                <Text style={styles.emptyText}>No products found</Text>
              </View>
            }
          />
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: '#fff' },
  container: { flex: 1, flexDirection: 'row' },
  sidebar: {
    width: width * 0.35,
    backgroundColor: '#f8f9fa',
    borderRightWidth: 1,
    borderRightColor: '#e9ecef',
    paddingTop: height * 0.02
  },
  sidebarTitle: {
    fontSize: width * 0.045,
    fontWeight: '700',
    color: '#333',
    paddingHorizontal: width * 0.03,
    paddingBottom: height * 0.015,
    borderBottomWidth: 1,
    borderBottomColor: '#e9ecef'
  },
  categoryItem: {
    paddingVertical: height * 0.015,
    paddingHorizontal: width * 0.03,
    backgroundColor: 'transparent'
  },
  selectedCategory: {
    backgroundColor: '#FF7E8B',
    borderRadius: 8,
    marginHorizontal: width * 0.02
  },
  categoryText: {
    fontSize: width * 0.04,
    fontWeight: '600',
    color: '#333'
  },
  selectedCategoryText: {
    color: '#fff'
  },
  subcategoryItem: {
    paddingVertical: height * 0.01,
    paddingHorizontal: width * 0.06,
    backgroundColor: 'transparent'
  },
  selectedSubcategory: {
    backgroundColor: '#ffe5e5'
  },
  subcategoryText: {
    fontSize: width * 0.035,
    color: '#666',
    fontWeight: '500'
  },
  selectedSubcategoryText: {
    color: '#FF7E8B',
    fontWeight: '600'
  },
  mainContent: {
    flex: 1,
    backgroundColor: '#fff'
  },
  mainContentWithSidebar: {
    flex: 1
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: width * 0.04,
    backgroundColor: '#FF7E8B',
    paddingTop: height * 0.02
  },
  menuButton: {
    padding: width * 0.02
  },
  menuIcon: {
    fontSize: width * 0.06,
    color: '#fff'
  },
  title: {
    flex: 1,
    color: '#fff',
    fontSize: width * 0.06,
    fontWeight: '800',
    textAlign: 'center'
  },
  headerRight: {
    width: width * 0.08
  },
  search: {
    backgroundColor: '#fff',
    borderRadius: 20,
    padding: width * 0.03,
    fontSize: width * 0.045,
    marginHorizontal: width * 0.04,
    marginVertical: height * 0.01
  },
  banner: {
    backgroundColor: '#FFE5E5',
    padding: width * 0.04,
    alignItems: 'center',
    marginHorizontal: width * 0.04,
    borderRadius: 10,
    marginBottom: height * 0.01
  },
  bannerText: {
    color: '#FF7E8B',
    fontSize: width * 0.045,
    fontWeight: '600',
    textAlign: 'center'
  },
  grid: {
    padding: width * 0.04,
    paddingBottom: height * 0.1
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: height * 0.2
  },
  emptyText: {
    fontSize: width * 0.045,
    color: '#666',
    textAlign: 'center'
  }
});
