import axios from 'axios';

const api = axios.create({
  baseURL: '/api/v1/todo',
  timeout: 10000,
});

// Extract the actual payload from the common API response envelope.
const unwrap = (response) => response.data?.result ?? response.data;

export const getTodos = async ({ pageNo = 1, pageSize = 5, keyword = '', status = '' } = {}) => {
  const params = { pageNo, pageSize };
  if (keyword) params.keyword = keyword;
  if (status) params.status = status;

  const response = await api.get('/getTodo', { params });
  return unwrap(response);
};

export const getTodoById = async (todoId) => {
  const response = await api.get(`/${todoId}`);
  return unwrap(response);
};

export const createTodo = async (data) => {
  const response = await api.post('/create', data);
  return unwrap(response);
};

export const updateTodo = async (todoId, data) => {
  const response = await api.put(`/update/${todoId}`, data);
  return unwrap(response);
};

export const deleteTodo = async (todoId) => {
  const response = await api.delete(`/delete/${todoId}`);
  return unwrap(response);
};

export const toggleTodoStatus = async (todoId) => {
  const response = await api.patch(`/status/${todoId}`);
  return unwrap(response);
};
