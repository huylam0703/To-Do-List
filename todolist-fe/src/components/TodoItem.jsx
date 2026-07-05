const TodoItem = ({ todo, onToggle, onEdit, onDelete }) => {
  return (
    <article className="todo-card card">
      <div className="todo-card__header">
        <div>
          <h4>{todo.title}</h4>
          <p>{todo.description || 'Không có mô tả.'}</p>
        </div>
        <span className={`status-badge ${todo.completed ? 'completed' : 'pending'}`}>
          {todo.completed ? 'Hoàn thành' : 'Chưa hoàn thành'}
        </span>
      </div>

      <div className="todo-meta">
        <span>Thời gian tạo: {new Date(todo.createdAt).toLocaleString('vi-VN')}</span>
        {todo.updatedAt && <span>Thời gian cập nhật: {new Date(todo.updatedAt).toLocaleString('vi-VN')}</span>}
      </div>

      <div className="todo-actions">
        <button className="btn btn-secondary" onClick={() => onToggle(todo.id)}>
          {todo.completed ? 'Đánh dấu chưa hoàn thành' : 'Đánh dấu hoàn thành'}
        </button>
        <button className="btn btn-secondary" onClick={() => onEdit(todo)}>
          Sửa
        </button>
        <button className="btn btn-danger" onClick={() => onDelete(todo.id)}>
          Xóa
        </button>
      </div>
    </article>
  );
};

export default TodoItem;
