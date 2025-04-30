import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from "./contexts/AuthContext.jsx";
import {Link} from "react-router-dom";

const ExerciseCard = ({ ex }) => {
    const [imageSrc, setImageSrc] = useState('/assets/default-exercise.png');
    const [exerciseName, setExerciseName] = useState('');
    const { user } = useAuth(); // токен для авторизации

    useEffect(() => {
        const fetchExerciseDetails = async () => {
            try {
                const response = await axios.get(`http://85.236.187.180:8080/api/exercise/${ex.id}`, {
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

        const fetchImage = async () => {
            try {
                const response = await axios.get(`http://85.236.187.180:8080/api/exercise/image/${ex.id}`, {
                    responseType: 'blob',
                    headers: {
                        'Authorization': `Bearer ${user.token}`
                    }
                });

                const imageUrl = URL.createObjectURL(response.data);
                setImageSrc(imageUrl);

                // чистим ссылку при размонтировании компонента
                return () => {
                    URL.revokeObjectURL(imageUrl);
                };
            } catch (error) {
                console.error('Ошибка загрузки изображения:', error);
                setImageSrc('/assets/image4.svg');
            }
        };

        fetchExerciseDetails();
        fetchImage();
    }, [ex.id, user.token]);

    return (
        <Link to={`/exercise/${ex.id}`} className="exercise-link">
            <div className="exercise-card">
                <img src={imageSrc} alt={ex.name} />
                <p>{exerciseName}</p>
            </div>
        </Link>

    );

};

export default ExerciseCard;
