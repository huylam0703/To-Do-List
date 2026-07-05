const Pagination = ({ pageNo, totalPages, onPrev, onNext, loading }) => {
  if (!totalPages) return null;

  return (
    <div className="pagination">
      <button className="btn btn-secondary" onClick={onPrev} disabled={pageNo <= 1 || loading}>
        Previous
      </button>
      <span>
        Page {pageNo} / {totalPages}
      </span>
      <button className="btn btn-secondary" onClick={onNext} disabled={pageNo >= totalPages || loading}>
        Next
      </button>
    </div>
  );
};

export default Pagination;
