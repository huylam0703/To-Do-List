const TodoFilter = ({ filters, onChange, onSearch, onReset, loading }) => {
  return (
    <div className="card filter-card">
      <div className="filter-controls">
        <label className="field">
          <span>Tìm kiếm</span>
          <input
            type="text"
            name="keyword"
            value={filters.keyword}
            onChange={onChange}
            placeholder="Tìm theo tiêu đề..."
          />
        </label>

        <label className="field">
          <span>Trạng thái</span>
          <select name="status" value={filters.status} onChange={onChange}>
            <option value="all">Tất cả</option>
            <option value="completed">Hoàn thành</option>
            <option value="pending">Chưa hoàn thành</option>
          </select>
        </label>
      </div>

      <div className="filter-actions">
        <button type="button" className="btn btn-primary" onClick={onSearch} disabled={loading}>
          Tìm kiếm
        </button>
        <button type="button" className="btn btn-secondary" onClick={onReset}>
          Đặt lại
        </button>
      </div>
    </div>
  );
};

export default TodoFilter;
