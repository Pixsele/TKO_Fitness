import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Trein.css'
import TreinCard from "./TreinCard.jsx"; // создадим под неё отдельный css


const Trein = () => {
    const { id } = useParams(); // Получаем id из URL
    const [workout, setWorkout] = useState(null);
    const [exercises, setExercises] = useState([]);
    const { user, logout } = useAuth();

    useEffect(() => {
        const fetchTrein = async () => {
            try {
                const workoutResponse = await axios.get(`http://85.236.187.180:8080/api/program/${id}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setWorkout(workoutResponse.data);

                const exercisesResponse = await axios.get(`http://85.236.187.180:8080/api/workout-program/workout/${id}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setExercises(exercisesResponse.data);

            } catch (error) {
                console.error('Ошибка при загрузке данных о тренировке:', error);
            }
        };

        fetchTrein();
    }, [id]);

    if (!workout) return <div className="Z">Загрузка...</div>;

    return (
        <div className="trein-page-container">
            {/* Header (как у всех страниц) */}
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">TKO <span>Fitness</span></Link>
                </div>
                <div className="navigation-links">
                    <Link to="/catalog" className="nav-link">Тренировки</Link>
                    <Link to="/programm" className="nav-link-now">Программы</Link>
                    <Link to="/kcal" className="nav-link">Расчет ккал</Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%202.svg" alt="User Icon" />
                    <Link to="/profile" className="user-name">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>

            {/* Основной контент */}
            <div className="trein-detail-content">
                {/* Левая часть */}
                <div className="trein-left-section">
                    <h2 className="name-work">{workout.name}</h2>
                    <button className="opis"><strong></strong> {workout.description}</button>
                    <button className="slozn"><strong></strong> {workout.difficult}</button>
                    <div className="exercise-list">
                        {exercises.map(ex => (
                            <TreinCard key={ex.id} ex={ex} />
                        ))}
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Trein;
