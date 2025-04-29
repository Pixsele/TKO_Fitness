import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Programm.css';

const Programm = () => {
    const [workouts, setWorkouts] = useState([]);
    const [filters, setFilters] = useState({
        muscleGroups: [],
        difficulty: '',
        sortField: 'name', // добавлено поле для сортировки
        sortDirection: 'asc', // добавлено направление сортировки
    });

    const { user, logout } = useAuth();

    const fetchWorkouts = async () => {
        try {
            const params = {
                sort: `${filters.sortField},${filters.sortDirection}`,
                difficulty: filters.difficulty || undefined,
                muscleGroups: filters.muscleGroups.length > 0 ? filters.muscleGroups : undefined,
            };

            const response = await axios.get('http://85.236.187.180:8080/api/program/page', {
                params,
                headers: {
                    'Authorization': `Bearer ${user.token}`,
                },
                paramsSerializer: params => {
                    const query = new URLSearchParams();
                    if (params.sort) query.append('sort', params.sort);
                    if (params.difficulty) query.append('difficulty', params.difficulty);
                    if (params.muscleGroups) {
                        params.muscleGroups.forEach(group => query.append('muscleGroups', group));
                    }
                    return query.toString();
                }
            });

            setWorkouts(response.data.content);
        } catch (error) {
            console.error('Ошибка при загрузке программ:', error);
        }
    };

    useEffect(() => {
        fetchWorkouts();
    }, [filters]);

    const toggleMuscleGroup = (group) => {
        setFilters(prev => ({
            ...prev,
            muscleGroups: prev.muscleGroups.includes(group)
                ? prev.muscleGroups.filter(g => g !== group)
                : [...prev.muscleGroups, group]
        }));
    };

    const handleDifficultyChange = (e) => {
        setFilters(prev => ({ ...prev, difficulty: e.target.value }));
    };

    const handleSortChange = (e) => {
        const value = e.target.value;
        if (value === 'name') {
            setFilters(prev => ({
                ...prev,
                sortField: 'name',
                sortDirection: 'asc',
            }));
        } else if (value === 'likeCount') {
            setFilters(prev => ({
                ...prev,
                sortField: 'likeCount',
                sortDirection: 'desc',
            }));
        } else if (value === 'favorite') {
            setFilters(prev => ({
                ...prev,
                sortField: 'favorite',
                sortDirection: 'desc',
            }));
        }
    };

    const handleLikeToggle = async (programId, liked) => {
        try {
            if (liked) {
                // Убираем лайк
                await axios.delete('http://85.236.187.180:8080/api/like-program', {
                    headers: {
                        'Authorization': `Bearer ${user.token}`,
                        'Content-Type': 'application/json',
                    },
                    data: {
                        trainingsProgramId: programId,
                        userId: user.userId,
                    },
                });
            } else {
                // Ставим лайк
                await axios.post('http://85.236.187.180:8080/api/like-program', {
                    trainingsProgramId: programId,
                    userId: user.userId,
                }, {
                    headers: {
                        'Authorization': `Bearer ${user.token}`,
                        'Content-Type': 'application/json',
                    }
                });
            }

            // После лайка перезагружаем список программ
            fetchWorkouts();
        } catch (error) {
            console.error('Ошибка при изменении лайка программы:', error);
        }
    };

    return (
        <div className="page-container">
            {/* Header */}
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

            {/* Content */}
            <div className="main-content">
                {/* Filters */}
                <div className="filters">
                    <button className="rectangle">
                        <div>
                            <h4>Сортировка</h4>
                            <label><input type="radio" value="name" checked={filters.sortField === 'name'} onChange={handleSortChange}/> По названию</label>
                            <label><input type="radio" value="likeCount" checked={filters.sortField === 'likeCount'} onChange={handleSortChange}/> Популярные</label>
                        </div>


                    </button>
                </div>

                {/* Programs */}
                <div className="workouts-container">
                    {workouts.map(workout => (
                        <div key={workout.id} className="workout-card">
                            <Link to={`/trein/${workout.id}`} className="workout-link">
                                <h5>{workout.name}</h5>
                            </Link>
                            <div className="like-section" onClick={() => handleLikeToggle(workout.id, workout.liked)}>
                                {workout.liked ? (
                                    <img src="/assets/liked.svg" alt="like" className="liked"/>
                                ) : (
                                    <img src="/assets/unliked.svg" alt="like" className="unliked"/>
                                )}
                                <span>{workout.likeCount}</span>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Programm;
