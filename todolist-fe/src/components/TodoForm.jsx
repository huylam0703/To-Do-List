const TodoForm = ({ formData, editingId, onChange, onSubmit, onCancel, isSubmitting }) => {
  const isEditing = Boolean(editingId);

  return (
    <form className="card form-card" onSubmit={onSubmit}>
      <div className="form-header">
        <div>
          <p className="eyebrow">Quản lý công việc</p>
          <h3>{isEditing ? 'Cập nhật công việc' : 'Thêm công việc mới'}</h3>
        </div>
      </div>

      <label className="field">
        <span>Tiêu đề</span>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={onChange}
          placeholder="Nhập tiêu đề công việc"
          required
        />
      </label>

      <label className="field">
        <span>Mô tả</span>
        <textarea
          name="description"
          value={formData.description}
          onChange={onChange}
          placeholder="Thêm chi tiết cho công việc"
          rows="4"
        />
      </label>

      {isEditing && (
        <label className="checkbox-row">
          <input
            type="checkbox"
            name="completed"
            checked={Boolean(formData.completed)}
            onChange={onChange}
          />
          <span>Đánh dấu đã hoàn thành</span>
        </label>
      )}

      <div className="form-actions">
        <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
          {isSubmitting ? 'Đang lưu...' : isEditing ? 'Cập nhật công việc' : 'Thêm công việc'}
        </button>
        {isEditing && (
          <button type="button" className="btn btn-secondary" onClick={onCancel}>
            Hủy
          </button>
        )}
      </div>
    </form>
  );
};

export default TodoForm;
