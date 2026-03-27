import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { api } from '../api/api';

export const fetchCategories = createAsyncThunk('categories/fetch', async () => {
  const resp = await api.get('/api/instamart/categories?page=0&size=100');
  return resp.data?.data || [];
});

const categoriesSlice = createSlice({
  name: 'categories',
  initialState: { items: [], status: 'idle' },
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(fetchCategories.pending, state => { state.status = 'loading'; })
      .addCase(fetchCategories.fulfilled, (state, action) => { state.status = 'succeeded'; state.items = action.payload; })
      .addCase(fetchCategories.rejected, state => { state.status = 'failed'; });
  }
});

export default categoriesSlice.reducer;
