import {useEffect, useState} from "react";
import {useParams} from "react-router";
import * as InPollApi from "../api/InPollApi.js";
import ResultQuestionItem from "../components/ResultQuestionItem.jsx";

export default function AggregateResultsPage() {

    const {id} = useParams();

    const [data, setData] = useState({results: []});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        InPollApi.getAggregate(id)
            .then(result => {
                setData(result || {results: []});
            })
            .finally(() => {
                setLoading(false);
            });

    }, [id]);

    if (loading) {
        return (
            <div className="container mt-3">
                <p>Loading results...</p>
            </div>
        );
    }

    const hasAnyAnswers =
        data.results.some(r =>
            (r.questionType === "BOOLEAN" && (r.yesCount + r.noCount > 0)) ||
            (r.questionType === "NUMERIC" && r.averageNumeric !== 0)
        );

    return (
        <div className="container mt-3">

            <h2>Aggregate Results</h2>

            {hasAnyAnswers ? (
                data.results.map(r => (
                    <ResultQuestionItem key={r.questionId} result={r} />
                ))
            ) : (
                <p>No boolean or numeric answers available.</p>
            )}

        </div>
    );
}