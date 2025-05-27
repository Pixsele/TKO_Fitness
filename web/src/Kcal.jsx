import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Kcal.css';

const Kcal = () => {
    const { user, logout } = useAuth();
    const [profileData, setProfileData] = useState(null);
    const [activityLevel, setActivityLevel] = useState(1.2);
    const [goal, setGoal] = useState('maintain');
    const [calories, setCalories] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`http://85.236.187.180:8080/api/user/${user.userId}`, {
                    headers: {
                        Authorization: `Bearer ${user.token}`
                    }
                });
                setProfileData(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке профиля:', error);
            }
        };

        fetchProfile();
    }, [user]);

    const calculateCalories = () => {
        if (!profileData) return;

        const weight = profileData.weight;
        const height = profileData.height;
        const birthDate = new Date(profileData.birthDay);
        const gender = profileData.gender;

        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }

        let bmr;
        if (gender === 'MALE') {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        let maintenanceCalories = bmr * activityLevel;

        let targetCalories;
        if (goal === 'lose') {
            targetCalories = maintenanceCalories * 0.85; // -15%
        } else if (goal === 'gain') {
            targetCalories = maintenanceCalories * 1.15; // +15%
        } else {
            targetCalories = maintenanceCalories;
        }

        setCalories(Math.round(targetCalories));
    };

    if (!profileData) return <div className="load-prof">Загрузка профиля...</div>;

    return (
        <div className="home-container">
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">TKO Fitness</Link>
                </div>
                <div className="navigation-links">
                    <Link to="/catalog" className="nav-link">Тренировки</Link>
                    <Link to="/programm" className="nav-link">Программы</Link>
                    <Link to="/kcal" className="nav-link-now">Расчет ккал</Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%202.svg" alt="User Icon" />
                    <Link to="/profile" className="user-name">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>

            <div className="kcal-profile-content">
                <div className="rasch">
                    <h1 className="rasch-h1">Расчет ккал</h1>
                    <p className="rasch-p"><strong>Рост:</strong> {profileData.height} см</p>
                    <p className="rasch-p"><strong>Вес:</strong> {profileData.weight} кг</p>
                    <p className="rasch-p"><strong>Дата рождения:</strong> {profileData.birthDay}</p>
                    <p className="rasch-p"><strong>Пол:</strong> {profileData.gender}</p>

                    <div className="selectors">
                        <div className="selector-activ">
                            <label>Уровень активности:</label>
                            <select value={activityLevel} onChange={(e) => setActivityLevel(parseFloat(e.target.value))}>
                                <option value="1.2">Минимальная (сидячий образ жизни)</option>
                                <option value="1.375">Легкая (1–3 тренировки в неделю)</option>
                                <option value="1.55">Средняя (3–5 раз в неделю)</option>
                                <option value="1.725">Высокая (6–7 раз в неделю)</option>
                                <option value="1.9">Очень высокая (2 тренировки в день)</option>
                            </select>
                        </div>

                        <div className="selector-activ">
                            <label>Цель:</label>
                            <select value={goal} onChange={(e) => setGoal(e.target.value)}>
                                <option value="lose">Похудение</option>
                                <option value="maintain">Поддержание</option>
                                <option value="gain">Набор массы</option>
                            </select>
                        </div>
                    </div>

                    <button
                        onClick={calculateCalories}
                        className="kcal-calculate-button"
                    >
                        Рассчитать
                    </button>

                    {calories !== null && (
                        <div className="result-kcal">
                            <h2>Рекомендуемая калорийность: {calories} ккал/день</h2>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Kcal;
