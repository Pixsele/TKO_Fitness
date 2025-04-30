import {Link, useParams} from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './ExercisePage.css'; // создадим под неё отдельный css

const ExercisePage = () => {
    const { id } = useParams();
    const { user , logout } = useAuth();
    const [svgData, setSvgData] = useState({ front: '', back: '' });
    const [exercise, setExercise] = useState(null);
    const [videoUrl, setVideoUrl] = useState('');

    useEffect(() => {
        const fetchExerciseData = async () => {
            try {
                const exerciseResponse = await axios.get(`http://85.236.187.180:8080/api/exercise/${id}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setExercise(exerciseResponse.data);

                const gender = user.gender || 'MALE'; // берем пол пользователя
                const svgResponse = await axios.get(`http://85.236.187.180:8080/api/exercise/svg/${id}?gender=${gender}`, {
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                setSvgData(svgResponse.data); // сохраняем { front: ..., back: ... }

                const videoResponse = await axios.get(`http://85.236.187.180:8080/api/exercise/video/${id}`, {
                    responseType: 'blob',
                    headers: { Authorization: `Bearer ${user.token}` }
                });
                const videoBlobUrl = URL.createObjectURL(videoResponse.data);
                setVideoUrl(videoBlobUrl);

            } catch (error) {
                console.error('Ошибка при загрузке данных упражнения:', error);
            }
        };

        fetchExerciseData();
    }, [id, user.token]);


    if (!exercise) return <div className="Z">Загрузка...</div>;

    return (
        <div className="page-container">
            {/* Тут шапка как всегда */}
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

            <div className="workout-detail-content">
                {/* Левая часть */}
                <div className="left-section">
                    <h1 className="name-work">{exercise.name}</h1>
                    <div className="opis">{exercise.instruction}</div>
                    <div className="slozn">Сложность: {exercise.difficult}</div>
                    <button className="svg">
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

                {/* Правая часть */}
                <div className="right-section-exercise">
                    {videoUrl && (
                        <video controls className="video">
                            <source src={videoUrl} type="video/mp4" />
                            Ваш браузер не поддерживает воспроизведение видео.
                        </video>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ExercisePage;
