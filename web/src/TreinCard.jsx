import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from "./contexts/AuthContext.jsx";
import {Link} from "react-router-dom";

const TrainCard = ({ ex }) => {
    const [exerciseName, setExerciseName] = useState('');
    const { user } = useAuth(); // токен для авторизации

    useEffect(() => {
        const fetchTreinDetails = async () => {
            try {
                const response = await axios.get(`http://85.236.187.180:8080/api/workout/${ex.id}`, {
                    headers: {
                        'Authorization': `Bearer ${user.token}`
                    }
                });
                setExerciseName(response.data.name || 'Без названия');
            } catch (error) {
                console.error('Ошибка загрузки названия упражнения:', error);
                setExerciseName('Без названия');
            }
        };
        fetchTreinDetails();
    }, [ex.id, user.token]);

    return (
        <Link to={`/workout/${ex.id}`} className="exercise-link">
            <div className="exercise-card">
                <p>{exerciseName}</p>
            </div>
        </Link>

    );

};

export default TrainCard;
