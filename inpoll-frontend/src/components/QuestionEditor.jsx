export default function QuestionEditor({question, index, removeQuestion, updateQuestion}) {

    function updateField(field, value) {
        updateQuestion(index, field, value);
    }

    return (
        <div className="card p-3 mb-3">

            <h5>Question {index + 1}</h5>

            <div className="mb-3">
                <input
                    className="form-control"
                    value={question.question}
                    onChange={e => updateField("question", e.target.value)}
                    placeholder="Question text"
                />
            </div>

            <div className="mb-3">
                <select
                    className="form-select"
                    value={question.type}
                    onChange={e => updateField("type", e.target.value)}
                >
                    <option value="TEXT">Text</option>
                    <option value="BOOLEAN">Boolean</option>
                    <option value="NUMERIC">Numeric</option>
                </select>
            </div>

            <div className="d-grid">
                <button
                    className="btn btn-danger"
                    onClick={() => removeQuestion(index)}
                >
                    Delete Question
                </button>
            </div>

        </div>
    );
}