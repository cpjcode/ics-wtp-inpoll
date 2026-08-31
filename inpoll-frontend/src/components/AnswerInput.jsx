export default function AnswerInput({question, answer, updateAnswer}) {

    function handleChange(e) {

        if (question.questionType === "NUMERIC") {

            let value = Number(e.target.value);

            if (!isNaN(value)) {
                value = Math.max(1, Math.min(5, value));
            }

            updateAnswer(question.id, value.toString());
            return;
        }

        updateAnswer(question.id, e.target.value);
    }

    return (
        <div className="card p-3 mb-2">

            <h6>{question.question}</h6>

            {question.questionType === "TEXT" && (
                <input
                    className="form-control"
                    value={answer}
                    onChange={handleChange}
                />
            )}

            {question.questionType === "BOOLEAN" && (
                <select
                    className="form-select"
                    value={answer}
                    onChange={handleChange}
                >
                    <option value="">-- select --</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>
            )}

            {question.questionType === "NUMERIC" && (
                <input
                    type="number"
                    className="form-control"
                    min="1"
                    max="5"
                    step="1"
                    value={answer}
                    onChange={handleChange}
                />
            )}

        </div>
    );
}