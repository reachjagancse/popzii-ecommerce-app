import { createSlice } from '@reduxjs/toolkit';

const ordersSlice = createSlice({
  name: 'orders',
  initialState: { items: [] },
  reducers: {
    addOrder(state, action) { state.items.unshift(action.payload); },
    updateOrderStatus(state, action) {
      const o = state.items.find(x => x.id === action.payload.id);
      if (o) o.status = action.payload.status;
    }
  }
});

export const { addOrder, updateOrderStatus } = ordersSlice.actions;
export default ordersSlice.reducer;
