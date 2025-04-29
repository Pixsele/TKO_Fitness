import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './WorkoutDetail.css'; // создадим под неё отдельный css
import ExerciseCard from "./ExerciseCard.jsx";

const WorkoutDetail = () => {
    const { id } = useParams(); // Получаем id из URL
    const [workout, setWorkout] = useState(null);
    const [exercises, setExercises] = useState([]);
    const [svgData, setSvgData] = useState({ front: '', back: '' });
    const { user, logout } = useAuth();

    useEffect(() => {
        const fetchWorkoutDetails = async () => {
            try {
                const workoutResponse = await axios.get(`http://85.236.187.180:8080/api/workout/${id}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setWorkout(workoutResponse.data);

                const gender = user.gender || 'MALE'; // берем пол пользователя
                const svgResponse = await axios.get(`http://85.236.187.180:8080/api/workout/svg/${id}?gender=${gender}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setSvgData(svgResponse.data); // сохраняем { front: ..., back: ... }

                const exercisesResponse = await axios.get(`http://85.236.187.180:8080/api/workout-exercise/workout/${id}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setExercises(exercisesResponse.data);

            } catch (error) {
                console.error('Ошибка при загрузке данных о тренировке:', error);
            }
        };

        fetchWorkoutDetails();
    }, [id]);

    if (!workout) return <div className="Z">Загрузка...</div>;

    return (
        <div className="page-container">
            {/* Header (как у всех страниц) */}
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">TKO <span>Fitness</span></Link>
                </div>
                <div className="navigation-links">
                    <Link to="/catalog" className="nav-link-now">Тренировки</Link>
                    <Link to="/programm" className="nav-link">Программы</Link>
                    <Link to="/kcal" className="nav-link">Расчет ккал</Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%202.svg" alt="User Icon" />
                    <Link to="/profile" className="user-name">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>

            {/* Основной контент */}
            <div className="workout-detail-content">
                {/* Левая часть */}
                <div className="left-section">
                    <h2 className="name-work">{workout.name}</h2>
                    <button className="opis"><strong></strong> {workout.description}</button>
                    <button className="slozn"><strong></strong> {workout.difficult}</button>

                    <div className="exercise-list">
                        {exercises.map(ex => (
                            <ExerciseCard key={ex.id} ex={ex} />
                        ))}
                    </div>

                </div>

                {/* Правая часть */}
                <button className="rig-section">
                    {svgData.front && (
                        <div className="svg-wrapper">
                            <div dangerouslySetInnerHTML={{ __html: svgData.front }} />
                        </div>
                    )}
                    {svgData.back && (
                        <div className="svg-wrapper">
                            <div dangerouslySetInnerHTML={{ __html: svgData.back }} />
                        </div>
                    )}
                </button>

            </div>
        </div>
    );
};

export default WorkoutDetail;
