import { createSlice } from '@reduxjs/toolkit';

const cartSlice = createSlice({
  name: 'cart',
  initialState: { items: [] },
  reducers: {
    addToCart(state, action) {
      const existing = state.items.find(i => i.id === action.payload.id);
      if (existing) existing.qty += action.payload.qty || 1;
      else state.items.push({ ...action.payload, qty: action.payload.qty || 1 });
    },
    updateQty(state, action) {
      const it = state.items.find(i => i.id === action.payload.id);
      if (it) it.qty = action.payload.qty;
    },
    removeFromCart(state, action) {
      state.items = state.items.filter(i => i.id !== action.payload.id);
    },
    clearCart(state) { state.items = []; }
  }
});

export const { addToCart, updateQty, removeFromCart, clearCart } = cartSlice.actions;
export default cartSlice.reducer;
