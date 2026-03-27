import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { api } from '../api/api';

export const fetchProducts = createAsyncThunk('products/fetch', async ({ categoryId, search } = {}) => {
  const params = new URLSearchParams();
  if (categoryId) params.append('categoryId', categoryId);
  if (search) params.append('search', search);
  params.append('page', '0');
  params.append('size', '100');
  const resp = await api.get(`/api/instamart/products?${params.toString()}`);
  return resp.data?.data || [];
});

const productsSlice = createSlice({
  name: 'products',
  initialState: { items: [], status: 'idle' },
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(fetchProducts.pending, state => { state.status = 'loading'; })
      .addCase(fetchProducts.fulfilled, (state, action) => { state.status = 'succeeded'; state.items = action.payload; })
      .addCase(fetchProducts.rejected, state => { state.status = 'failed'; });
  }
});

export default productsSlice.reducer;
