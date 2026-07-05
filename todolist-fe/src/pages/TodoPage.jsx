import { useEffect, useMemo, useState } from 'react';
import { createTodo, deleteTodo, getTodos, toggleTodoStatus, updateTodo } from '../api/todoApi';
import TodoFilter from '../components/TodoFilter';
import TodoForm from '../components/TodoForm';
import TodoList from '../components/TodoList';
import Pagination from '../components/Pagination';

const initialFormState = {
  title: '',
  description: '',
  completed: false,
};

const TodoPage = () => {
  const pageSize = 5;
  const [todos, setTodos] = useState([]);
  const [pageInfo, setPageInfo] = useState({ pageNo: 1, pageSize, totalElements: 0, totalPages: 0, last: false });
  const [filters, setFilters] = useState({ keyword: '', status: 'all' });
  const [formData, setFormData] = useState(initialFormState);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const stats = useMemo(() => {
    const completed = todos.filter((todo) => todo.completed).length;
    return {
      total: todos.length,
      completed,
      pending: todos.length - completed,
    };
  }, [todos]);

  const loadTodos = async (nextPage = pageInfo.pageNo) => {
    try {
      setLoading(true);
      setError('');
      const data = await getTodos({
        pageNo: nextPage,
        pageSize,
        keyword: filters.keyword,
        status: filters.status,
      });
      setTodos(data.content || []);
      setPageInfo({
        pageNo: data.pageNo || 1,
        pageSize: data.pageSize || pageSize,
        totalElements: data.totalElements || 0,
        totalPages: data.totalPages || 0,
        last: data.last || false,
      });
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Không thể tải danh sách công việc');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTodos(1);
  }, []);

  const handleInputChange = (event) => {
    const { name, value, type, checked } = event.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const resetForm = () => {
    setFormData(initialFormState);
    setEditingId(null);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!formData.title.trim()) {
      setError('Tiêu đề không được để trống');
      return;
    }

    try {
      setSubmitting(true);
      setError('');
      if (editingId) {
        await updateTodo(editingId, {
          title: formData.title.trim(),
          description: formData.description.trim(),
          completed: formData.completed,
        });
        setSuccess('Cập nhật công việc thành công');
      } else {
        await createTodo({
          title: formData.title.trim(),
          description: formData.description.trim(),
        });
        setSuccess('Thêm công việc thành công');
      }
      resetForm();
      await loadTodos(1);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Không thể thực hiện thao tác');
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (todo) => {
    setEditingId(todo.id);
    setFormData({
      title: todo.title,
      description: todo.description || '',
      completed: todo.completed,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (todoId) => {
    const confirmed = window.confirm('Bạn có chắc chắn muốn xóa công việc này không?');
    if (!confirmed) return;

    try {
      setError('');
      await deleteTodo(todoId);
      setSuccess('Xóa công việc thành công');
      await loadTodos(pageInfo.pageNo);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Xóa công việc thất bại');
    }
  };

  const handleToggle = async (todoId) => {
    try {
      setError('');
      await toggleTodoStatus(todoId);
      setSuccess('Cập nhật trạng thái thành công');
      await loadTodos(pageInfo.pageNo);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Cập nhật trạng thái thất bại');
    }
  };

  const handleFilterChange = (event) => {
    const { name, value } = event.target;
    setFilters((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSearch = async () => {
    await loadTodos(1);
  };

  const handleReset = async () => {
    const resetFilters = { keyword: '', status: 'all' };
    setFilters(resetFilters);
    await loadTodos(1);
  };

  const handlePageChange = async (direction) => {
    const nextPage = direction === 'prev' ? pageInfo.pageNo - 1 : pageInfo.pageNo + 1;
    if (nextPage < 1 || nextPage > pageInfo.totalPages) return;
    await loadTodos(nextPage);
  };

  return (
    <div className="page-shell">
      <header className="hero-card card">
        <div>
          <p className="eyebrow">Hiệu quả làm việc</p>
          <h1>Ứng dụng công việc</h1>
          <p className="subtitle">Quản lý công việc hằng ngày một cách hiệu quả</p>
        </div>
      </header>

      <section className="stats-grid">
        <div className="card stat-card">
          <span>Tổng số công việc</span>
          <strong>{stats.total}</strong>
        </div>
        <div className="card stat-card">
          <span>Đã hoàn thành</span>
          <strong>{stats.completed}</strong>
        </div>
        <div className="card stat-card">
          <span>Chưa hoàn thành</span>
          <strong>{stats.pending}</strong>
        </div>
      </section>

      <div className="content-grid">
        <div className="left-column">
          <TodoForm
            formData={formData}
            editingId={editingId}
            onChange={handleInputChange}
            onSubmit={handleSubmit}
            onCancel={resetForm}
            isSubmitting={submitting}
          />
        </div>

        <div className="right-column">
          <TodoFilter
            filters={filters}
            onChange={handleFilterChange}
            onSearch={handleSearch}
            onReset={handleReset}
            loading={loading}
          />
          {error && <div className="alert alert-error">{error}</div>}
          {success && <div className="alert alert-success">{success}</div>}

          {loading ? (
            <div className="card loading-card">Đang tải danh sách...</div>
          ) : todos.length === 0 ? (
            <div className="card empty-card">Không có công việc nào phù hợp.</div>
          ) : (
            <>
              <TodoList todos={todos} onToggle={handleToggle} onEdit={handleEdit} onDelete={handleDelete} />
              <Pagination
                pageNo={pageInfo.pageNo}
                totalPages={pageInfo.totalPages}
                onPrev={() => handlePageChange('prev')}
                onNext={() => handlePageChange('next')}
                loading={loading}
              />
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default TodoPage;
