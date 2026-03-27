import { Platform } from 'react-native';

// Adjust baseURL for device/emulator. Use your machine IP when testing on a physical device.
const baseURL = Platform.OS === 'android' ? 'http://10.0.2.2:4000' : 'http://localhost:4000'; // Android emulator -> 10.0.2.2

const api = {
  get: async (url, config = {}) => {
    const headers = { 'Content-Type': 'application/json', ...config.headers };
    const response = await fetch(`${baseURL}${url}`, {
      method: 'GET',
      headers,
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return { data: await response.json() };
  },
  post: async (url, data, config = {}) => {
    const headers = { 'Content-Type': 'application/json', ...config.headers };
    const response = await fetch(`${baseURL}${url}`, {
      method: 'POST',
      headers,
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return { data: await response.json() };
  },
  patch: async (url, data, config = {}) => {
    const headers = { 'Content-Type': 'application/json', ...config.headers };
    const response = await fetch(`${baseURL}${url}`, {
      method: 'PATCH',
      headers,
      body: data ? JSON.stringify(data) : undefined,
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return { data: await response.json() };
  },
};

export { api };
