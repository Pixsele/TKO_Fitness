import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Profile.css';
import EditForm from "./EditForm.jsx";

const Profile = () => {
    const { user, logout } = useAuth();
    const [profileData, setProfileData] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [message, setMessage] = useState('');

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

    const handleSave = async (updatedData) => {
        try {
            await axios.put(`http://85.236.187.180:8080/api/user/${user.userId}`, updatedData, {
                headers: {
                    Authorization: `Bearer ${user.token}`,
                    'Content-Type': 'application/json'
                }
            });
            setProfileData({ ...profileData, ...updatedData });
            setShowEditModal(false);
            setTimeout(() => setMessage(''), 3000);
        } catch (error) {
            console.error('Ошибка при сохранении данных:', error);
        }
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
                    <Link to="/kcal" className="nav-link">Расчет ккал</Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%203.svg" alt="User Icon" />
                    <Link to="/profile" className="user-name-p">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>

            <button className="profile-content">
                <img
                    src="/assets/Mask%20group.svg"
                    alt="Avatar"
                    className="profile-avatar"
                />
                <div className="profile-info">
                    <p><strong>Имя:</strong> {profileData.name}</p>
                    <p><strong>Логин:</strong> {profileData.login}</p>
                    <p><strong>Рост:</strong> {profileData.height} см</p>
                    <p><strong>Вес:</strong> {profileData.weight} кг</p>
                    <p><strong>Дата рождения:</strong> {profileData.birthDay}</p>
                    <p><strong>Целевые ккал:</strong> {profileData.targetKcal}</p>

                    <span onClick={() => setShowEditModal(true)} className="edit-link">Изменить данные</span>
                </div>
            </button>

            {showEditModal && (
                <EditForm
                    profileData={profileData}
                    onClose={() => setShowEditModal(false)}
                    onSave={handleSave}
                />
            )}

            {message && <div className="profile-message">{message}</div>}
        </div>
    );
};

export default Profile;
