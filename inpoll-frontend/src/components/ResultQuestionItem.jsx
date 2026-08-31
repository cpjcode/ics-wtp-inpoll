export default function ResultQuestionItem({result}) {

    const isBoolean = result.yesCount + result.noCount > 0;

    return (
        <div className="card p-3 mb-3">

            <h5>{result.question}</h5>

            {isBoolean ? (
                <>
                    <p>Yes: {result.yesCount}</p>
                    <p>No: {result.noCount}</p>
                </>
            ) : (
                <>
                    <p>Average: {result.averageNumeric}</p>
                </>
            )}

        </div>
    );
}