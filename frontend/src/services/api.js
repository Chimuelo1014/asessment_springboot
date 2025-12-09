// src/services/api.js
import axios from 'axios';

const API_BASE_URL = '/api/v1';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);

// API methods
const api = {
  // Authentication
  auth: {
    login: (credentials) =>
      apiClient.post('/auth/login', credentials),
    
    register: (userData) =>
      apiClient.post('/auth/register', userData),
  },

  // Affiliates
  affiliates: {
    getAll: () =>
      apiClient.get('/affiliates'),
    
    getById: (id) =>
      apiClient.get(`/affiliates/${id}`),
    
    getByDocument: (document) =>
      apiClient.get(`/affiliates/document/${document}`),
    
    create: (data) =>
      apiClient.post('/affiliates', data),
    
    update: (id, data) =>
      apiClient.put(`/affiliates/${id}`, data),
  },

  // Credit Applications
  applications: {
    getAll: () =>
      apiClient.get('/credit-applications'),
    
    getById: (id) =>
      apiClient.get(`/credit-applications/${id}`),
    
    getMyApplications: (affiliateId) =>
      apiClient.get(`/credit-applications/my-applications?affiliateId=${affiliateId}`),
    
    getPending: () =>
      apiClient.get('/credit-applications/pending'),
    
    create: (data) =>
      apiClient.post('/credit-applications', data),
    
    evaluate: (id) =>
      apiClient.post(`/credit-applications/${id}/evaluate`),
    
    approve: (id, comments) =>
      apiClient.post(`/credit-applications/${id}/approve`, null, {
        params: { comments }
      }),
    
    reject: (id, comments) =>
      apiClient.post(`/credit-applications/${id}/reject`, null, {
        params: { comments }
      }),
  },
};

export default api;